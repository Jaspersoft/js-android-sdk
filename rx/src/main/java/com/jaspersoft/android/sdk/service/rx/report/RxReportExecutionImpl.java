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
 * @author Tom Koptel
 * @since 2.0
 */
final class RxReportExecutionImpl implements RxReportExecution {
    private final ReportExecution mSyncDelegate;

    @TestOnly
    RxReportExecutionImpl(ReportExecution reportExecution) {
        mSyncDelegate = reportExecution;
    }

    @NotNull
    @Override
    public Observable<RxReportExport> export(final @NotNull ReportExportOptions options) {
        Preconditions.checkNotNull(options, "Export options should not be null");

        return Observable.defer(new Func0<Observable<RxReportExport>>() {
            @Override
            public Observable<RxReportExport> call() {
                try {
                    ReportExport export = mSyncDelegate.export(options);
                    RxReportExport rxReportExport = new RxReportExportImpl(export);
                    return Observable.just(rxReportExport);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    @NotNull
    @Override
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

    @NotNull
    @Override
    public Observable<RxReportExecution> updateExecution(@Nullable final List<ReportParameter> newParameters) {
        return Observable.defer(new Func0<Observable<RxReportExecution>>() {
            @Override
            public Observable<RxReportExecution> call() {
                try {
                    ReportExecution execution = mSyncDelegate.updateExecution(newParameters);
                    RxReportExecution rxExec = new RxReportExecutionImpl(execution);
                    return Observable.just(rxExec);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
            }
        });
    }
}
