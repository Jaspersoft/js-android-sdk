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

package com.jaspersoft.android.sdk.service.rx.report;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.Preconditions;
import com.jaspersoft.android.sdk.service.report.ReportExecution;
import com.jaspersoft.android.sdk.service.report.ReportExecutionOptions;
import com.jaspersoft.android.sdk.service.report.ReportService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
import rx.Observable;
import rx.functions.Func0;

/**
 * The entry point that allows to start report execution.
 * {@link ReportExecution} API allows to perform report export on later stages.
 * All responses wrapped as Rx {@link rx.Observable}.
 *
 * <pre>
 * {@code
 *
 * Server server = Server.builder()
 *         .withBaseUrl("http://mobiledemo2.jaspersoft.com/jasperserver-pro/")
 *         .build();
 *
 * Credentials credentials = SpringCredentials.builder()
 *         .withPassword("phoneuser")
 *         .withUsername("phoneuser")
 *         .withOrganization("organization_1")
 *         .build();
 *
 *
 * AuthorizedClient client = server.newClient(credentials).create();
 * RxReportService service = RxReportService.newService(client);
 *
 * ReportExecutionOptions options = ReportExecutionOptions.builder()
 *         .withFormat(ReportFormat.HTML)
 *         .withFreshData(true)
 *         .withMarkupType(ReportMarkup.EMBEDDABLE)
 *         .withInteractive(false)
 *         .build();
 *
 * String reportUri = "/my/report/uri";
 * service.run(reportUri, options).flatMap(new Func1<RxReportExecution, Observable<RxReportExport>>() {
 *     &#064;
 *     public Observable<RxReportExport> call(RxReportExecution rxReportExecution) {
 *         ReportExportOptions exportOptions = ReportExportOptions.builder()
 *                 .withFormat(ReportFormat.PDF)
 *                 .withPageRange(PageRange.parse("1-100"))
 *                 .build();
 *         return rxReportExecution.export(exportOptions);
 *     }
 * }).flatMap(new Func1<RxReportExport, Observable<ReportExportOutput>>() {
 *     &#064;
 *     public Observable<ReportExportOutput> call(RxReportExport rxReportExport) {
 *         return rxReportExport.download();
 *     }
 * }).flatMap(new Func1<ReportExportOutput, Observable<Void>>() {
 *     &#064;
 *     public Observable<Void> call(ReportExportOutput output) {
 *         try {
 *             InputStream content = output.getStream();
 *             // save content
 *         } catch (IOException e) {
 *             return Observable.error(e);
 *         }
 *         return Observable.just(null);
 *     }
 * }).subscribe(new Action1<Void>() {
 *     &#064;
 *     public void call(Void aVoid) {
 *         // success
 *     }
 * }, new Action1<Throwable>() {
 *     &#064;
 *     public void call(Throwable throwable) {
 *         // handle error
 *     }
 * });

 * }
 * </pre>
 *
 * @author Tom Koptel
 * @since 2.3
 */
public class RxReportService {
    private final ReportService mSyncDelegate;

    @TestOnly
    RxReportService(ReportService reportService) {
        mSyncDelegate = reportService;
    }

    /**
     * Run report execution on the basis of passed options
     *
     * @param reportUri unique report uri
     * @param execOptions options that configure execution on JRS
     * @return object that encapsulates execution API
     */
    @NotNull
    public Observable<RxReportExecution> run(@NotNull final String reportUri, @Nullable final ReportExecutionOptions execOptions) {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");

        return Observable.defer(new Func0<Observable<RxReportExecution>>() {
            @Override
            public Observable<RxReportExecution> call() {
                try {
                    ReportExecution execution = mSyncDelegate.run(reportUri, execOptions);
                    RxReportExecution rxReportExecution = new RxReportExecution(execution);
                    return Observable.just(rxReportExecution);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    /**
     * Factory method to create new service
     *
     * @param client authorized network client
     * @return instance of newly created service
     */
    @NotNull
    public static RxReportService newService(@NotNull AuthorizedClient client) {
        Preconditions.checkNotNull(client, "Client should not be null");
        ReportService reportService = ReportService.newService(client);
        return new RxReportService(reportService);
    }
}
