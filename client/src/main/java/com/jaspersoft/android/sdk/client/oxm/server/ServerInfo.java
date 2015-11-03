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

package com.jaspersoft.android.sdk.client.oxm.server;

import com.google.gson.annotations.Expose;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.math.BigDecimal;

/**
 * @author Ivan Gadzhega
 * @since 1.4
 */
@Root(strict=false)
public class ServerInfo {

    public static class VERSION_CODES {
        public static final int UNKNOWN = 0;
        public static final double EMERALD = 5.0;
        public static final double EMERALD_MR1 = 5.2;
        public static final double EMERALD_TWO = 5.5;
        public static final double EMERALD_THREE = 5.6;
        public static final double AMBER = 6.0;
    }

    public static class EDITIONS {
        public static final String CE = "CE";
        public static final String PRO = "PRO";
    }

    @Expose
    @Element(required=false)
    private String build;

    @Expose
    @Element
    private String edition;

    @Expose
    @Element(required=false)
    private String editionName;

    @Expose
    @Element(required=false)
    private String expiration;

    @Expose
    @Element(required=false)
    private String features;

    @Expose
    @Element(required=false)
    private String licenseType;

    private String version;

    private double versionCode;


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

            BigDecimal decimalSubVersion, decimalFactor, decimalResult;
            BigDecimal decimalVersion = new BigDecimal("0");
            for (int i = 0; i < subs.length; i++) {
                try {
                    decimalSubVersion = new BigDecimal(Integer.parseInt(subs[i]));
                } catch (NumberFormatException ex) {
                    decimalSubVersion = new BigDecimal("0");
                }

                decimalFactor = new BigDecimal(String.valueOf(Math.pow(10, i * -1)));
                decimalResult = decimalSubVersion.multiply(decimalFactor);
                decimalVersion = decimalVersion.add(decimalResult);
            }
            versionCode = decimalVersion.doubleValue();
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

    public double getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(double versionCode) {
        this.versionCode = versionCode;
    }

}
