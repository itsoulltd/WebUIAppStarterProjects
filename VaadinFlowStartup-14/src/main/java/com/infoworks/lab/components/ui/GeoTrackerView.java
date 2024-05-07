package com.infoworks.lab.components.ui;

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

import com.flowingcode.vaadin.addons.googlemaps.GoogleMap;
import com.flowingcode.vaadin.addons.googlemaps.GoogleMap.MapType;
import com.flowingcode.vaadin.addons.googlemaps.GoogleMapMarker;
import com.infoworks.lab.components.presenters.MapView.GoogleMapsView;
import com.infoworks.lab.layouts.RootAppLayout;
import com.infoworks.lab.layouts.RoutePath;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("GeoLocation Tracker")
@Route(value = RoutePath.GEO_TRACKER_VIEW, layout = RootAppLayout.class)
@SuppressWarnings("serial")
public class GeoTrackerView extends GoogleMapsView {

    private Integer trackLocationId = null;

    @Override
    protected void initGoogleMapsView(String apiKey) {
        GoogleMap gmaps = new GoogleMap(apiKey, null, null);
        gmaps.setMapType(MapType.ROADMAP);
        gmaps.setSizeFull();
        gmaps.setZoom(15);
        add(gmaps);

        // create button to activate location tracking
        Button startLocationTrackingButton =
                new Button("Start tracking my location", e -> gmaps.trackLocation());
        // create button to stop location tracking
        Button stopLocationTrackingButton =
                new Button("Stop tracking my location", e -> gmaps.stopTrackLocation(trackLocationId));
        add(startLocationTrackingButton, stopLocationTrackingButton);

        // create marker to track location
        GoogleMapMarker locationMarker = new GoogleMapMarker();
        locationMarker.setCaption("You're here");
        locationMarker.setDraggable(false);
        gmaps.addMarker(locationMarker);

        // add listener to obtain id when track location is activated
        gmaps.addLocationTrackingActivatedEventListener(ev -> {
            trackLocationId = ev.getTrackLocationId();
        });

        // add listener to know when location was updated and update location marker position
        gmaps.addCurrentLocationEventListener(e -> {
            locationMarker.setPosition(e.getSource().getCenter());
        });

        // add listener to capture geolocation error
        gmaps.addGeolocationErrorEventListener(e -> {
            if (!e.isBrowserHasGeolocationSupport())
                Notification.show("Your browser doesn't support geolocation.");
        });
    }
}
