/*
 * Copyright (C) 2015 TIBCO Jaspersoft Corporation. All rights reserved.
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

package com.jaspersoft.android.sdk.service.data.server;

import java.text.SimpleDateFormat;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class ServerInfo {
    private SimpleDateFormat dateFormatPattern;
    private SimpleDateFormat datetimeFormatPattern;
    private ServerVersion version;
    private String edition;
    private String licenseType;
    private String build;
    private String editionName;
    private Set<String> features;

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public SimpleDateFormat getDateFormatPattern() {
        return dateFormatPattern;
    }

    public void setDateFormatPattern(SimpleDateFormat dateFormatPattern) {
        this.dateFormatPattern = dateFormatPattern;
    }

    public SimpleDateFormat getDatetimeFormatPattern() {
        return datetimeFormatPattern;
    }

    public void setDatetimeFormatPattern(SimpleDateFormat datetimeFormatPattern) {
        this.datetimeFormatPattern = datetimeFormatPattern;
    }

    public boolean isEditionPro() {
        return "PRO".equals(edition);
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getEditionName() {
        return editionName;
    }

    public void setEditionName(String editionName) {
        this.editionName = editionName;
    }

    public Set<String> getFeatures() {
        return features;
    }

    public void setFeatures(Set<String> features) {
        this.features = features;
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

    public void setVersion(ServerVersion version) {
        this.version = version;
    }
}
