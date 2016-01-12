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
import com.jaspersoft.android.sdk.network.entity.resource.ReportLookup;
import com.jaspersoft.android.sdk.service.data.report.ReportResource;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;
import com.jaspersoft.android.sdk.service.internal.info.InfoCacheManager;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @author Tom Koptel
 * @since 2.0
 */
class RepositoryUseCase {
    private final ServiceExceptionMapper mServiceExceptionMapper;
    private final RepositoryRestApi mRepositoryRestApi;
    private final ReportResourceMapper mReportResourceMapper;
    private final InfoCacheManager mInfoCacheManager;

    RepositoryUseCase(ServiceExceptionMapper serviceExceptionMapper,
                      RepositoryRestApi repositoryRestApi,
                      ReportResourceMapper reportResourceMapper,
                      InfoCacheManager infoCacheManager) {
        mServiceExceptionMapper = serviceExceptionMapper;
        mRepositoryRestApi = repositoryRestApi;
        mReportResourceMapper = reportResourceMapper;
        mInfoCacheManager = infoCacheManager;
    }

    public ReportResource getReportDetails(String reportUri) throws ServiceException {
        try {
            ServerInfo info = mInfoCacheManager.getInfo();
            SimpleDateFormat datetimeFormatPattern = info.getDatetimeFormatPattern();
            ReportLookup reportLookup = mRepositoryRestApi.requestReportResource(reportUri);
            return mReportResourceMapper.transform(reportLookup, datetimeFormatPattern);
        } catch (HttpException e) {
            throw mServiceExceptionMapper.transform(e);
        } catch (IOException e) {
            throw mServiceExceptionMapper.transform(e);
        }
    }
}
