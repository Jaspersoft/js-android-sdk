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

package com.jaspersoft.android.sdk.network;


import com.jaspersoft.android.sdk.network.entity.resource.FolderLookup;
import com.jaspersoft.android.sdk.network.entity.resource.ReportLookup;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceSearchResult;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Map;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public interface RepositoryRestApi {
    @NotNull
    ResourceSearchResult searchResources(@NotNull Cookies cookies,
                                         @Nullable Map<String, Object> searchParams) throws HttpException, IOException;

    @NotNull
    ReportLookup requestReportResource(@NotNull Cookies cookies,
                                       @NotNull String resourceUri) throws HttpException, IOException;

    @NotNull
    FolderLookup requestFolderResource(@NotNull Cookies cookies,
                                       @NotNull String resourceUri) throws HttpException, IOException;

    final class Builder extends GenericBuilder<Builder, RepositoryRestApi> {
        @Override
        RepositoryRestApi createApi() {
            return new RepositoryRestApiImpl(getAdapter().build());
        }
    }
}
