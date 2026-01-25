package com.infoworks.components.component;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.tabs.Tabs;

import java.util.HashMap;
import java.util.Map;

public class TabView extends Composite<Div> {

    private TabVariant[] themeVariants = { TabVariant.LUMO_ICON_ON_TOP };
    private final Map<Tab, Component> tab2Contents = new HashMap<>();
    private Tab[] orderedTabs;

    public TabView() {}

    public TabView(Tab[] tabs, Component[] components) {
        setTab2Contents(tabs, components);
    }

    public TabView(Map<Tab, Component> tab2Contents) {
        setTab2Contents(tab2Contents.keySet().toArray(new Tab[0])
                , tab2Contents.values().toArray(new Component[0]));
    }

    public Tab[] getTabs() {
        return orderedTabs;
    }

    public void setTab2Contents(Tab[] tabs, Component[] components) {
        if (tabs.length != components.length)
            throw new RuntimeException("TabView: Inconsistent Argument Exceptions. Tab[] tabs and Component[] components length are not equal!");
        this.orderedTabs = tabs;
        int index = 0;
        for (Tab tab : orderedTabs) {
            this.tab2Contents.put(tab, components[index++]);
        }
    }

    public void setThemeVariants(TabVariant[] themeVariants) {
        this.themeVariants = themeVariants;
    }

    public TabVariant[] getThemeVariants() {
        return themeVariants;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if (getContent().getChildren().count() > 0){
            getContent().removeAll();
        }
        super.onAttach(attachEvent);

        //TabContentView:-
        final Div tabContentLayout = new Div();
        tabContentLayout.setSizeFull();

        //Tabs:-
        Tab[] tabItems = getTabs();

        //Set the icon on top:-
        for (Tab tab : tabItems) {
            tab.addThemeVariants(getThemeVariants());
        }

        //Tabs config:-
        Tabs tabs = new Tabs(tabItems);
        tabs.setWidthFull();
        tabs.addSelectedChangeListener(event -> {
            final Tab selectedTab = event.getSelectedTab();
            final Component component = tab2Contents.get(selectedTab);
            tabContentLayout.removeAll();
            tabContentLayout.add(component);
        });

        //Set default content:-
        if(tabItems.length > 0) tabContentLayout.add(tab2Contents.get(tabItems[0]));

        //Set init tab-content:-
        getContent().add(tabs, tabContentLayout);
    }
}
