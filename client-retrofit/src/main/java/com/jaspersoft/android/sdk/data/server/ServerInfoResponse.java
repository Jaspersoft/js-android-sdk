/*
 * Copyright (C) 2012-2013 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.jaspersoft.android.sdk.data.server;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class ServerInfoResponse {

    @Expose
    private String dateFormatPattern;

    @Expose
    private String datetimeFormatPattern;

    @Expose
    @JsonAdapter(ServerVersionAdapter.class)
    private ServerVersion version;

    @Expose
    private ServerEdition edition;

    @Expose
    private String licenseType;

    @Expose
    private String build;

    @Expose
    private String editionName;

    @Expose
    @JsonAdapter(FeaturesAdapter.class)
    private FeatureSet features;

    //---------------------------------------------------------------------
    // Getters
    //---------------------------------------------------------------------

    public String getBuild() {
        return build;
    }

    public String getDateFormatPattern() {
        return dateFormatPattern;
    }

    public String getDatetimeFormatPattern() {
        return datetimeFormatPattern;
    }

    public ServerEdition getEdition() {
        return edition;
    }

    public String getEditionName() {
        return editionName;
    }

    public FeatureSet getFeatureSet() {
        return features;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public ServerVersion getVersion() {
        return version;
    }
}
