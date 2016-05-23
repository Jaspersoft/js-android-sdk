/*
 * Copyright (C) 2016 TIBCO Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/mobile-sdk-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile SDK for Android.
 *
 * TIBCO Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile SDK for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.network.entity.type;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.jaspersoft.android.sdk.network.entity.dashboard.DashboardComponentCollection;
import com.jaspersoft.android.sdk.network.entity.dashboard.InputControlDashboardComponent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.3
 */
final class DashboardComponentTypeAdapterFactory implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (type.getRawType() == DashboardComponentCollection.class) {
            return (TypeAdapter<T>) customizeMyClassAdapter(gson, (TypeToken<DashboardComponentCollection>) type);
        }
        return null;
    }

    private TypeAdapter<DashboardComponentCollection> customizeMyClassAdapter(final Gson gson, TypeToken<DashboardComponentCollection> type) {
        final TypeAdapter<DashboardComponentCollection> delegate = gson.getDelegateAdapter(this, type);
        final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
        return new TypeAdapter<DashboardComponentCollection>() {
            @Override
            public void write(JsonWriter out, DashboardComponentCollection value) throws IOException {
                delegate.toJsonTree(value);
            }

            @Override
            public DashboardComponentCollection read(JsonReader in) throws IOException {
                JsonElement tree = elementAdapter.read(in);
                return adaptComponent(tree, gson);
            }
        };
    }

    private DashboardComponentCollection adaptComponent(JsonElement tree, Gson gson) {
        JsonArray array = tree.getAsJsonArray();
        List<InputControlDashboardComponent> components = new ArrayList<>(array.size());

        for (int i = 0, size = array.size(); i < size; i++) {
            JsonObject object = array.get(i).getAsJsonObject();
            InputControlDashboardComponent component = getIcsComponent(gson, object);
            if (component != null) {
                components.add(component);
            }
        }

        return new DashboardComponentCollection(components);
    }

    private InputControlDashboardComponent getIcsComponent(Gson gson, JsonObject object) {
        String type = object.get("type").getAsString();
        if ("inputControl".equals(type)) {
            return gson.fromJson(object, InputControlDashboardComponent.class);
        }
        return null;
    }
}
