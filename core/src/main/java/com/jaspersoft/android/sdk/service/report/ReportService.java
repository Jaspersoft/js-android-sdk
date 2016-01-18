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

package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.DefaultExceptionMapper;
import com.jaspersoft.android.sdk.service.internal.Preconditions;
import com.jaspersoft.android.sdk.service.internal.ReportExceptionMapper;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;
import com.jaspersoft.android.sdk.service.internal.info.InMemoryInfoCache;
import com.jaspersoft.android.sdk.service.internal.info.InfoCacheManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public abstract class ReportService {
    @NotNull
    public abstract ReportExecution run(@NotNull String reportUri,
                                        @Nullable ReportExecutionOptions execOptions) throws ServiceException;

    @NotNull
    public static ReportService newService(@NotNull AuthorizedClient client) {
        Preconditions.checkNotNull(client, "Client should not be null");

        InfoCacheManager cacheManager = InfoCacheManager.create(client, new InMemoryInfoCache());
        ServiceExceptionMapper defaultMapper = new DefaultExceptionMapper();
        ServiceExceptionMapper reportMapper = new ReportExceptionMapper(defaultMapper);

        ReportServiceFactory reportServiceFactory = new ReportServiceFactory(cacheManager,
                client.reportExecutionApi(),
                client.reportExportApi(),
                reportMapper,
                client.getBaseUrl(),
                TimeUnit.SECONDS.toMillis(1)
        );

        return new ProxyReportService(reportServiceFactory);
    }
}
