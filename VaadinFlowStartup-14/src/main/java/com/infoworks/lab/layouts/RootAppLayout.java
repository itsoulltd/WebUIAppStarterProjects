package com.infoworks.lab.layouts;

import com.infoworks.lab.components.component.VImage;
import com.infoworks.lab.components.ui.GeoTrackerView;
import com.infoworks.lab.components.ui.UsersView;
import com.infoworks.lab.components.ui.ProfileView;
import com.infoworks.lab.components.ui.TrendsView;
import com.infoworks.lab.config.UserSessionManagement;
import com.infoworks.lab.domain.repository.AuthRepository;
import com.infoworks.lab.rest.models.Response;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import java.util.HashMap;
import java.util.Map;

@Push
@Theme(Lumo.class)
@CssImport(value = "./styles/view-styles.css", id = "view-styles")
@CssImport(value = "./styles/shared-styles.css", include = "view-styles")
@PWA(name = "Time Tracking", shortName = "Ticker")
@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes, viewport-fit=cover")
public class RootAppLayout extends AppLayout {

    private static final String LOGO_URL = "logo.png";
    private Map<Tab, Component> tab2Workspace = new HashMap<>();

    public RootAppLayout() {
        Image logo = VImage.loadFromImages(LOGO_URL, "Vaadin Logo");
        logo.setHeight("44px");
        addToNavbar(new DrawerToggle(), logo);

        final Tabs tabs = new Tabs(profile(), trends(), users(), trackerView(), logout());
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addSelectedChangeListener(event -> {
            final Tab selectedTab = event.getSelectedTab();
            final Component component = tab2Workspace.get(selectedTab);
            setContent(component);
        });
        addToDrawer(tabs);
    }

    private Tab logout() {
        final Button btn = new Button();
        btn.setText("Logout");
        btn.setSizeFull();
        btn.addClickListener(e -> {
            AuthRepository authRepo = new AuthRepository();
            String authToken = UI.getCurrent().getSession().getAttribute(AuthRepository.X_AUTH_TOKEN).toString();
            authRepo.doLogout(authToken, (isSuccess, msg) -> {
                if (isSuccess) {
                    UserSessionManagement.handleSessionExpireEvent(new Response().setStatus(401));
                }
            });
        });
        //
        final Tab tab = new Tab(btn);
        tab2Workspace.put(tab, new Label("Logout"));
        return tab;
    }

    private Tab profile() {
        final Tab  tab   = new Tab("Profile");
        tab2Workspace.put(tab, new ProfileView());
        return tab;
    }

    private Tab trends() {
        final Tab tab = new Tab("Trends View");
        tab2Workspace.put(tab, new TrendsView());
        return tab;
    }

    private Tab users() {
        final Tab  tab   = new Tab("Users");
        tab2Workspace.put(tab, new UsersView());
        return tab;
    }

    private Tab trackerView() {
        final Tab  tab   = new Tab("Geo Tracker");
        tab2Workspace.put(tab, new GeoTrackerView());
        return tab;
    }

}