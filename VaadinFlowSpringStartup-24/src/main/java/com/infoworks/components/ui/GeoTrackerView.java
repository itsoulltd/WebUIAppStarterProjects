package com.infoworks.components.ui;

/*-
 * #%L
 * Google Maps Addon
 * %%
 * Copyright (C) 2020 - 2024 Flowing Code
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ch.hsr.geohash.GeoHash;
import ch.hsr.geohash.WGS84Point;
import com.flowingcode.vaadin.addons.googlemaps.GoogleMap;
import com.flowingcode.vaadin.addons.googlemaps.GoogleMap.MapType;
import com.flowingcode.vaadin.addons.googlemaps.GoogleMapMarker;
import com.flowingcode.vaadin.addons.googlemaps.LatLon;
import com.flowingcode.vaadin.addons.googlemaps.Markers;
import com.infoworks.components.presenters.MapView.GoogleMapsView;
import com.infoworks.config.AppQueue;
import com.infoworks.domain.tasks.OStreetAddressSearch;
import com.infoworks.domain.models.OStreetGeocode;
import com.infoworks.applayouts.RootLayout;
import com.infoworks.applayouts.RoutePath;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@PageTitle("GeoLocation Tracker")
@Route(value = RoutePath.GEO_TRACKER_VIEW, layout = RootLayout.class)
@SuppressWarnings("serial")
public class GeoTrackerView extends GoogleMapsView {

    private Integer trackLocationId = null;

    @Override
    protected void initGoogleMapsView(String apiKey) {
        GoogleMap gmaps = new GoogleMap(apiKey, null, null);
        gmaps.setMapType(MapType.ROADMAP);
        gmaps.setSizeFull();
        gmaps.setZoom(15);

        //Location search  using OpenStreetMap-Api:
        OStreetAddressSearch searchTask = new OStreetAddressSearch(UI.getCurrent()
                , (gCodes) -> {
                    if (gCodes == null || gCodes.isEmpty()) return;
                    System.out.println("Search found: " + gCodes.size());
                    OStreetGeocode code = gCodes.get(0);
                    System.out.println(code.getDisplay_name());
                    updateMapCenter(gmaps, code.getGeoHash());
        });

        //Config Search:
        HorizontalLayout searchbar = new HorizontalLayout();
        searchbar.setWidthFull();
        searchbar.setPadding(true);
        TextField search = new TextField("", "Search...");
        search.setWidth("500px");
        Button searchBtn = new Button("Search", (e) -> {
            String searchTx = search.getValue();
            searchTask.setQuery(searchTx);
            UI ui = e.getSource().getUI().orElse(null);
            AppQueue.dispatch(120, TimeUnit.MILLISECONDS
                    , () -> ui.access(() -> searchTask.execute(null)));
        });
        searchbar.add(search, searchBtn);

        //Add search-bar and google-map:
        getContent().add(searchbar, gmaps);

        //Create button to activate location tracking
        Button startLocationTrackingButton =
                new Button("Start tracking my location", e -> gmaps.trackLocation());
        //Create button to stop location tracking
        Button stopLocationTrackingButton =
                new Button("Stop tracking my location", e -> gmaps.stopTrackLocation());

        //Add Buttons:
        HorizontalLayout buttonBox = new HorizontalLayout(startLocationTrackingButton, stopLocationTrackingButton);
        buttonBox.setWidthFull();
        buttonBox.setPadding(true);
        getContent().add(buttonBox);

        //Create marker to track location
        GoogleMapMarker locationMarker = createMapMarker("You're here");
        registerMarkerClickEvent(UI.getCurrent(), locationMarker, "What time is it?");
        gmaps.addMarker(locationMarker);

        //Add listener to obtain id when track location is activated
        gmaps.addLocationTrackingActivatedEventListener(ev -> {
            trackLocationId = ev.getSource().getTrackLocationId();
        });

        //Add listener to know when location was updated and update location marker position
        gmaps.addCurrentLocationEventListener(e -> {
            locationMarker.setPosition(e.getSource().getCenter());
        });

        //Add listener to capture geolocation error
        gmaps.addGeolocationErrorEventListener(e -> {
            Notification notification = new Notification();
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            if (!e.isBrowserHasGeolocationSupport())
                notification.show("Your browser doesn't support geolocation.");
            else
                notification.show("GeolocationErrorEventListener: " + e.toString());
        });
    }

    private GoogleMapMarker createMapMarker(String caption) {
        GoogleMapMarker locationMarker = new GoogleMapMarker();
        locationMarker.setCaption(caption);
        locationMarker.setDraggable(false);
        locationMarker.setIconUrl(pickMarkerIconUrl());
        return locationMarker;
    }

    private void registerMarkerClickEvent(UI ui, GoogleMapMarker marker, String title) {
        //Add click event:
        marker.addClickListener(e -> {
            System.out.println("Marker Clicked!");
            AppQueue.dispatch(100, TimeUnit.MILLISECONDS
                    , () -> ui.access(() -> {
                        //Update Current Time:
                        String now = DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalTime.now());
                        String html = String.format("%s <br> Now: %s", title, now);
                        marker.addInfoWindow(html);
                    }));
        });
        //
    }

    private String pickMarkerIconUrl() {
        List<String> markerColorsList = Arrays.asList(Markers.RED
                , Markers.PINK
                , Markers.BLUE
                , Markers.GREEN
                , Markers.PURPLE
                , Markers.YELLOW
                , Markers.ORANGE
                , Markers.LIGHTBLUE);
        //Choose randomly:
        Random random = new Random();
        int inx = random.nextInt(markerColorsList.size());
        String markerColor = markerColorsList.get(inx);
        return markerColor;
    }

    private void updateMapCenterIfNotSetYet(GoogleMap gmaps, String gHash, LatLon center) {
        if (gmaps.getCenter().getLat() == 0.0D) {
            if (gHash != null && !gHash.isEmpty()) {
                GeoHash geoHash = GeoHash.fromGeohashString(gHash);
                WGS84Point point = geoHash.getPoint();
                center = new LatLon(point.getLatitude(), point.getLongitude());
            }
            System.out.println("Lat:" + center.getLat() + "; Lon:" + center.getLon());
            gmaps.setCenter(center);
        }
    }

    private void updateMapCenter(GoogleMap gmaps, String gHash) {
        if (gHash != null && !gHash.isEmpty()) {
            GeoHash geoHash = GeoHash.fromGeohashString(gHash);
            WGS84Point point = geoHash.getPoint();
            LatLon center = new LatLon(point.getLatitude(), point.getLongitude());
            System.out.println("Lat:" + center.getLat() + "; Lon:" + center.getLon());
            gmaps.setCenter(center);
        }
    }
}
