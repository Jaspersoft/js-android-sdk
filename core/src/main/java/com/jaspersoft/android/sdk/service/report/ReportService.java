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
import com.jaspersoft.android.sdk.service.internal.info.InfoCacheManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

/**
 * The entry point that allows to start report execution.
 * {@link ReportExecution} API allows to perform report export on later stages.
 *
 * <pre>
 * {@code
 *
 *  Server server = Server.builder()
 *          .withBaseUrl("http://mobiledemo2.jaspersoft.com/jasperserver-pro/")
 *          .build();
 *
 *  Credentials credentials = SpringCredentials.builder()
 *          .withPassword("phoneuser")
 *          .withUsername("phoneuser")
 *          .withOrganization("organization_1")
 *          .build();
 *
 *
 *  AuthorizedClient client = server.newClient(credentials)
 *          .create();
 *
 *  ReportService reportService = ReportService.newService(client);
 *
 *  String reportUri = "/my/report/uri";
 *
 *  ReportExecutionOptions options = ReportExecutionOptions.builder()
 *          .withFormat(ReportFormat.HTML)
 *          .withFreshData(true)
 *          .withMarkupType(ReportMarkup.EMBEDDABLE)
 *          .withInteractive(false)
 *          .build();
 *
 *  try {
 *      ReportExecution execution = reportService.run(reportUri, options);
 *
 *      // waits for report completion
 *      ReportMetadata reporExecutionDetails = execution.waitForReportCompletion();
 *
 *      List<ReportParameter> newParameters = Collections.singletonList(
 *              new ReportParameter("key", Collections.singleton("value"))
 *      );
 *      ReportExecution newExecution = execution.updateExecution(newParameters);
 *
 *      ReportExportOptions exportOptions = ReportExportOptions.builder()
 *              .withFormat(ReportFormat.PDF)
 *              .withPageRange(PageRange.parse("1-100"))
 *              .build();
 *      ReportExport export = newExecution.export(exportOptions);
 *
 *      ReportExportOutput content = export.download();
 *      List<ReportAttachment> attachments = export.getAttachments();
 *      for (ReportAttachment attachment : attachments) {
 *          ResourceOutput attachmentContent = attachment.download();
 *      }
 *  } catch (ServiceException e) {
 *      // handle error API
 *  }
 * }
 * </pre>
 *
 * @author Tom Koptel
 * @since 2.3
 */
public abstract class ReportService {
    /**
     * Run report execution on the basis of passed options
     *
     * @param reportUri unique report uri
     * @param execOptions options that configure execution on JRS
     * @return object that encapsulates execution API
     * @throws ServiceException wraps both http/network/api related errors
     */
    @NotNull
    public abstract ReportExecution run(@NotNull String reportUri,
                                        @Nullable ReportExecutionOptions execOptions) throws ServiceException;

    /**
     * Factory method to create new service
     *
     * @param client authorized network client
     * @return instance of newly created service
     */
    @NotNull
    public static ReportService newService(@NotNull AuthorizedClient client) {
        Preconditions.checkNotNull(client, "Client should not be null");

        InfoCacheManager cacheManager = InfoCacheManager.create(client);
        ServiceExceptionMapper reportMapper = ReportExceptionMapper.getInstance();

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
