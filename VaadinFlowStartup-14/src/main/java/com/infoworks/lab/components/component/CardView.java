package com.infoworks.lab.components.component;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CardView extends VerticalLayout {
    //
    private String title;
    private Double currentVal;
    private Double previousVal;
    private Alignment[] borderAlignments = new Alignment[] {Alignment.END};
    //
    private H3 cardValue;
    private Span movementBadge;

    public CardView(String title, Double currentVal, Double previousVal) {
        this.title = title;
        this.currentVal = currentVal;
        this.previousVal = previousVal;
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        //Setup CardView:
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setPadding(true);
        setSpacing(true);
        setWidth("200px");
        setHeight("170px");
        //Css:
        //getStyle().set("background-color", "#e6f3ff");
        //getStyle().set("border", "1px solid #c5c5c5");
        //getStyle().set("border-radius", "var(--lumo-border-radius-m)"); //-s | -m | -l
        //Set Border Line:
        Stream.of(borderAlignments).forEach(alignment -> {
            if (alignment == Alignment.END)
                getStyle().set("border-right", "1px solid #c5c5c5");
            else if (alignment == Alignment.START)
                getStyle().set("border-left", "1px solid #c5c5c5");
            else if (alignment == Alignment.BASELINE)
                getStyle().set("border-bottom", "1px solid #c5c5c5");
            else
                getStyle().set("border-top", "1px solid #c5c5c5");
        });
        //Config visuals:
        Span cardTitle = new Span(title);
        cardTitle.setWidthFull();
        cardTitle.getStyle().set("border-bottom", "1px solid #c5c5c5");
        cardTitle.getStyle().set("text-align", "center");
        cardTitle.getStyle().set("padding", "var(--lumo-space-xs)");
        cardTitle.getStyle().set("box-sizing", "border-box");
        add(cardTitle);
        //Render card-view:
        List<Component> items = update(currentVal, previousVal);
        if (items.size() > 0)
            add(items.toArray(new Component[0]));
    }

    public List<Component> update(Double currentVal, Double previousVal) {
        List<Component> components = new ArrayList<>();
        //
        if(cardValue == null) cardValue = new H3(currentVal.toString());
        else cardValue.setText(currentVal.toString());
        components.add(0, cardValue);
        //
        Double movement = calculateMovement(currentVal, previousVal);
        //if (movement <= 0.0) return components; //TEST: We skip adding movement-badge or later.
        Icon moveIcon = movement > 0.0 ? createIcon(VaadinIcon.ARROW_UP) : createIcon(VaadinIcon.ARROW_DOWN);
        String moveVal = movement > 0.0
                ? String.format("+%.2f %s", movement, "%")
                : String.format("-%.2f %s", movement, "%");
        String badgeTheme = movement > 0.0 ? "badge success" : "badge error";
        if (movementBadge == null) {
            movementBadge = new Span(moveIcon, new Span(moveVal));
        } else {
            movementBadge.removeAll();
            movementBadge.add(moveIcon, new Span(moveVal));
        }
        movementBadge.setWidthFull();
        movementBadge.getElement().getThemeList().add(badgeTheme);
        movementBadge.getStyle().set("background-color", "#e6f3ff");
        movementBadge.getStyle().set("border", "1px solid #c5c5c5");
        movementBadge.getStyle().set("padding", "var(--lumo-space-xs)");
        movementBadge.getStyle().set("box-sizing", "border-box");
        movementBadge.getStyle().set("border-radius", "var(--lumo-border-radius-s)");
        movementBadge.getStyle().set("text-align", "center");
        if (movement <= 0.0) { //In this case we can dim the view.
            movementBadge.getStyle().set("opacity","0.5");
            movementBadge.getStyle().set("color","#e6f3ff");
        }
        components.add(1, movementBadge);
        //Finally return components:
        return components;
    }

    private double calculateMovement(double current, double previous) {
        if (previous <= 0.0) return 0.0;
        double val = (current * 100) / previous;
        return val;
    }

    private Icon createIcon(VaadinIcon vaadinIcon) {
        Icon icon = vaadinIcon.create();
        icon.getStyle()
                .set("padding", "var(--lumo-space-xs)")
                .set("box-sizing", "border-box");
        return icon;
    }

    public void setBorderAlignments(Alignment...alignments) {
        this.borderAlignments = alignments;
    }
}
