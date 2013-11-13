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

package com.jaspersoft.android.sdk.client.oxm.server;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * @author Ivan Gadzhega
 * @since 1.4
 */
@Root(strict=false)
public class ServerInfo {

    public static class VERSION_CODES {
        public static final int UNKNOWN = 0;
        public static final int EMERALD = 50000;
        public static final int EMERALD_TWO = 50500;
    }

    public static class EDITIONS {
        public static final String CE = "CE";
        public static final String PRO = "PRO";
    }

    @Element(required=false)
    private String build;

    @Element
    private String edition;

    @Element(required=false)
    private String editionName;

    @Element(required=false)
    private String expiration;

    @Element(required=false)
    private String features;

    @Element(required=false)
    private String licenseType;

    private String version;

    private int versionCode;


    public ServerInfo() {
        edition = EDITIONS.CE;
        version = String.valueOf(VERSION_CODES.UNKNOWN);
    }

    @Element
    public void setVersion(String version) {
        this.version = version;
        this.versionCode = 0;
        // update version code
        if (version != null) {
            String[] subs = version.split("\\.");
            for (int i = 0; i < subs.length; i++) {
                int exponent = ((subs.length - 1) - i) * 2;
                versionCode += Integer.parseInt(subs[i]) * Math.pow(10, exponent);
            }
        }
    }

    @Element
    public String getVersion() {
        return version;
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

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

}
