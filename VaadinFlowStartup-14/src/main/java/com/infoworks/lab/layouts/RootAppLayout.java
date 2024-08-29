package com.infoworks.lab.layouts;

import com.infoworks.lab.components.component.VImage;
import com.infoworks.lab.components.ui.GeoTrackerView;
import com.infoworks.lab.components.ui.ProfileView;
import com.infoworks.lab.components.ui.TrendsView;
import com.infoworks.lab.components.ui.UsersView;
import com.infoworks.lab.config.ApplicationProperties;
import com.infoworks.lab.config.UserSessionManagement;
import com.infoworks.lab.domain.repository.AuthRepository;
import com.infoworks.lab.rest.models.Response;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
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
        //Left-Side Layout:
        DrawerToggle hamburgerMenu = new DrawerToggle();
        H1 title = new H1(ApplicationProperties.APP_DISPLAY_NAME);
        title.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");
        Image logo = VImage.loadFromImages(LOGO_URL, ApplicationProperties.APP_DISPLAY_NAME);
        logo.setWidth("74px");
        logo.setHeight("36px");
        //Menu Tabs:
        final Tabs tabs = getTabs();
        //AppLayout buildup:
        addToDrawer(tabs);
        addToNavbar(hamburgerMenu, logo, title);
    }

    private Tabs getTabs() {
        Tabs tabs = new Tabs(
                profile()
                , users()
                , trends()
                , trackerView()
                , logout());
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addSelectedChangeListener(event -> {
            final Tab selectedTab = event.getSelectedTab();
            final Component component = tab2Workspace.get(selectedTab);
            setContent(component);
        });
        return tabs;
    }

    private Tab createTab(VaadinIcon viewIcon, String viewName, Class<? extends Component> viewClass) {
        Icon icon = viewIcon.create();
        icon.getStyle()
                .set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin-inline-start", "var(--lumo-space-xs)")
                .set("padding", "var(--lumo-space-xs)");
        //RouteLinks:
        RouterLink link = new RouterLink();
        link.add(icon, new Span(viewName));
        link.setRoute(viewClass);
        link.setTabIndex(-1);
        return new Tab(link);
    }

    private Tab logout() {
        final Button btn = new Button();
        btn.setText(RoutePath.LOGOUT_VIEW);
        btn.setSizeFull();
        btn.addClickListener(e -> {
            AuthRepository authRepo = new AuthRepository();
            String authToken = AuthRepository.parseToken(e.getSource().getUI().orElse(null));
            authRepo.doLogout(authToken, (isSuccess, msg) -> {
                if (isSuccess) {
                    UserSessionManagement.handleSessionExpireEvent(new Response().setStatus(401));
                }
            });
        });
        //
        final Tab tab = new Tab(btn);
        tab2Workspace.put(tab, new Label(RoutePath.LOGOUT_VIEW));
        return tab;
    }

    private Tab profile() {
        final Tab  tab   = new Tab(RoutePath.PROFILE_VIEW);
        tab2Workspace.put(tab, new ProfileView());
        return tab;
    }

    private Tab trends() {
        final Tab tab = new Tab(RoutePath.TRENDS_VIEW);
        tab2Workspace.put(tab, new TrendsView());
        return tab;
    }

    private Tab users() {
        final Tab  tab   = new Tab(RoutePath.USERS_CRUD_VIEW);
        tab2Workspace.put(tab, new UsersView());
        return tab;
    }

    private Tab trackerView() {
        final Tab  tab   = new Tab(RoutePath.GEO_TRACKER_VIEW);
        tab2Workspace.put(tab, new GeoTrackerView());
        return tab;
    }

}