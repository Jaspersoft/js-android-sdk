/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile for Android.
 *
 * TIBCO Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.network.entity.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionRequestOptions;

/**
 * @author Tom Koptel
 * @since 2.3
 */
final class ReportExecutionRequestOptionsFactory extends CustomizedTypeAdapterFactory<ReportExecutionRequestOptions> {
    public ReportExecutionRequestOptionsFactory() {
        super(ReportExecutionRequestOptions.class);
    }

    @Override
    protected void beforeWrite(ReportExecutionRequestOptions source, JsonElement toSerialize) {
        JsonObject response = toSerialize.getAsJsonObject();
        JsonElement parametersElement = response.get("parameters");
        if (!parametersElement.isJsonNull()) {
            JsonArray parameters = parametersElement.getAsJsonArray();

            JsonObject reportParameter = new JsonObject();
            reportParameter.add("reportParameter", parameters);

            response.remove("parameters");
            response.add("parameters", reportParameter);
        }

        super.beforeWrite(source, response);
    }
}
