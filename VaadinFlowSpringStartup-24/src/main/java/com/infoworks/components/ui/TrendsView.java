package com.infoworks.components.ui;

import com.infoworks.components.component.ConfirmDeleteAction;
import com.infoworks.components.component.IndeterminateDialog;
import com.infoworks.components.presenters.Forms.TrendsForm;
import com.infoworks.components.presenters.GridView.GridView;
import com.infoworks.config.AppQueue;
import com.infoworks.domain.tasks.DisplayAsyncNotification;
import com.infoworks.domain.entities.Trend;
import com.infoworks.applayouts.RootLayout;
import com.infoworks.applayouts.RoutePath;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.concurrent.TimeUnit;

@PageTitle("Trends")
@Route(value = RoutePath.TRENDS_VIEW, layout = RootLayout.class)
public class TrendsView<Entity extends Trend> extends Composite<Div> {

    private GridView gridView;

    public TrendsView() {
        getContent().add(new Span("Trends"));
    }

    private Class<Entity> getEntityClass() {
        return (Class<Entity>) Trend.class;
    }

    private Entity newEntity() {
        return (Entity) new Trend();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if (getContent().getChildren().count() > 0){
            getContent().removeAll();
        }
        super.onAttach(attachEvent);
        gridView = createGridView();
        //Add GridView + Context-Menu-For-Grid to the View:
        getContent().add(gridView, new TrendContextMenu(gridView.getGrid()));
        //Trigger data-loading from rest-api-call: Async
        //TODO: Set PagingTask, GetTask, SearchTask:
        //...
        gridView.dispatchAsyncLoad(UI.getCurrent().getUI().orElse(null));
    }

    private GridView createGridView() {
        GridView<Entity> gridView = new GridView(getEntityClass()
                , 10
                , "enabled");
        gridView.getGrid().addColumn(createTemplateRenderer())
                .setHeader("Trend")
                .setAutoWidth(true).setFlexGrow(26);
        gridView.getGrid().addColumn(createStatusComponentRenderer())
                .setHeader("Status")
                .setAutoWidth(true).setFlexGrow(2);
        //Configure Action Columns:
        //Add Edit & Delete:
        gridView.getGrid().addComponentColumn(createActionBar())
                .setHeader("Action")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setAutoWidth(true).setFlexGrow(1);
        //Add Context-Menu:
        gridView.getGrid().addComponentColumn(createContextMenuBar())
                .setAutoWidth(true).setFlexGrow(1);
        //Configure Add New Item Button Event on GridSearchView:
        //Note:On providing AddNewItemListener to GridView, will enable GridSearchView to show 'Add New!' Button.
        gridView.addNewItemEventListener(event -> {
            //TODO: Popup New Item FormView:
            Dialog dialog = new Dialog();
            dialog.getElement().setAttribute("aria-label", "Create New Trends!");
            dialog.addDetachListener((dlgCloseEvent) -> {
                //Now reload GridView using fetchTask and countTask in sequence Or otherwise:
                UI ui = dlgCloseEvent.getSource().getUI().orElse(null);
                this.gridView.dispatchAsyncLoad(ui);
            });
            //Config user-form
            TrendsForm form = new TrendsForm(null, dialog);
            dialog.add(form);
            dialog.open();
        });
        //For Dynamic Table Height.
        gridView.getGrid().setAllRowsVisible(true);
        return gridView;
    }

    private LitRenderer<Entity> createTemplateRenderer() {
        return LitRenderer.<Entity>of(
                        "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                                + "<vaadin-avatar img=\"[[item.pictureUrl]]\" name=\"[[item.title]]\" alt=\"User avatar\"></vaadin-avatar>"
                                + "  <vaadin-vertical-layout style=\"line-height: var(--lumo-line-height-m);\">"
                                + "    <span> [[item.title]] </span>"
                                + "    <span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">"
                                + "      [[item.subtitle]]" + "    </span>"
                                + "  </vaadin-vertical-layout>"
                                + "</vaadin-horizontal-layout>")
                .withProperty("pictureUrl", Entity::getPictureUrl)
                .withProperty("title", Entity::getTitle)
                .withProperty("subtitle", Entity::getSubtitle);
    }

    private ComponentRenderer<Span, Entity> createStatusComponentRenderer() {
        return new ComponentRenderer<>(Span::new, (span, entity) -> {
            span.setText(entity.isEnabled() ? "Active" : "Inactive");
            String theme = String
                    .format("badge %s", entity.isEnabled() ? "success" : "error");
            span.getElement().getThemeList().add(theme);
            //span.getElement().setAttribute("theme", theme);
        });
    }

    private ValueProvider<Entity, HorizontalLayout> createActionBar() {
        return (entity) -> {
            Button recycleButton = new Button("", VaadinIcon.RECYCLE.create());
            recycleButton.addClickListener(e -> {
                UI ui = e.getSource().getUI().orElse(null);
                Dialog dialog = new IndeterminateDialog("Recycle In Progress...");
                dialog.addDetachListener((dlgCloseEvent) -> {
                    //Update GridView:
                    this.gridView.dispatchAsyncLoad(ui);
                });
                //Using progress-bar: how we can do progress during blocking rest-api call:
                dialog.addAttachListener(evn -> {
                    //Can we dispatch in delayed by 1 Sec:
                    //Must dispatch using scheduler on UI thread:
                    AppQueue.dispatch(1
                            , TimeUnit.SECONDS
                            , () -> ui.access(() -> dialog.close()));
                });
                //Open Dialog:
                dialog.open();
            });
            //
            Button editButton = new Button("", VaadinIcon.EDIT.create());
            editButton.addClickListener(e -> {
                //TODO: Popup Edit FormView:
                Dialog dialog = new Dialog();
                dialog.getElement().setAttribute("aria-label", "Edit Trends!");
                dialog.addDetachListener((dlgCloseEvent) -> {
                    //Now reload GridView using fetchTask and countTask in sequence Or otherwise:
                    UI ui = dlgCloseEvent.getSource().getUI().orElse(null);
                    this.gridView.dispatchAsyncLoad(ui);
                });
                //Config user-form
                TrendsForm form = new TrendsForm(entity, dialog);
                dialog.add(form);
                dialog.open();
            });
            //
            Button delButton = new Button("", VaadinIcon.CLOSE.create());
            delButton.addClickListener(e -> {
                //Popup Delete Confirmation Window:
                Dialog dialog = new Dialog();
                dialog.getElement().setAttribute("aria-label", "Delete Trends!");
                dialog.addDetachListener((closeEvn) -> {
                    //Now reload fetchTask and countTask in sequence:
                    UI ui = closeEvn.getSource().getUI().orElse(null);
                    this.gridView.dispatchAsyncLoad(ui);
                });
                //Config delete-confirmation:
                ConfirmDeleteAction confirm = new ConfirmDeleteAction(dialog
                        , new Span("Are you sure about deleting entity: " + entity.getTitle()));
                confirm.setDeleteButtonDisableOnClick(true);
                confirm.addOnDeleteAction((event) -> {
                    UI ui = event.getSource().getUI().orElse(null);
                    //TODO: implement delete action:
                    //Can we dispatch in delayed by 1 Sec:
                    //Must dispatch using scheduler on UI thread:
                    System.out.println("Delete: " + entity.getId());
                    AppQueue.dispatch(1
                            , TimeUnit.SECONDS
                            , () -> ui.access(() -> dialog.close()));
                });
                dialog.add(confirm);
                dialog.open();
            });
            //
            HorizontalLayout layout = new HorizontalLayout();
            layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
            layout.add(recycleButton, editButton, delButton);
            return layout;
        };
    }

    private ValueProvider<Entity, MenuBar> createContextMenuBar() {
        return (entity) -> {
            MenuBar menuBar = new MenuBar();
            menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);
            MenuItem menuItem = menuBar.addItem("•••");
            menuItem.getElement().setAttribute("aria-label", "More options");
            //Prepare per-row context-menu:
            ContextMenu contextMenu = new ContextMenu(menuItem);
            contextMenu.setOpenOnClick(true);
            contextMenu.addItem("Edit", event -> {
                //TODO: Popup Edit FormView:
                UI ui = event.getSource().getUI().orElse(null);
                AppQueue.dispatchTask(new DisplayAsyncNotification(ui, "Edit: " + entity.getTitle()));
            });
            contextMenu.addItem("Delete", event -> {
                //TODO: Popup Delete Confirmation Window:
                UI ui = event.getSource().getUI().orElse(null);
                AppQueue.dispatchTask(new DisplayAsyncNotification(ui, "Delete: " + entity.getTitle()));
            });
            //
            return menuBar;
        };
    }

    //Custom GridContextMenu:
    private static class TrendContextMenu extends GridContextMenu<Trend> {
        public TrendContextMenu(Grid<Trend> target) {
            super(target);

            addItem("Edit"
                    , e -> e.getItem().ifPresent(entity -> {
                //TODO: Popup Edit FormView:
                UI ui = e.getSource().getUI().orElse(null);
                AppQueue.dispatchTask(new DisplayAsyncNotification(ui, "Edit: " + entity.getTitle()));
            }));
            addItem("Delete"
                    , e -> e.getItem().ifPresent(entity -> {
                //TODO: Popup Delete Confirmation Window:
                UI ui = e.getSource().getUI().orElse(null);
                AppQueue.dispatchTask(new DisplayAsyncNotification(ui, "Delete: " + entity.getTitle()));
            }));

            add(new Hr());

            GridMenuItem<Trend> emailItem = addItem("Email",
                    e -> e.getItem().ifPresent(trend -> {
                        //TODO: Popup Send Email Window:
                        UI ui = e.getSource().getUI().orElse(null);
                        AppQueue.dispatchTask(new DisplayAsyncNotification(ui, "Send to: " + trend.getEmail()));
                    }));
            GridMenuItem<Trend> phoneItem = addItem("Call",
                    e -> e.getItem().ifPresent(trend -> {
                        //TODO: Popup Call Window:
                        UI ui = e.getSource().getUI().orElse(null);
                        AppQueue.dispatchTask(new DisplayAsyncNotification(ui, "Call: " + trend.getPhone()));
                    }));

            setDynamicContentHandler(entity -> {
                // Do not show context menu when header is clicked
                if (entity == null) return false;
                emailItem.setText(String.format("Email: %s", entity.getEmail()));
                phoneItem.setText(String.format("Call: %s", entity.getPhone()));
                return true;
            });
        }
    }
    //End GridContextMenu
}