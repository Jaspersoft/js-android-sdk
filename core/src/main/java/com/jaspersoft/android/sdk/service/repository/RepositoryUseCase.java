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

package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.network.RepositoryRestApi;
import com.jaspersoft.android.sdk.network.entity.export.OutputResource;
import com.jaspersoft.android.sdk.network.entity.resource.ResourceLookup;
import com.jaspersoft.android.sdk.service.data.report.ResourceOutput;
import com.jaspersoft.android.sdk.service.data.repository.Resource;
import com.jaspersoft.android.sdk.service.data.repository.ResourceType;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;
import com.jaspersoft.android.sdk.service.internal.info.InfoCacheManager;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class RepositoryUseCase {
    private final ServiceExceptionMapper mServiceExceptionMapper;
    private final RepositoryRestApi mRepositoryRestApi;
    private final ResourcesMapper mResourcesMapper;
    private final InfoCacheManager mInfoCacheManager;

    RepositoryUseCase(ServiceExceptionMapper serviceExceptionMapper,
                      RepositoryRestApi repositoryRestApi,
                      ResourcesMapper resourcesMapper,
                      InfoCacheManager infoCacheManager) {
        mServiceExceptionMapper = serviceExceptionMapper;
        mRepositoryRestApi = repositoryRestApi;
        mResourcesMapper = resourcesMapper;
        mInfoCacheManager = infoCacheManager;
    }

    public Resource getResourceByType(String resourceUri, ResourceType type) throws ServiceException {
        try {
            ServerInfo info = mInfoCacheManager.getInfo();
            SimpleDateFormat datetimeFormatPattern = info.getDatetimeFormatPattern();
            ResourceLookup reportLookup = mRepositoryRestApi.requestResource(resourceUri, type.name());
            return mResourcesMapper.toConcreteResource(reportLookup, datetimeFormatPattern, type);
        } catch (HttpException e) {
            throw mServiceExceptionMapper.transform(e);
        } catch (IOException e) {
            throw mServiceExceptionMapper.transform(e);
        }
    }

    public ResourceOutput getResourceContent(String resourceUri) throws ServiceException  {
        try {
            final OutputResource resource = mRepositoryRestApi.requestResourceOutput(resourceUri);
            return new ResourceOutput() {
                @Override
                public InputStream getStream() throws IOException {
                    return resource.getStream();
                }
            };
        } catch (HttpException e) {
            throw mServiceExceptionMapper.transform(e);
        } catch (IOException e) {
            throw mServiceExceptionMapper.transform(e);
        }
    }
}
