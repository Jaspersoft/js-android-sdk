/*
 * Copyright � 2015 TIBCO Software, Inc. All rights reserved.
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

package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.network.api.RepositoryRestApi;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceLookupResponse;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;

import java.util.Collection;

import rx.Observable;

/**
 * @author Tom Koptel
 * @since 2.0
 */
interface SearchStrategy {
    Observable<Collection<ResourceLookupResponse>> searchNext();
    boolean hasNext();

    class Factory {
        public static SearchStrategy get(String serverVersion,
                                         RepositoryRestApi.Factory repositoryApiFactory,
                                         SearchCriteria criteria) {
            ServerVersion version = ServerVersion.defaultParser().parse(serverVersion);
            if (version.getVersionCode() <= ServerVersion.EMERALD_MR2.getVersionCode()) {
                return new EmeraldMR2SearchStrategy_(repositoryApiFactory, criteria);
            }
            if (version.getVersionCode() >= ServerVersion.EMERALD_MR3.getVersionCode()) {
                return new EmeraldMR3SearchStrategy(repositoryApiFactory, criteria);
            }
            throw new UnsupportedOperationException("Could not resolve searchNext strategy for serverVersion: " + serverVersion);
        }
    }
}
