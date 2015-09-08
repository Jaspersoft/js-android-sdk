/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
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

package com.jaspersoft.android.sdk.test.integration.api.utils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class JrsMetadata {

    private final String organization;
    private final String serverUrl;
    private final String username;
    private final String password;

    private JrsMetadata(Builder builder) {
        this.organization = builder.organization;
        this.serverUrl = builder.serverUrl;
        this.username = builder.username;
        this.password = builder.password;
    }

    public static JrsMetadata createMobileDemo() {
        return builder()
                .setOrganization("")
                .setServerUrl("http://mobiledemo.jaspersoft.com/jasperserver-pro")
                .setUsername("superuser")
                .setPassword("superuser")
                .build();
    }

    public static JrsMetadata createMobileDemo2() {
        return builder()
                .setOrganization("organization_1")
                .setServerUrl("http://mobiledemo2.jaspersoft.com/jasperserver-pro")
                .setUsername("phoneuser")
                .setPassword("phoneuser")
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getOrganization() {
        return organization;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "JrsMetadata{" +
                ", organization='" + organization + '\'' +
                ", serverUrl='" + serverUrl + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public static class Builder {
        private String organization;
        private String serverUrl;
        private String username;
        private String password;


        public Builder setOrganization(String organization) {
            this.organization = organization;
            return this;
        }

        public Builder setServerUrl(String serverUrl) {
            this.serverUrl = serverUrl;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPassword(String pass) {
            this.password = pass;
            return this;
        }

        public JrsMetadata build() {
            checkValues();
            return new JrsMetadata(this);
        }

        private void checkValues() {
            assertPropertyNotNull(organization, "organization");
            assertPropertyNotEmpty(serverUrl, "serverUrl");
            assertPropertyNotEmpty(username, "username");
            assertPropertyNotEmpty(password, "password");
            try {
                new URL(serverUrl);
            } catch (MalformedURLException e) {
                throw new IllegalStateException(e);
            }
        }

        private void assertPropertyNotNull(String property, String propertyName) {
            if (property == null) {
                throw new IllegalStateException(
                        propertyName + " invalid should not be: " + String.valueOf(property));
            }
        }

        private void assertPropertyNotEmpty(String property, String propertyName) {
            if (isEmpty(property)) {
                throw new IllegalStateException(
                        propertyName + " invalid should not be: " + String.valueOf(property));
            }
        }

        private static boolean isEmpty(String str) {
            if (str == null || str.trim().length() == 0)
                return true;
            else
                return false;
        }
    }
}
