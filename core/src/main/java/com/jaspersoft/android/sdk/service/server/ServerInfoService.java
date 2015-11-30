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

package com.jaspersoft.android.sdk.service.server;

import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.network.ServerRestApi;
import com.jaspersoft.android.sdk.network.entity.server.ServerInfoData;
import com.jaspersoft.android.sdk.service.RestClient;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;

import org.jetbrains.annotations.TestOnly;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ServerInfoService {
    private final ServerRestApi mRestApi;
    private final ServerInfoTransformer mTransformer;

    @TestOnly
    ServerInfoService(ServerRestApi restApi, ServerInfoTransformer transformer) {
        mRestApi = restApi;
        mTransformer = transformer;
    }

    public static ServerInfoService create(RestClient client) {
        ServerRestApi restApi = new ServerRestApi.Builder()
                .baseUrl(client.getServerUrl())
                .connectionTimeOut(client.getConnectionTimeOut(), TimeUnit.MILLISECONDS)
                .readTimeout(client.getReadTimeOut(), TimeUnit.MICROSECONDS)
                .build();

        return new ServerInfoService(restApi, ServerInfoTransformer.get());
    }

    public ServerInfo requestServerInfo() throws ServiceException {
        try {
            ServerInfoData response = mRestApi.requestServerInfo();
            return mTransformer.transform(response);
        } catch (HttpException e) {
            throw ServiceExceptionMapper.transform(e);
        } catch (IOException e) {
            throw ServiceExceptionMapper.transform(e);
        }
    }

    public double requestServerVersion() throws ServiceException {
        try {
            String version = mRestApi.requestVersion();
            return VersionParser.INSTANCE.toDouble(version);
        } catch (HttpException e) {
            throw ServiceExceptionMapper.transform(e);
        } catch (IOException e) {
            throw ServiceExceptionMapper.transform(e);
        }
    }

    public SimpleDateFormat requestServerDateTimeFormat() throws ServiceException {
        try {
            String dateTimeFormat = mRestApi.requestDateTimeFormatPattern();
            return new SimpleDateFormat(dateTimeFormat);
        } catch (HttpException e) {
            throw ServiceExceptionMapper.transform(e);
        } catch (IOException e) {
            throw ServiceExceptionMapper.transform(e);
        }
    }
}
