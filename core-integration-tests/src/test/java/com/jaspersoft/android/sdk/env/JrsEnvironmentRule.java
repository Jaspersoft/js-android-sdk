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

package com.jaspersoft.android.sdk.env;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.Server;
import com.jaspersoft.android.sdk.network.SpringCredentials;
import com.jaspersoft.android.sdk.testkit.ListResourcesUrisCommand;
import com.jaspersoft.android.sdk.testkit.LocalCookieManager;
import com.jaspersoft.android.sdk.testkit.TestKitClient;
import com.jaspersoft.android.sdk.testkit.dto.AuthConfig;
import com.jaspersoft.android.sdk.testkit.dto.SampleServer;
import com.jaspersoft.android.sdk.testkit.exception.HttpException;
import org.junit.rules.ExternalResource;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public final class JrsEnvironmentRule extends ExternalResource {
    private static final InetSocketAddress CHARLES_ADDRESS = new InetSocketAddress("0.0.0.0", 8888);
    private static final Proxy CHARLES = new Proxy(Proxy.Type.HTTP, CHARLES_ADDRESS);

    private IntegrationEnv mIntegrationEnv;

    public Object[] listClients() {
        List<SampleServer> sampleServers = getLazyEnv().getServers();
        List<ServerTestBundle> bundles = new ArrayList<>();

        for (SampleServer sampleServer : sampleServers) {
            Server.OptionalBuilder serverBuilder = Server.builder()
                    .withBaseUrl(sampleServer.getUrl());
            if (isProxyReachable()) {
                serverBuilder.withProxy(CHARLES);
            }
            Server server = serverBuilder.build();
            for (AuthConfig authConfig : sampleServer.getAuthConfigs()) {
                SpringCredentials credentials = SpringCredentials.builder()
                        .withOrganization(authConfig.getOrganization())
                        .withPassword(authConfig.getPassword())
                        .withUsername(authConfig.getPassword())
                        .build();
                AuthorizedClient client = server.newClient(credentials)
                        .withCookieHandler(LocalCookieManager.get())
                        .create();

                try {
                    client.authenticationApi().authenticate(credentials);
                } catch (Exception e) {
                    System.out.println(e);
                }

                bundles.add(new ServerTestBundle(credentials, client));
            }
        }

        Object[] result = new Object[bundles.size()];
        bundles.toArray(result);

        return result;
    }


    public Object[] listReports() {
        List<ReportTestBundle> bundle = new ArrayList<>();

        for (Object o : listClients()) {
            ServerTestBundle sererBundle = (ServerTestBundle) o;
            String baseUrl = sererBundle.getClient().getBaseUrl();
            TestKitClient kitClient = TestKitClient.newClient(baseUrl);

            int reportsNumber = getLazyEnv().reportExecNumber();
            ListResourcesUrisCommand listResourcesUris = new ListResourcesUrisCommand(reportsNumber, "reportUnit");

            try {
                List<String> uris = kitClient.getResourcesUris(listResourcesUris);

                for (String uri : uris) {
                    bundle.add(new ReportTestBundle(uri, sererBundle));
                }

            } catch (IOException | HttpException e) {
                System.out.println(e);
            }
        }

        Object[] result = new Object[bundle.size()];
        return bundle.toArray(result);
    }

    private IntegrationEnv getLazyEnv() {
        if (mIntegrationEnv == null) {
            mIntegrationEnv = IntegrationEnv.load();
        }
        return mIntegrationEnv;
    }

    private static boolean isProxyReachable() {
        try {
            Socket socket = new Socket();
            socket.connect(CHARLES_ADDRESS);
            socket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
