package com.infoworks.components.presenters.MapView;

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

import com.infoworks.components.ui.BaseComposite;
import com.infoworks.config.ApplicationProperties;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;

@SuppressWarnings("serial")
public abstract class GoogleMapsView extends BaseComposite<Div> {

    public GoogleMapsView() {
        getContent().setWidthFull();
        getContent().setHeight(70, Unit.PERCENTAGE);
        //Load from VM Options: -Dgoogle.maps.api=<api-key>
        //OR from docker-environment: - google.maps.api: <api-key>
        //IF-None-Of-These-Are-There-Then pass = null;
        String apiKey = "AIzaSyDGeLyzQK2i2AZHpLg2LIn0PYb1qat-kbE"; //ApplicationProperties.GOOGLE_MAP_API_KEY;
        if (apiKey == null) {
            getContent().add(new H2("Api key is needed to run the demo, " +
                    "pass it using the following system property: '-Dgoogle.maps.api=<your-api-key>'"));
        } else {
            System.out.println("GoogleMap-API-Key: " + apiKey);
            initGoogleMapsView(apiKey);
        }
    }

    protected abstract void initGoogleMapsView(String apiKey);
}
