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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jaspersoft.android.sdk.network.entity.schedule.JobSourceParamsWrapper;

import java.util.Map;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.3
 */
final class JobSourceParamsWrapperTypeAdapterFactory extends CustomizedTypeAdapterFactory<JobSourceParamsWrapper> {
    public JobSourceParamsWrapperTypeAdapterFactory() {
        super(JobSourceParamsWrapper.class);
    }


    @Override
    protected JsonElement afterRead(JsonElement element) {
        if (element.isJsonNull()) {
            return element;
        }

        JsonObject params = new JsonObject();
        JsonObject root = element.getAsJsonObject();
        JsonObject parameterValues = root.get("parameterValues").getAsJsonObject();

        Set<Map.Entry<String, JsonElement>> valuesSet = parameterValues.entrySet();
        for (Map.Entry<String, JsonElement> entry : valuesSet) {
            String paramKey = entry.getKey();
            JsonObject valuesWrapper = entry.getValue().getAsJsonObject();

            JsonArray values = valuesWrapper.get("item").getAsJsonArray();
            params.add(paramKey, values);
        }

        root.remove("parameterValues");
        root.add("parameterValues", params);

        return element;
    }
}
