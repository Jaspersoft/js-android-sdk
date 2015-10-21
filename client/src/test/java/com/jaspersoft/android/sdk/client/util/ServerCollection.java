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

package com.jaspersoft.android.sdk.client.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 1.10
 */
public class ServerCollection {
    private final List<Object[]> params = new ArrayList<>();
    private final Set<String> servers;
    private final String[] mDataTypes;

    private ServerCollection(String resourceName, String[] dataTypes) {
        String data = TestResource.getJson().rawData(resourceName);
        Type type = new TypeToken<Set<String>>() {
        }.getType();
        servers = new Gson().fromJson(data, type);
        mDataTypes = dataTypes;
    }

    public static ServerCollection newInstance(Class<?> targetClass) {
        Annotation[] annotations = targetClass.getAnnotations();
        String[] dataTypes = {"XML"};
        for (Annotation annotation : annotations) {
            if (annotation instanceof TargetDataType) {
                TargetDataType dataType = (TargetDataType) annotation;
                dataTypes = dataType.values();
            }
        }
        return new ServerCollection("servers_under_test", dataTypes);
    }

    public Collection<Object[]> load() {
        if (params.isEmpty()) {
            for (String serveUrl : servers) {
                createParams(readServerData(serveUrl));
            }
        }
        if (params.isEmpty()) {
            throw new IllegalStateException("Oops! It looks like you forgot to setup 'servers_under_test.json' resource");
        }
        return params;
    }

    private void createParams(Map<String, String> serverData) {
        String version = serverData.get("version");
        String url = serverData.get("url");
        for (String dataType : mDataTypes) {
            params.add(new Object[]{version, url, dataType});
        }
    }

    private Map<String, String> readServerData(String serveUrl) {
        try {
            URL url = new URL(serveUrl + "/rest_v2/serverInfo");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");

            connection.setUseCaches(true);
            connection.setDoOutput(true);

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if not Java 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();

            String json = response.toString();
            Type type = new TypeToken<Map<String, String>>() {
            }.getType();

            Map<String, String> serverData = new Gson().fromJson(json, type);
            serverData.put("url", serveUrl);
            serverData.put("version", normalizeServerVersion(serverData.get("version")));
            return serverData;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static String normalizeServerVersion(String version) {
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
        return String.valueOf(decimalVersion.doubleValue());
    }

}
