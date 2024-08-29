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
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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
        //TopBar Layout:
        Component topBar = createTopBar();
        //Menu Tabs:
        final Tabs tabs = getTabs();
        //AppLayout buildup:
        addToDrawer(tabs);
        addToNavbar(topBar);
    }

    private Component createTopBar() {
        //Hamburger Menu:
        DrawerToggle hamburgerMenu = new DrawerToggle();
        //Left-Side Layout:
        H1 title = new H1(ApplicationProperties.APP_DISPLAY_NAME);
        title.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");
        Image logo = VImage.loadFromImages(LOGO_URL, ApplicationProperties.APP_DISPLAY_NAME);
        logo.setWidth("74px");
        logo.setHeight("36px");
        //
        HorizontalLayout layout = new HorizontalLayout();
        layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        layout.setWidthFull();
        layout.setHeightFull();
        layout.getStyle().set("border-bottom", "1px solid #c5c5c5");
        layout.add(hamburgerMenu, title, logo);
        return layout;
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
            if(component != null) setContent(component);
        });
        return tabs;
    }

    private Icon createTabIcon(VaadinIcon viewIcon) {
        Icon icon = viewIcon.create();
        icon.getStyle().set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin-inline-start", "var(--lumo-space-xs)")
                .set("padding", "var(--lumo-space-xs)");
        return icon;
    }

    private Tab createTab(VaadinIcon viewIcon, String viewName, Class<? extends Component> viewClass) {
        //Design tab-icon:
        Icon icon = createTabIcon(viewIcon);
        //RouteLinks:
        RouterLink link = new RouterLink();
        link.add(icon, new Span(viewName));
        if(viewClass != null) link.setRoute(viewClass);
        link.setTabIndex(-1);
        return new Tab(link);
    }

    private Tab logout() {
        final Button btn = new Button(RoutePath.LOGOUT_VIEW, createTabIcon(VaadinIcon.SIGN_OUT));
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
        String viewName = RoutePath.menuName(RoutePath.PROFILE_VIEW);
        final Tab tab = createTab(VaadinIcon.USER_STAR, viewName, ProfileView.class);
        return tab;
    }

    private Tab users() {
        String viewName = RoutePath.menuName(RoutePath.USERS_CRUD_VIEW);
        final Tab tab = createTab(VaadinIcon.USERS, viewName, UsersView.class);
        return tab;
    }

    private Tab trends() {
        String viewName = RoutePath.menuName(RoutePath.TRENDS_VIEW);
        final Tab tab = createTab(VaadinIcon.LINE_BAR_CHART, viewName, TrendsView.class);
        return tab;
    }

    private Tab trackerView() {
        String viewName = RoutePath.menuName(RoutePath.GEO_TRACKER_VIEW);
        //CAUTION: To reuse content, we have to use tab2Workspace storage.
        // at the same time please do not pass viewClass in createTab(),
        // this will create duplicate content.
        final Tab tab = createTab(VaadinIcon.MAP_MARKER, viewName, null);
        tab2Workspace.put(tab, new GeoTrackerView());
        return tab;
    }

}