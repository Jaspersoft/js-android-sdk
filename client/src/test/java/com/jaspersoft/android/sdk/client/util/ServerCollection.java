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

package com.jaspersoft.android.sdk.client.util;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 1.10
 */
public class ServerCollection {
    private final List<Object[]> params = new ArrayList<>();
    private final ServerJsonModel[] servers;

    private ServerCollection() {
        String data = TestResource.getJson().rawData("servers_under_test");
        servers = new Gson().fromJson(data, ServerJsonModel[].class);
    }

    public static ServerCollection newInstance() {
        return new ServerCollection();
    }

    public Collection<Object[]> getAll() {
        return create5_5().create5_6().create5_6_1().create6_0().create6_0_1().create6_1().get();
    }

    public ServerCollection create5_5() {
        loadParamsForServerVersion("5.5");
        return this;
    }

    public ServerCollection create5_6() {
        loadParamsForServerVersion("5.6");
        return this;
    }

    public ServerCollection create5_6_1() {
        loadParamsForServerVersion("5.6.1");
        return this;
    }

    public ServerCollection create6_0() {
        loadParamsForServerVersion("6.0");
        return this;
    }

    public ServerCollection create6_0_1() {
        loadParamsForServerVersion("6.0.1");
        return this;
    }

    public ServerCollection create6_1() {
        loadParamsForServerVersion("6.1");
        return this;
    }

    public Collection<Object[]> get() {
        if (params.isEmpty()) {
            throw new IllegalStateException("Oops! It looks like you forgot to setup data set 'servers_under_test.json'");
        }
        return params;
    }

    private ServerCollection loadParamsForServerVersion(String version) {
        Set<String> serverUrls = getServerUrls(version);
        for (String serverUrl : serverUrls) {
            params.add(new Object[]{version, serverUrl, "XML"});
            params.add(new Object[]{version, serverUrl, "JSON"});
        }
        return this;
    }

    private Set<String> getServerUrls(String version) {
        for (ServerJsonModel model : servers) {
            if (model.getVersion().equals(version)) {
                return model.getServers();
            }
        }
        throw new IllegalStateException("No server urls found for version: " + version);
    }

    private static class ServerJsonModel {
        private String version;
        private Set<String> servers;

        public Set<String> getServers() {
            return servers;
        }

        public String getVersion() {
            return version;
        }
    }
}
