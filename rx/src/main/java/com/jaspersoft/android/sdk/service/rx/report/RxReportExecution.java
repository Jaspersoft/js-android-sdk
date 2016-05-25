/*
 * Copyright (C) 2016 TIBCO Jaspersoft Corporation. All rights reserved.
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

package com.jaspersoft.android.sdk.service.rx.report;

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.data.report.ReportMetadata;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.Preconditions;
import com.jaspersoft.android.sdk.service.report.ReportExecution;
import com.jaspersoft.android.sdk.service.report.ReportExport;
import com.jaspersoft.android.sdk.service.report.ReportExportOptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
import rx.Observable;
import rx.functions.Func0;

import java.util.List;

/**
 * Public API allows to wait for report completion, wait for report completion and performs export.
 * All responses wrapped as Rx {@link rx.Observable}.
 *
 * @author Tom Koptel
 * @since 2.3
 */
public class RxReportExecution {
    private final ReportExecution mSyncDelegate;

    @TestOnly
    RxReportExecution(ReportExecution reportExecution) {
        mSyncDelegate = reportExecution;
    }

    /**
     * Initiates report export process
     *
     * @param options allows to configure export process of JRS side
     * @return export for corresponding execution
     */
    @NotNull
    public Observable<RxReportExport> export(final @NotNull ReportExportOptions options) {
        Preconditions.checkNotNull(options, "Export options should not be null");

        return Observable.defer(new Func0<Observable<RxReportExport>>() {
            @Override
            public Observable<RxReportExport> call() {
                try {
                    ReportExport export = mSyncDelegate.export(options);
                    RxReportExport rxReportExport = new RxReportExport(export);
                    return Observable.just(rxReportExport);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    /**
     * Performs series of requests until the status reach completion or fail response
     *
     * @return details of execution
     */
    @NotNull
    public Observable<ReportMetadata> waitForReportCompletion() {
        return Observable.defer(new Func0<Observable<ReportMetadata>>() {
            @Override
            public Observable<ReportMetadata> call() {
                try {
                    ReportMetadata data = mSyncDelegate.waitForReportCompletion();
                    return Observable.just(data);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    /**
     * Updates and restart report execution
     *
     * @param newParameters that force execution process to be restarted
     * @return new report execution
     */
    @NotNull
    public Observable<RxReportExecution> updateExecution(@Nullable final List<ReportParameter> newParameters) {
        return Observable.defer(new Func0<Observable<RxReportExecution>>() {
            @Override
            public Observable<RxReportExecution> call() {
                try {
                    ReportExecution execution = mSyncDelegate.updateExecution(newParameters);
                    RxReportExecution rxExec = new RxReportExecution(execution);
                    return Observable.just(rxExec);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    /**
     * Provides synchronous counterpart of service
     *
     * @return wrapped version of service {@link ReportExecution}
     */
    public ReportExecution toBlocking() {
        return mSyncDelegate;
    }

    /**
     * Exposes internal id of report execution session
     *
     * @return execution id
     */
    @NotNull
    public String getExecutionId() {
        return mSyncDelegate.getExecutionId();
    }
}
