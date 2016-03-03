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

package com.jaspersoft.android.sdk.service.info;

import com.jaspersoft.android.sdk.network.AnonymousClient;
import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.network.ServerRestApi;
import com.jaspersoft.android.sdk.network.entity.server.ServerInfoData;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.DefaultExceptionMapper;
import com.jaspersoft.android.sdk.service.internal.Preconditions;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ServerInfoService {
    private final ServerRestApi mRestApi;
    private final ServerInfoTransformer mTransformer;
    private final ServiceExceptionMapper mServiceExceptionMapper;

    @TestOnly
    ServerInfoService(ServerRestApi restApi,
                      ServerInfoTransformer transformer,
                      ServiceExceptionMapper serviceExceptionMapper) {
        mRestApi = restApi;
        mTransformer = transformer;
        mServiceExceptionMapper = serviceExceptionMapper;
    }

    @NotNull
    public ServerInfo requestServerInfo() throws ServiceException {
        try {
            ServerInfoData response = mRestApi.requestServerInfo();
            return mTransformer.transform(response);
        } catch (HttpException e) {
            throw mServiceExceptionMapper.transform(e);
        } catch (IOException e) {
            throw mServiceExceptionMapper.transform(e);
        }
    }

    @NotNull
    public static ServerInfoService newService(@NotNull AnonymousClient client) {
        Preconditions.checkNotNull(client, "Client should not be null");
        ServiceExceptionMapper serviceExceptionMapper = DefaultExceptionMapper.getInstance();
        return new ServerInfoService(client.infoApi(), ServerInfoTransformer.get(), serviceExceptionMapper);
    }
}
