package com.infoworks.lab.components.presenters.MapView;

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

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.Optional;

@SuppressWarnings("serial")
public abstract class GoogleMapsView extends VerticalLayout {

    public GoogleMapsView() {
        this.setSizeFull();
        //Load from VM Options: -Dgoogle.maps.api=<api-key>
        //OR from docker-environment: - google.maps.api: <api-key>
        //IF-None-Of-These-Are-There-Then pass = null;
        String apiKey = Optional.ofNullable(System.getProperty("google.maps.api") != null
                ? System.getProperty("google.maps.api")
                : System.getenv("google.maps.api")).orElse(null);
        if (apiKey == null) {
            add(new H2("Api key is needed to run the demo, " +
                    "pass it using the following system property: '-Dgoogle.maps.api=<your-api-key>'"));
        } else {
            System.out.println("GoogleMap-API-Key: " + apiKey);
            initGoogleMapsView(apiKey);
        }
    }

    protected abstract void initGoogleMapsView(String apiKey);
}
