package com.vaadin.demo.dashboard.view.sales;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;


import com.sun.javafx.charts.Legend;
import com.vaadin.demo.dashboard.DashboardUI;
import com.vaadin.demo.dashboard.domain.Movie;
import com.vaadin.demo.dashboard.domain.MovieRevenue;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import javafx.scene.chart.Chart;


@SuppressWarnings("serial")
public class SalesView extends VerticalLayout implements View {

    private ComboBox<Movie> movieSelect;
    private Collection<Movie> movies;

    public SalesView() {
        setSizeFull();
        addStyleName("sales");
        setMargin(false);
        setSpacing(false);

        addComponent(buildHeader());


        initMovieSelect();
        // Add first 4 by default
        List<Movie> subList = new ArrayList<Movie>(
                DashboardUI.getDataProvider().getMovies()).subList(0, 4);
        for (Movie m : subList) {
            addDataSet(m);
        }
    }

    private void initMovieSelect() {
        movies = new HashSet<>(DashboardUI.getDataProvider().getMovies());
        movieSelect.setItems(movies);
    }

    private Component buildHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        Responsive.makeResponsive(header);

        Label titleLabel = new Label("Revenue by Movie");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponents(titleLabel, buildToolbar());

        return header;
    }

    private Component buildToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.addStyleName("toolbar");

        movieSelect = new ComboBox<>();
        movieSelect.setItemCaptionGenerator(Movie::getTitle);
        movieSelect.addShortcutListener(
                new ShortcutListener("Add", KeyCode.ENTER, null) {
                    @Override
                    public void handleAction(final Object sender,
                            final Object target) {
                        addDataSet(movieSelect.getValue());
                    }
                });

        final Button add = new Button("Add");
        add.setEnabled(false);
        add.addStyleName(ValoTheme.BUTTON_PRIMARY);

        CssLayout group = new CssLayout(movieSelect, add);
        group.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        toolbar.addComponent(group);

        movieSelect.addSelectionListener(
                event -> add.setEnabled(event.getValue() != null));

        final Button clear = new Button("Clear");
        clear.addStyleName("clearbutton");
        clear.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                initMovieSelect();
                clear.setEnabled(false);
            }
        });
        toolbar.addComponent(clear);

        add.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                addDataSet(movieSelect.getValue());
                clear.setEnabled(true);
            }
        });

        return toolbar;
    }



    private void addDataSet(final Movie movie) {
        movies.remove(movie);
        movieSelect.setValue(null);
        movieSelect.getDataProvider().refreshAll();

        Collection<MovieRevenue> revenues = DashboardUI.getDataProvider()
                .getDailyRevenuesByMovie(movie.getId());


    }

    @Override
    public void enter(final ViewChangeEvent event) {
    }
}
