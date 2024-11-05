package com.infoworks.lab.components.presenters.GridView;

import com.infoworks.lab.rest.models.ItemCount;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class GridFooter extends VerticalLayout {

    private static String TOTAL_COUNT_TITLE = "Total Count: %s";
    private long totalCount = 0;
    private final Label titleLabel;
    private final Button previous;
    private final Button next;
    private int page, pageSize, paginationCarry;
    private GridView parent;
    private ActionEvent eventDelegate;

    public GridFooter(GridView parent) {
        this.parent = parent;
        this.page = 0;
        this.pageSize = parent.getGrid().getPageSize();
        this.paginationCarry = this.pageSize;
        if (ActionEvent.class.isAssignableFrom(parent.getClass())) {
            this.eventDelegate = parent;
        }
        //Configure Next & Previous action:
        previous = new Button(new Icon(VaadinIcon.ARROW_LEFT), (event) -> {
            this.page--;
            enableButton(FooterButtonType.Next);
            this.paginationCarry -= this.pageSize;
            if (eventDelegate != null) {
                eventDelegate.onPreviousArrowClick(event, this.page, this.pageSize, this.totalCount);
            }
            if (this.page <= 0) {
                disableButton(FooterButtonType.Previous);
            }
        });
        next = new Button(new Icon(VaadinIcon.ARROW_RIGHT), (event) -> {
            this.page++;
            enableButton(FooterButtonType.Previous);
            this.paginationCarry += this.pageSize;
            if (eventDelegate != null) {
                eventDelegate.onNextArrowClick(event, this.page, this.pageSize, this.totalCount);
            }
            if (this.paginationCarry >= this.totalCount) {
                disableButton(FooterButtonType.Next);
            }
        });
        //
        HorizontalLayout arrowButtons = new HorizontalLayout(previous, next);
        arrowButtons.setAlignItems(Alignment.CENTER);
        disableButton(FooterButtonType.Next);
        disableButton(FooterButtonType.Previous);
        //
        titleLabel = new Label(String.format(TOTAL_COUNT_TITLE, totalCount));
        HorizontalLayout content = new HorizontalLayout(titleLabel, arrowButtons);
        content.setFlexGrow(18, titleLabel);
        content.setFlexGrow(2, arrowButtons);
        content.addClassNames("content");
        content.setPadding(false);
        content.setSpacing(true);
        content.setSizeFull();
        add(content);
    }

    public void updateTitleCount(ItemCount count) {
        if (count.getStatus() == 200) {
            totalCount = count.getCount();
            titleLabel.setText(String.format(TOTAL_COUNT_TITLE, totalCount));
            if (totalCount > 0 && totalCount > paginationCarry) {
                enableButton(FooterButtonType.Next);
            }
        } else {
            titleLabel.setText(count.getError());
        }
    }

    public void enableButton(FooterButtonType type) {
        if (type == FooterButtonType.Previous) {
            previous.setEnabled(true);
        }
        if (type == FooterButtonType.Next) {
            next.setEnabled(true);
        }
    }

    public void disableButton(FooterButtonType type) {
        if (type == FooterButtonType.Previous) {
            previous.setEnabled(false);
        }
        if (type == FooterButtonType.Next) {
            next.setEnabled(false);
        }
    }

    public enum FooterButtonType {
        Previous, Next
    }

    public void setEventDelegate(ActionEvent delegate) {
        this.eventDelegate = delegate;
    }

    public interface ActionEvent {
        void onNextArrowClick(ClickEvent<Button> event, int page, int pageSize, long totalCount);
        void onPreviousArrowClick(ClickEvent<Button> event, int page, int pageSize, long totalCount);
    }

    public void reset() {
        this.page = 0;
        this.pageSize = parent.getGrid().getPageSize();
        this.paginationCarry = this.pageSize;
        disableButton(FooterButtonType.Next);
        disableButton(FooterButtonType.Previous);
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getTotalCount() {
        return totalCount;
    }
}
