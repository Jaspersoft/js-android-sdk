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


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jaspersoft.android.sdk.util.rest.dto.Server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Properties;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class IntegrationEnv {
    private static final String PROPERTIES_FILE = "test-env.properties";
    private static final String SERVERS_CONFIG_FILE = "servers-local.json";
    private final Properties config;
    private final Gson mGson;

    private IntegrationEnv(Properties properties) {
        config = properties;
        mGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
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

    public List<Server> getServers() {
        TestResource rawConfig = TestResource.create(SERVERS_CONFIG_FILE);
        Reader reader = new InputStreamReader(rawConfig.asStream());
        return mGson.fromJson(reader, new TypeToken<List<Server>>(){}.getType());
    }

    public int reportExecNumber() {
        return Integer.parseInt(config.getProperty("run.report.count"));
    }

    @Override
    public String toString() {
        return "IntegrationEnv{" +
                "config=" + config +
                '}';
    }
}
