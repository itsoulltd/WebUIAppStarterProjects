package com.infoworks.applayouts;

import com.infoworks.components.component.VImage;
import com.infoworks.config.ApplicationProperties;
import com.infoworks.config.UserSessionManagement;
import com.infoworks.domain.entities.User;
import com.infoworks.domain.repositories.AuthRepository;
import com.infoworks.components.ui.ProfileView;
import com.infoworks.objects.Response;
import com.infoworks.orm.Property;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;

import java.util.HashMap;
import java.util.Map;

//@Push
//@PWA(name = "Time Tracking", shortName = "Ticker")
public class RootLayout extends AppLayout {

    private static final String LOGO_URL = "logo.png";
    private Map<Tab, Component> tab2Workspace = new HashMap<>();

    public RootLayout() {
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
        HorizontalLayout left = new HorizontalLayout();
        left.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        left.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        left.setWidthFull();
        left.setHeightFull();
        left.add(hamburgerMenu, title);
        //Right-Side Layout:
        MenuBar userInfoBar = new MenuBar();
        MenuItem item = userInfoBar.addItem(prepareUserInfo());
        SubMenu subMenu = item.getSubMenu();
        subMenu.addItem(new Span(createTabIcon(VaadinIcon.INFO_CIRCLE), new Anchor(
                "/pages/about.html"
                , "About"
                , AnchorTarget.BLANK
        )));
        subMenu.addItem(new Span(createTabIcon(VaadinIcon.SITEMAP), new Anchor(
                "/pages/help.html"
                , "Help"
                , AnchorTarget.BLANK
        )));
        subMenu.addItem(new Span(createTabIcon(VaadinIcon.PACKAGE), new Span(ApplicationProperties.APP_DISPLAY_VERSION)));
        subMenu.add(new Hr());
        subMenu.addItem(new Span(createTabIcon(VaadinIcon.EXIT), new Span("Logout")), onLogoutClickEvent());
        HorizontalLayout right = new HorizontalLayout();
        right.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        right.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        right.setWidthFull();
        right.setHeightFull();
        right.add(userInfoBar);
        //
        HorizontalLayout layout = new HorizontalLayout();
        layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.EVENLY);
        layout.setWidthFull();
        layout.setHeightFull();
        layout.getStyle().set("border-bottom", "1px solid #c5c5c5");
        layout.add(left, right);
        return layout;
    }

    private Component prepareUserInfo() {
        User principle = AuthRepository.currentPrincipleFromToken(UI.getCurrent(), new Property("username"));
        Icon icon = VaadinIcon.USER.create();
        icon.getStyle().set("margin-left", "5px");
        String username = principle.getName();
        Span component = new Span(new Span(username), icon);
        return component;
    }

    private Tabs getTabs() {
        Tabs tabs = new Tabs(
                profile()
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
        btn.addClickListener(onLogoutClickEvent());
        //
        final Tab tab = new Tab(btn);
        tab2Workspace.put(tab, new Label(RoutePath.LOGOUT_VIEW));
        return tab;
    }

    private ComponentEventListener onLogoutClickEvent() {
        return (event) -> {
            AuthRepository authRepo = new AuthRepository();
            String authToken = AuthRepository.parseToken(event.getSource().getUI().orElse(null));
            authRepo.doLogout(authToken, (isSuccess, msg) -> {
                if (isSuccess) {
                    UserSessionManagement.handleSessionExpireEvent(new Response().setStatus(401));
                }
            });
        };
    }

    private Tab profile() {
        String viewName = RoutePath.menuName(RoutePath.PROFILE_VIEW);
        final Tab tab = createTab(VaadinIcon.USER_STAR, viewName, ProfileView.class);
        return tab;
    }

    /*private Tab trends() {
        String viewName = RoutePath.menuName(RoutePath.TRENDS_VIEW);
        final Tab tab = createTab(VaadinIcon.LINE_BAR_CHART, viewName, TrendsView.class);
        return tab;
    }

    private Tab trendsCrud() {
        String viewName = RoutePath.menuName(RoutePath.TRENDS_CRUD_VIEW);
        final Tab tab = createTab(VaadinIcon.LINE_CHART, viewName, TrendsCrudView.class);
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
    }*/

}