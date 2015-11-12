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

package com.jaspersoft.android.sdk.util;


import com.jaspersoft.android.sdk.util.rest.token.Credentials;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class IntegrationEnv {
    private static final String PROPERTIES_FILE = "integration_env.properties";
    private final Properties config;

    private IntegrationEnv(Properties properties) {
        config = properties;
    }

    public static IntegrationEnv load() {
        Properties properties = new Properties();
        try {
            properties.load(TestResource.create(PROPERTIES_FILE).asStream());
            return new IntegrationEnv(properties);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String[] getServers() {
        String serverProp = config.getProperty("test.servers");
        String[] servers = serverProp.split(",");
        if (servers.length == 0) {
            return new String[] {serverProp};
        }
        return servers;
    }

    public Credentials[] getCredentials() {
        String serverProp = config.getProperty("test.credentials");
        String[] credentials = serverProp.split(",");
        if (credentials.length == 0) {
            credentials = new String[] {serverProp};
        }

        Credentials[] result = new Credentials[credentials.length];
        for (int i = 0; i < credentials.length; i++) {
            result[i] = Credentials.Factory.create(credentials[i]);
        }

        return result;
    }

    @Override
    public String toString() {
        return "IntegrationEnv{" +
                "config=" + config +
                '}';
    }
}
