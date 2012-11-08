/*
 * Copyright (C) 2005 - 2012 Jaspersoft Corporation. All rights reserved.
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

package com.jaspersoft.android.sdk.client.oxm.server;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * @author Ivan Gadzhega
 * @version $Id$
 * @since 1.4
 */
@Root
public class ServerInfo {

    public static class VERSION_CODES {
        public static int UNKNOWN = 0;
        public static int EMERALD = 50000;
    }

    @Element
    private String build;

    @Element
    private String edition;

    @Element
    private String editionName;

    @Element(required=false)
    private String expiration;

    @Element
    private String features;

    @Element
    private String licenseType;

    @Element
    private String version;


    public int getVersionCode() {
        int versionCode = VERSION_CODES.UNKNOWN;
        if (version != null) {
            String[] subs = version.split("\\.");
            for (int i = 0; i < subs.length; i++) {
                int exponent = ((subs.length - 1) - i) * 2;
                versionCode += Integer.parseInt(subs[i]) * Math.pow(10, exponent);
            }
        }
        return versionCode;
    }

    //---------------------------------------------------------------------
    // Getters & Setters
    //---------------------------------------------------------------------

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
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

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
