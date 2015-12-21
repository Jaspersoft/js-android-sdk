/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile for Android.
 *
 * TIBCO Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.test.integration.api;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.Client;
import com.jaspersoft.android.sdk.network.HttpException;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class LazyClient {
    private final JrsMetadata mJrsMetadata;
    private AuthorizedClient authorizedClient;

    public LazyClient(JrsMetadata jrsMetadata) {
        mJrsMetadata = jrsMetadata;
    }

    public AuthorizedClient get() {
        if (authorizedClient == null) {
            Client client = Client.newBuilder()
                    .withBaseUrl(mJrsMetadata.getServerUrl())
                    .create();
            authorizedClient = client.makeAuthorizedClient(mJrsMetadata.getCredentials())
                    .create();
            try {
                authorizedClient.connect();
            } catch (IOException | HttpException e) {
                throw new RuntimeException(e);
            }
        }
        return authorizedClient;
    }
}
