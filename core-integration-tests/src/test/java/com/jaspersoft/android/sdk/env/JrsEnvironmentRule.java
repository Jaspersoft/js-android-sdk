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

import com.jaspersoft.android.sdk.testkit.dto.AuthConfig;
import com.jaspersoft.android.sdk.testkit.dto.Server;
import com.jaspersoft.android.sdk.testkit.exception.HttpException;
import com.jaspersoft.android.sdk.testkit.ServiceFactory;
import com.jaspersoft.android.sdk.testkit.token.Authorizer;
import com.jaspersoft.android.sdk.testkit.token.Credentials;
import com.jaspersoft.android.sdk.testkit.token.CredentialsMapper;

import org.junit.rules.ExternalResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public final class JrsEnvironmentRule extends ExternalResource {
    private IntegrationEnv mIntegrationEnv;

    public Object[] listServers() {
        List<Server> servers = getLazyEnv().getServers();
        Object[] result = new Object[servers.size()];

        for (int i = 0; i < result.length; i++) {
            result[i] = new Object[]{servers.get(i).getUrl()};
        }

        return result;
    }

    public Object[] listAuthorizedServers() {
        List<Server> servers = getLazyEnv().getServers();
        List<Object> result = new ArrayList<>();

        for (Server server : servers) {
            for (AuthConfig config : server.getAuthConfigs()) {
                Credentials credentials = CredentialsMapper.transform(config);
                try {
                    String token = Authorizer.create(server.getUrl()).authorize(credentials);
                    result.add(new Object[]{new ServerBundle(token, server.getUrl())});
                } catch (IOException | HttpException e) {
                    System.out.println(e);
                }
            }
        }

        return toArray(result);
    }

    public Object[] listReports() {
        List<Object> result = new ArrayList<>();
        for (Object o : listAuthorizedServers()) {
            Object[] pair = (Object[]) o;
            try {
                ServerBundle bundle = (ServerBundle) pair[0];
                String token = bundle.getToken();
                String url = bundle.getUrl();
                ServiceFactory service = ServiceFactory.builder()
                        .baseUrl(url).token(token).create();

                List<String> reports = service
                        .resourceRepository(getLazyEnv().reportExecNumber(), "reportUnit")
                        .listResources();
                for (String report : reports) {
                    result.add(new Object[] {bundle, report});
                }
            } catch (IOException | HttpException e) {
                System.out.println(e);
            }
        }
        return toArray(result);
    }

    private static Object[] toArray(List<Object> result) {
        Object[] resultArray = new Object[result.size()];
        result.toArray(resultArray);
        return resultArray;
    }

    private IntegrationEnv getLazyEnv() {
        if (mIntegrationEnv == null) {
            mIntegrationEnv = IntegrationEnv.load();
        }
        return mIntegrationEnv;
    }
}
