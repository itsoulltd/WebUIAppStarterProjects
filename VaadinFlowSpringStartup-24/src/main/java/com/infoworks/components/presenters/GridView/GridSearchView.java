package com.infoworks.components.presenters.GridView;

import com.infoworks.orm.Property;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

public class GridSearchView extends HorizontalLayout {

    public static final String CLEAR_BUTTON_TITLE = "Clear";
    public static final String SEARCH_BUTTON_TITLE = "Search";
    public static final String ADD_NEW_ITEM_BUTTON_TITLE = "Add New!";
    private TextField searchField;
    private Button searchButton;
    private Button addButton;
    private final Property searchProperty = new Property("AnyKey", null);
    private GridView parent;
    private SearchEvent eventDelegate;
    private AddNewItemEventListener addNewItemEventListener;
    private boolean hideSearchBar = false;
    private boolean hideAddNewButton = true;

    public GridSearchView(GridView parent) {
        this.parent = parent;
        if (SearchEvent.class.isAssignableFrom(parent.getClass())) {
            this.eventDelegate = parent;
        }
        //Config SearchBar:
        searchField = new TextField();
        searchField.setLabel("");
        searchField.setPlaceholder("Search By Any...");
        searchField.setPrefixComponent(new Icon("lumo", "search"));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);
        //Action on value-changed event on Search Field:
        searchField.addValueChangeListener(((event) -> {
            if (searchProperty != null) {
                //System.out.println("Search With: " + event.getValue());
                searchProperty.setValue(event.getValue());
            }
            alterButton(searchButton, SEARCH_BUTTON_TITLE, new Icon("lumo", "search"));
        }));
        //
        searchButton = new Button(SEARCH_BUTTON_TITLE, new Icon("lumo", "search"));
        searchButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        searchButton.addClickShortcut(Key.ENTER);
        //Action on searchButton:
        searchButton.addClickListener((event) -> {
            if (event.getSource().getText().equalsIgnoreCase(CLEAR_BUTTON_TITLE)) {
                //Clear the grid & related buttons:
                searchField.clear();
                //Reload current page in Grid:
                if (eventDelegate != null) {
                    eventDelegate.onClearEvent(event);
                }
            } else {
                if (searchProperty != null) {
                    if (searchProperty.getValue() == null) {
                        searchProperty.setValue(searchField.getValue());
                    }
                    //Do Search action:
                    if (eventDelegate != null) {
                        eventDelegate.onSearchEvent(event, searchProperty);
                    }
                    alterButton(event.getSource(), CLEAR_BUTTON_TITLE, new Icon("lumo", "cross"));
                }
            }
        });
        //We add the addButton to view when delegate will be set.
        addButton = new Button(ADD_NEW_ITEM_BUTTON_TITLE, new Icon("vaadin", "pencil"));
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickShortcut(Key.ADD);
        //Action on addNewItemButton:
        addButton.addClickListener(event -> {
            if (this.addNewItemEventListener != null)
                this.addNewItemEventListener.onAddNewItemEvent(event);
        });
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        if(!isHideSearchBar()) add(searchField);
        if(!isHideSearchBar()) add(searchButton);
        if(!isHideAddNewButton()) add(addButton);
    }

    public boolean isHideSearchBar() {
        return hideSearchBar;
    }

    public void setHideSearchBar(boolean hideSearchBar) {
        this.hideSearchBar = hideSearchBar;
    }

    public boolean isHideAddNewButton() {
        return hideAddNewButton;
    }

    public void setHideAddNewButton(boolean hideAddNewButton) {
        this.hideAddNewButton = hideAddNewButton;
    }

    private void alterButton(Button button, String title, Icon icon) {
        if (button.getText().equalsIgnoreCase(title))
            return;
        button.setText(title);
        button.setIcon(icon);
    }

    public void setSearchEvent(SearchEvent eventDelegate) {
        this.eventDelegate = eventDelegate;
    }

    public interface SearchEvent {
        void onSearchEvent(ClickEvent<Button> event, Property search);
        void onClearEvent(ClickEvent<Button> event);
    }

    public void addNewEventListener(AddNewItemEventListener eventListener) {
        if (this.addNewItemEventListener != null) return;
        //Setup AddNewItemButton:
        if (AddNewItemEventListener.class.isAssignableFrom(eventListener.getClass())) {
            this.addNewItemEventListener = eventListener;
        }
        this.hideAddNewButton = false;
    }

    @FunctionalInterface
    public interface AddNewItemEventListener {
        void onAddNewItemEvent(ClickEvent<Button> event);
    }

}
