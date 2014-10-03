package com.vaadin.demo.dashboard.component;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.vaadin.demo.dashboard.domain.Movie;
import com.vaadin.demo.dashboard.event.DashboardEvent.CloseOpenWindowsEvent;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class MovieDetailsWindow extends Window {

    private final Label synopsis = new Label();

    public MovieDetailsWindow(Movie movie, Date startTime, Date endTime) {
        addStyleName("moviedetailswindow");
        Responsive.makeResponsive(this);

        setCaption(movie.getTitle());
        center();
        setCloseShortcut(KeyCode.ESCAPE, null);
        // setDraggable(false);
        setResizable(false);
        setClosable(false);
        // setWidth(90.0f, Unit.PERCENTAGE);
        setHeight(90.0f, Unit.PERCENTAGE);

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        setContent(content);

        Panel detailsWrapper = new Panel(buildMovieDetails(movie, startTime,
                endTime));
        detailsWrapper.setSizeFull();
        detailsWrapper.addStyleName(ValoTheme.PANEL_BORDERLESS);
        detailsWrapper.addStyleName("scroll-divider");
        content.addComponent(detailsWrapper);
        content.setExpandRatio(detailsWrapper, 1f);

        content.addComponent(buildFooter());
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);

        Button ok = new Button("Close");
        ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
        ok.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                close();
            }
        });
        ok.focus();
        footer.addComponent(ok);
        footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);
        return footer;
    }

    private Component buildMovieDetails(Movie movie, Date startTime,
            Date endTime) {
        HorizontalLayout details = new HorizontalLayout();
        details.setWidth(100.0f, Unit.PERCENTAGE);
        details.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        details.setMargin(true);
        details.setSpacing(true);

        final Image coverImage = new Image(null, new ExternalResource(
                movie.getPosterUrl()));
        coverImage.addStyleName("cover");
        details.addComponent(coverImage);

        Component detailsForm = buildDetailsForm(movie, startTime, endTime);
        details.addComponent(detailsForm);
        details.setExpandRatio(detailsForm, 1);

        return details;
    }

    private Component buildDetailsForm(Movie movie, Date startTime, Date endTime) {
        FormLayout fields = new FormLayout();
        fields.setSpacing(true);
        fields.setMargin(true);

        Label label;
        SimpleDateFormat df = new SimpleDateFormat();
        if (startTime != null) {
            df.applyPattern("dd-mm-yyyy");
            label = new Label(df.format(startTime));
            label.setSizeUndefined();
            label.setCaption("Date");
            fields.addComponent(label);

            df.applyPattern("hh:mm a");
            label = new Label(df.format(startTime));
            label.setSizeUndefined();
            label.setCaption("Starts");
            fields.addComponent(label);
        }

        if (endTime != null) {
            label = new Label(df.format(endTime));
            label.setSizeUndefined();
            label.setCaption("Ends");
            fields.addComponent(label);
        }

        label = new Label(movie.getDuration() + " minutes");
        label.setSizeUndefined();
        label.setCaption("Duration");
        fields.addComponent(label);

        synopsis.setData(movie.getSynopsis());
        synopsis.setCaption("Synopsis");
        updateSynopsis(movie, false);
        fields.addComponent(synopsis);

        final Button more = new Button("More…");
        more.addStyleName(ValoTheme.BUTTON_LINK);
        fields.addComponent(more);
        more.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                updateSynopsis(null, true);
                event.getButton().setVisible(false);
            }
        });

        return fields;
    }

    public void updateSynopsis(Movie m, boolean expand) {
        String synopsisText = synopsis.getData().toString();
        if (m != null) {
            synopsisText = m.getSynopsis();
            synopsis.setData(m.getSynopsis());
        }
        if (!expand) {
            synopsisText = synopsisText.length() > 350 ? synopsisText
                    .substring(0, 350) + "…" : synopsisText;

        }
        synopsis.setValue(synopsisText);
    }

    public static void open(Movie movie, Date startTime, Date endTime) {
        DashboardEventBus.post(new CloseOpenWindowsEvent());
        Window w = new MovieDetailsWindow(movie, startTime, endTime);
        UI.getCurrent().addWindow(w);
        w.focus();
    }
}
