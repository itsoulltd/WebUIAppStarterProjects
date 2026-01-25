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

    private Map<Tab, Component> tab2Workspace = new HashMap<>();

    public TabView() {}

    public TabView(Map<Tab, Component> tab2Workspace) {
        this.tab2Workspace = tab2Workspace;
    }

    public Map<Tab, Component> getTab2Workspace() {
        return tab2Workspace;
    }

    public void setTab2Workspace(Map<Tab, Component> tab2Workspace) {
        this.tab2Workspace = tab2Workspace;
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
        Tab[] tabList = tab2Workspace.keySet().toArray(new Tab[0]);

        //Set the icon on top:-
        for (Tab tab : tabList) {
            tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
        }

        //Tabs config:-
        Tabs tabs = new Tabs(tabList);
        tabs.setWidthFull();
        tabs.addSelectedChangeListener(event -> {
            final Tab selectedTab = event.getSelectedTab();
            final Component component = tab2Workspace.get(selectedTab);
            tabContentLayout.removeAll();
            tabContentLayout.add(component);
        });

        //Set default content:-
        if(tabList.length > 0) tabContentLayout.add(tab2Workspace.get(tabList[0]));

        //Set init tab-content:-
        getContent().add(tabs, tabContentLayout);
    }
}
