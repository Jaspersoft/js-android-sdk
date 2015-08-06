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

package com.jaspersoft.android.sdk.network.rest.v2.entity.server;

import com.google.gson.annotations.Expose;

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
    private String version;

    @Expose
    private String edition;

    @Expose
    private String licenseType;

    @Expose
    private String build;

    @Expose
    private String editionName;

    @Expose
    private String features;

    public String getBuild() {
        return build;
    }

    public String getDateFormatPattern() {
        return dateFormatPattern;
    }

    public String getDatetimeFormatPattern() {
        return datetimeFormatPattern;
    }

    public String getEdition() {
        return edition;
    }

    public String getEditionName() {
        return editionName;
    }

    public String getFeatures() {
        return features;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "ServerInfoResponse{" +
                "build='" + build + '\'' +
                ", dateFormatPattern='" + dateFormatPattern + '\'' +
                ", datetimeFormatPattern='" + datetimeFormatPattern + '\'' +
                ", version='" + version + '\'' +
                ", edition='" + edition + '\'' +
                ", licenseType='" + licenseType + '\'' +
                ", editionName='" + editionName + '\'' +
                ", features='" + features + '\'' +
                '}';
    }
}
