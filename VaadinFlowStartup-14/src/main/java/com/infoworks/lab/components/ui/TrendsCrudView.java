package com.infoworks.lab.components.ui;

import com.infoworks.lab.components.component.FormActionBar;
import com.infoworks.lab.domain.beans.queues.EventQueue;
import com.infoworks.lab.domain.entities.Trend;
import com.infoworks.lab.domain.repository.TrendRepository;
import com.infoworks.lab.layouts.ApplicationLayout;
import com.infoworks.lab.layouts.RoutePath;
import com.infoworks.lab.rest.models.events.EventType;
import com.infoworks.lab.rest.repository.RestRepository;
import com.it.soul.lab.data.simple.SimpleDataSource;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@PageTitle("Trends CRUD")
@Route(value = RoutePath.TRENDS_CRUD_VIEW, layout = ApplicationLayout.class)
public class TrendsCrudView<Entity extends Trend> extends Composite<Div> {

    private Dialog dialog;
    private FormActionBar bottomActionBar;
    private EventType formEventType = EventType.CREATE;
    private Entity selected;
    private Grid<Entity> grid;
    private SimpleDataSource<Integer, Entity> entityCache = new SimpleDataSource<>();
    private Map<String, Component> formLayoutMap = new ConcurrentHashMap<>();

    public TrendsCrudView(Dialog dialog) {
        this.dialog = dialog;
        this.bottomActionBar = new FormActionBar(dialog);
    }

    public TrendsCrudView() {
        this(null);
    }

    private Class<Entity> getEntityClass() {
        return (Class<Entity>) Trend.class;
    }

    private Entity newEntity() {
        return (Entity) new Trend();
    }

    private RestRepository repository;
    private RestRepository getRepository() {
        if (repository == null) {
            repository = new TrendRepository();
        }
        return repository;
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if (getContent().getChildren().count() > 0) {
            getContent().removeAll();
        }
        super.onAttach(attachEvent);
        //Add to UI:
        //Form:
        Component formLayout = createEntityForm();
        updateFormFields(newEntity(), formLayoutMap);
        //ActionBar:Save Button
        Button save = new Button("Save", VaadinIcon.FORM.create());
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(e -> {
            UI ui = e.getSource().getUI().orElse(null);
            boolean isSaved = onEntitySaveAction(formEventType, selected);
            if(isSaved) {
                onClearAction(ui);
                reloadGrid(ui);
            } else {
                Notification notification = Notification.show("Failed to Save!"
                        , 1500
                        , Notification.Position.TOP_CENTER);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        formLayoutMap.put("save", save);
        //ActionBar: Delete Button
        Button delete = new Button("Delete", VaadinIcon.CLOSE.create());
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.addClickListener(e -> {
            UI ui = e.getSource().getUI().orElse(null);
            if (selected != null) {
                boolean isDeleted = onEntityDeleteAction(selected);
                if(isDeleted) {
                    onClearAction(ui);
                    reloadGrid(ui);
                }
            } else {
                Notification notification = Notification.show("Failed to Delete!"
                        , 1500
                        , Notification.Position.TOP_CENTER);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        formLayoutMap.put("delete", delete);
        //ActionBar: Clear Button
        Button clear = new Button("Clear", VaadinIcon.RECYCLE.create());
        clear.addClickListener(e -> {
            UI ui = e.getSource().getUI().orElse(null);
            onClearAction(ui);
        });
        //Form Action Bar:
        HorizontalLayout actionBar = new HorizontalLayout();
        //actionBar.setSpacing(true);
        actionBar.setPadding(true);
        actionBar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.STRETCH);
        actionBar.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        actionBar.setWidthFull();
        actionBar.add(save, delete, clear);
        //Grid:
        grid = createGrid(getEntityClass(), 10);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.addSelectionListener(selectionEvent -> {
            UI ui = selectionEvent.getSource().getUI().orElse(null);
            Set<Entity> isSelected = selectionEvent.getAllSelectedItems();
            if (isSelected.size() > 0) {
                //Update Form with Selected-Item:
                Entity entity = isSelected.iterator().next();
                selected = entity;
                EventQueue.dispatch(100, TimeUnit.MILLISECONDS
                        , () -> ui.access(() -> {
                            formEventType = EventType.UPDATE;
                            updateSaveTextState();
                            updateFormFields(selected, formLayoutMap);
                            updateSaveAndDeleteActiveState(entity);
                        }));
            }
        });
        //Grid Container:
        VerticalLayout gridLayout = new VerticalLayout();
        gridLayout.setPadding(true);
        gridLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.STRETCH);
        gridLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        gridLayout.setWidthFull();
        gridLayout.add(grid);
        if (dialog != null) {
            //FormActionBar Config:
            bottomActionBar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.STRETCH);
            bottomActionBar.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
            bottomActionBar.setWidthFull();
            gridLayout.add(bottomActionBar);
        }
        //Adding to UI:
        getContent().add(formLayout, actionBar, gridLayout);
        //Dispatch fetch:
        UI ui = UI.getCurrent();
        reloadGrid(ui);
    }

    private boolean onEntityDeleteAction(Entity selected) {
        //return getRepository().delete(selected.getId(), AuthRepository.parseToken(UI.getCurrent()));
        return false;
    }

    private void onClearAction(UI ui) {
        selected = null;
        formEventType = EventType.CREATE;
        updateSaveTextState();
        updateFormFields(newEntity(), formLayoutMap);
        updateSaveAndDeleteActiveState(null);
    }

    private void updateSaveTextState() {
        Button button = (Button) formLayoutMap.get("save");
        if (this.formEventType == EventType.UPDATE) {
            button.setIcon(VaadinIcon.CHECK.create());
            button.setText("Update");
        } else {
            button.setIcon(VaadinIcon.FORM.create());
            button.setText("Save");
        }
    }

    private void updateSaveAndDeleteActiveState(Entity entity) {
        Button save = (Button) formLayoutMap.get("save");
        Button delete = (Button) formLayoutMap.get("delete");
        if (entity == null) {
            save.setEnabled(true);
            delete.setEnabled(true);
            return;
        }
        //TODO
    }

    private void reloadGrid(UI ui) {
        EventQueue.dispatch(250, TimeUnit.MILLISECONDS
                , () -> ui.access(() -> {
                    //TODO:
                    List<Entity> fetched = (List<Entity>) getRepository().fetch(0, 20);
                    this.grid.setItems(fetched);
                    //Caching:
                    if (this.entityCache.size() > 0) this.entityCache.clear();
                    fetched.forEach(obj -> this.entityCache.put(obj.getId(), obj));
                }));
    }

    private Grid<Entity> createGrid(Class<Entity> type, int pageSize) {
        Grid<Entity> grid = new Grid<>(type, false);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setPageSize(pageSize);
        grid.setAllRowsVisible(true);
        grid.setWidthFull();
        //Columns
        grid.addColumn(entity -> entity.getId())
                .setHeader("Uuid").setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(2);

        grid.addColumn(entity -> entity.getTitle())
                .setHeader("Title").setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(4);

        grid.addColumn(entity -> entity.getSubtitle())
                .setHeader("Subtitle").setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(4);
        return grid;
    }

    private Component createEntityForm() {
        //1st Form:
        FormLayout itemLayout = new FormLayout();
        //Config form responsive:
        itemLayout.setWidth("650px");
        itemLayout.setResponsiveSteps(
                // Use one column by default
                new FormLayout.ResponsiveStep("0", 1),
                // Use two columns, if the layout's width exceeds 220px
                new FormLayout.ResponsiveStep("220px", 2),
                // Use three columns, if the layout's width exceeds 320px
                new FormLayout.ResponsiveStep("320px", 3)
        );
        //Title:
        TextField title = new TextField("Title:");
        formLayoutMap.put("title", title);
        itemLayout.setColspan(title, 2);
        itemLayout.add(title);
        //Price:
        BigDecimalField price = new BigDecimalField("Price: (E.g. 1.00 or 10.00)");
        formLayoutMap.put("price", price);
        price.setValue(new BigDecimal("0.00"));
        //price.setHelperText("E.g. 1.00 or 10.00");
        itemLayout.add(price);
        //Detail:
        TextField description = new TextField("Detail:");
        formLayoutMap.put("detail", description);
        itemLayout.setColspan(description, 3);
        itemLayout.add(description);
        //End 1st Form.
        //2nd Form:
        FormLayout otherLayout = new FormLayout();
        //Config form responsive:
        otherLayout.setWidth("650px");
        otherLayout.setResponsiveSteps(
                // Use one column by default
                new FormLayout.ResponsiveStep("0", 1),
                // Use two columns, if the layout's width exceeds 320px
                new FormLayout.ResponsiveStep("320px", 2)
        );
        //Amount:
        BigDecimalField amount = new BigDecimalField("Collection Amount: (E.g. 1.00 or 10.00)");
        amount.setValue(new BigDecimal("0.00"));
        //amount.setHelperText("E.g. 1.00 or 10.00");
        otherLayout.setColspan(amount, 2);
        otherLayout.add(amount);
        formLayoutMap.put("amount", amount);
        //From Address:
        TextField fromAddress = new TextField("From Address:");
        fromAddress.setPlaceholder("e.g. street; block; zip; city; state; country");
        otherLayout.setColspan(fromAddress, 2);
        otherLayout.add(fromAddress);
        formLayoutMap.put("fromAddress", fromAddress);
        //To Address:
        TextField toAddress = new TextField("To Address:");
        toAddress.setPlaceholder("e.g. street; block; zip; city; state; country");
        otherLayout.setColspan(toAddress, 2);
        otherLayout.add(toAddress);
        formLayoutMap.put("toAddress", toAddress);
        //End 2nd Form.
        //Final wrapper:
        HorizontalLayout splitLayout = new HorizontalLayout();
        splitLayout.setPadding(true);
        splitLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.STRETCH);
        splitLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        splitLayout.setWidthFull();
        splitLayout.add(itemLayout, otherLayout);
        return splitLayout;
    }

    private void updateFormFields(Entity entity, Map<String, Component> formLayoutMap) {
        //Update Form Fields:
    }

    private void updateEntity(Entity entity, Map<String, Component> formLayoutMap) {
        //Get All Fields:
    }

    private boolean onEntitySaveAction(EventType formEventType, Entity selected) {
        //TODO:
        return false;
    }

}
