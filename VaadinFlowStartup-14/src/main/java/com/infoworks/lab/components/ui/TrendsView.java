package com.infoworks.lab.components.ui;

import com.infoworks.lab.components.presenters.GridView.GridView;
import com.infoworks.lab.domain.entities.Trend;
import com.infoworks.lab.domain.repository.TrendRepository;
import com.infoworks.lab.layouts.RootAppLayout;
import com.infoworks.lab.layouts.RoutePath;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.router.Route;

@Route(value = RoutePath.TRENDS_VIEW, layout = RootAppLayout.class)
public class TrendsView extends Composite<Div> {
    public TrendsView() {
        getContent().add(new Span("Trends"));
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if (getContent().getChildren().count() > 0){
            getContent().removeAll();
        }
        super.onAttach(attachEvent);
        //
        GridView<Trend> gridView = new GridView<>(Trend.class
                , 10
                , new TrendRepository()
                , "enabled");
        gridView.getGrid().addColumn(createTrendTemplateRenderer()).setHeader("Trend")
                .setAutoWidth(true).setFlexGrow(2);
        gridView.getGrid().addColumn(Trend::getId).setHeader("Serial")
                .setAutoWidth(true).setFlexGrow(1);
        gridView.getGrid().addColumn(createStatusComponentRenderer()).setHeader("Status")
                .setAutoWidth(true).setFlexGrow(1);
        gridView.getGrid().setAllRowsVisible(true); //For Dynamic Table Height.
        getContent().add(gridView);
        gridView.dispatchAsyncLoad(UI.getCurrent().getUI().orElse(null));
    }

    private TemplateRenderer<Trend> createTrendTemplateRenderer() {
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
}