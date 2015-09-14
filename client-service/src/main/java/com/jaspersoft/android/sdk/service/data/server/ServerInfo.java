/*
 * Copyright � 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of Jaspersoft Mobile for Android.
 *
 * Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.service.data.server;

import java.text.SimpleDateFormat;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class ServerInfo {
    private SimpleDateFormat dateFormatPattern;
    private SimpleDateFormat datetimeFormatPattern;
    private ServerVersion version;
    private ServerEdition edition;
    private String licenseType;
    private String build;
    private String editionName;
    private FeatureSet features;

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public SimpleDateFormat getDateFormatPattern() {
        return dateFormatPattern;
    }

    public void setDateFormatPattern(String dateFormatPattern) {
        this.dateFormatPattern = new SimpleDateFormat(dateFormatPattern);
    }

    public SimpleDateFormat getDatetimeFormatPattern() {
        return datetimeFormatPattern;
    }

    public void setDatetimeFormatPattern(String datetimeFormatPattern) {
        this.datetimeFormatPattern = new SimpleDateFormat(datetimeFormatPattern);
    }

    public ServerEdition getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = ServerEdition.valueOf(edition);
    }

    public String getEditionName() {
        return editionName;
    }

    public void setEditionName(String editionName) {
        this.editionName = editionName;
    }

    public FeatureSet getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = FeatureSet.parse(features);
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public ServerVersion getVersion() {
        return version;
    }

    public void setVersion(String version) {
        ServerVersion.Parser parser = ServerVersion.defaultParser();
        this.version = parser.parse(version);
    }
}
