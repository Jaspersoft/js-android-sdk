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

import com.jaspersoft.android.sdk.util.rest.exception.HttpException;
import com.jaspersoft.android.sdk.util.rest.token.Authorizer;
import com.jaspersoft.android.sdk.util.rest.token.Credentials;

import org.junit.rules.ExternalResource;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public final class JrsEnvironmentRule extends ExternalResource {
    private IntegrationEnv mIntegrationEnv;

    public Object[] listServers() {
        String[] servers = getLazyEnv().getServers();
        Object[] result = new Object[servers.length];
        for (int i = 0; i < servers.length; i++) {
            result[i] = new Object[]{servers[i]};
        }
        return result;
    }

    public Object[] listAuthorizedServers() {
        String[] servers = getLazyEnv().getServers();
        Credentials[] credentials = getLazyEnv().getCredentials();
        if (servers.length != credentials.length) {
            throw new IllegalStateException("Test environment configured improperly. Servers number should equal credentials");
        }

        Object[] result = new Object[servers.length];
        for (int i = 0; i < servers.length; i++) {
            try {
                String token = Authorizer.create(servers[i]).authorize(credentials[i]);
                result[i] = new Object[]{token, servers[i]};
            } catch (IOException | HttpException e) {
                throw new RuntimeException("Failed to configure token for server " + servers[i] + " abort test execution", e);
            }
        }
        return result;
    }

    private IntegrationEnv getLazyEnv() {
        if (mIntegrationEnv == null) {
            mIntegrationEnv = IntegrationEnv.load();
        }
        return mIntegrationEnv;
    }
}
