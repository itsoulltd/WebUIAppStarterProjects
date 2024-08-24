package com.infoworks.lab.components.ui;

import com.infoworks.lab.components.component.ConfirmDeleteAction;
import com.infoworks.lab.components.component.IndeterminateDialog;
import com.infoworks.lab.components.presenters.Forms.TrendsForm;
import com.infoworks.lab.components.presenters.GridView.GridView;
import com.infoworks.lab.domain.beans.queues.EventQueue;
import com.infoworks.lab.domain.beans.tasks.DisplayAsyncNotification;
import com.infoworks.lab.domain.entities.Trend;
import com.infoworks.lab.domain.repository.TrendRepository;
import com.infoworks.lab.layouts.RootAppLayout;
import com.infoworks.lab.layouts.RoutePath;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
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
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Route;

import java.util.concurrent.TimeUnit;

@Route(value = RoutePath.TRENDS_VIEW, layout = RootAppLayout.class)
public class TrendsView extends Composite<Div> {

    private GridView gridView;

    public TrendsView() {
        getContent().add(new Span("Trends"));
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
        gridView.dispatchAsyncLoad(UI.getCurrent().getUI().orElse(null));
    }

    private GridView createGridView() {
        GridView<Trend> gridView = new GridView<>(Trend.class
                , 10
                , new TrendRepository()
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

    private TemplateRenderer<Trend> createTemplateRenderer() {
        return TemplateRenderer.<Trend>of(
                        "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                                + "<vaadin-avatar img=\"[[item.pictureUrl]]\" name=\"[[item.title]]\" alt=\"User avatar\"></vaadin-avatar>"
                                + "  <vaadin-vertical-layout style=\"line-height: var(--lumo-line-height-m);\">"
                                + "    <span> [[item.title]] </span>"
                                + "    <span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">"
                                + "      [[item.subtitle]]" + "    </span>"
                                + "  </vaadin-vertical-layout>"
                                + "</vaadin-horizontal-layout>")
                .withProperty("pictureUrl", Trend::getPictureUrl)
                .withProperty("title", Trend::getTitle)
                .withProperty("subtitle", Trend::getSubtitle);
    }

    private ComponentRenderer<Span, Trend> createStatusComponentRenderer() {
        return new ComponentRenderer<>(Span::new, (span, user) -> {
            span.setText(user.isEnabled() ? "Active" : "Inactive");
            String theme = String
                    .format("badge %s", user.isEnabled() ? "success" : "error");
            span.getElement().getThemeList().add(theme);
            //span.getElement().setAttribute("theme", theme);
        });
    }

    private ValueProvider<Trend, HorizontalLayout> createActionBar() {
        return (trend) -> {
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
                    EventQueue.dispatch(1
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
                TrendsForm form = new TrendsForm(trend, dialog);
                dialog.add(form);
                dialog.open();
            });
            //
            Button delButton = new Button("", VaadinIcon.CLOSE.create());
            delButton.addClickListener(e -> {
                //Popup Delete Confirmation Window:
                Dialog dialog = new Dialog();
                dialog.getElement().setAttribute("aria-label", "Delete Trend!");
                dialog.addDetachListener((closeEvn) -> {
                    //Now reload fetchTask and countTask in sequence:
                    UI ui = closeEvn.getSource().getUI().orElse(null);
                    this.gridView.dispatchAsyncLoad(ui);
                });
                //Config delete-confirmation:
                ConfirmDeleteAction confirm = new ConfirmDeleteAction(dialog
                        , new Span("Are you sure about deleting trend: " + trend.getTitle()));
                confirm.setDeleteButtonDisableOnClick(true);
                confirm.addOnDeleteAction((event) -> {
                    UI ui = event.getSource().getUI().orElse(null);
                    //TODO: implement delete action:
                    //Can we dispatch in delayed by 1 Sec:
                    //Must dispatch using scheduler on UI thread:
                    System.out.println("Delete: " + trend.getId());
                    EventQueue.dispatch(1
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

    private ValueProvider<Trend, MenuBar> createContextMenuBar() {
        return (trend) -> {
            MenuBar menuBar = new MenuBar();
            menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);
            MenuItem menuItem = menuBar.addItem("•••");
            menuItem.getElement().setAttribute("aria-label", "More options");
            return menuBar;
        };
    }

    //Custom GridContextMenu:
    private static class TrendContextMenu extends GridContextMenu<Trend> {
        public TrendContextMenu(Grid<Trend> target) {
            super(target);

            addItem("Edit"
                    , e -> e.getItem().ifPresent(trend -> {
                //TODO: Popup Edit FormView:
                UI ui = e.getSource().getUI().orElse(null);
                EventQueue.dispatchTask(new DisplayAsyncNotification(ui, "Edit: " + trend.getTitle()));
            }));
            addItem("Delete"
                    , e -> e.getItem().ifPresent(trend -> {
                //TODO: Popup Delete Confirmation Window:
                UI ui = e.getSource().getUI().orElse(null);
                EventQueue.dispatchTask(new DisplayAsyncNotification(ui, "Delete: " + trend.getTitle()));
            }));

            add(new Hr());

            GridMenuItem<Trend> emailItem = addItem("Email",
                    e -> e.getItem().ifPresent(trend -> {
                        //TODO: Popup Send Email Window:
                        UI ui = e.getSource().getUI().orElse(null);
                        EventQueue.dispatchTask(new DisplayAsyncNotification(ui, "Send to: " + trend.getEmail()));
                    }));
            GridMenuItem<Trend> phoneItem = addItem("Call",
                    e -> e.getItem().ifPresent(trend -> {
                        //TODO: Popup Call Window:
                        UI ui = e.getSource().getUI().orElse(null);
                        EventQueue.dispatchTask(new DisplayAsyncNotification(ui, "Call: " + trend.getPhone()));
                    }));

            setDynamicContentHandler(trend -> {
                // Do not show context menu when header is clicked
                if (trend == null) return false;
                emailItem.setText(String.format("Email: %s", trend.getEmail()));
                phoneItem.setText(String.format("Call: %s", trend.getPhone()));
                return true;
            });
        }
    }
    //End GridContextMenu
}