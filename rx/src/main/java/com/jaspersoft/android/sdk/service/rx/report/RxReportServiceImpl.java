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

import com.jaspersoft.android.sdk.network.entity.control.InputControl;
import com.jaspersoft.android.sdk.network.entity.control.InputControlState;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.network.entity.report.option.ReportOption;
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

import java.util.List;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class RxReportServiceImpl extends RxReportService {
    private final ReportService mSyncDelegate;

    @TestOnly
    RxReportServiceImpl(ReportService reportService) {
        mSyncDelegate = reportService;
    }

    @NotNull
    @Override
    public Observable<RxReportExecution> run(@NotNull final String reportUri, @Nullable final ReportExecutionOptions execOptions) {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");

        return Observable.defer(new Func0<Observable<RxReportExecution>>() {
            @Override
            public Observable<RxReportExecution> call() {
                try {
                    ReportExecution execution = mSyncDelegate.run(reportUri, execOptions);
                    RxReportExecution rxReportExecution = new RxReportExecutionImpl(execution);
                    return Observable.just(rxReportExecution);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    @NotNull
    @Override
    public Observable<List<InputControl>> listControls(@NotNull final String reportUri) {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");

        return Observable.defer(new Func0<Observable<List<InputControl>>>() {
            @Override
            public Observable<List<InputControl>> call() {
                try {
                    List<InputControl> inputControls = mSyncDelegate.listControls(reportUri);
                    return Observable.just(inputControls);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    @NotNull
    @Override
    public Observable<List<InputControlState>> listControlsValues(@NotNull final String reportUri,
                                                                  @NotNull final List<ReportParameter> parameters) {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");
        Preconditions.checkNotNull(parameters, "Parameters should not be null");

        return Observable.defer(new Func0<Observable<List<InputControlState>>>() {
            @Override
            public Observable<List<InputControlState>> call() {
                try {
                    List<InputControlState> inputControlStates = mSyncDelegate.listControlsValues(reportUri, parameters);
                    return Observable.just(inputControlStates);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    @NotNull
    @Override
    public Observable<Set<ReportOption>> listReportOptions(@NotNull final String reportUri) {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");

        return Observable.defer(new Func0<Observable<Set<ReportOption>>>() {
            @Override
            public Observable<Set<ReportOption>> call() {
                try {
                    Set<ReportOption> reportOptions = mSyncDelegate.listReportOptions(reportUri);
                    return Observable.just(reportOptions);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    @NotNull
    @Override
    public Observable<ReportOption> createReportOption(@NotNull final String reportUri,
                                                       @NotNull final String optionLabel,
                                                       @NotNull final List<ReportParameter> parameters,
                                                       final boolean overwrite) {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");
        Preconditions.checkNotNull(optionLabel, "Option label should not be null");
        Preconditions.checkNotNull(parameters, "Parameters should not be null");

        return Observable.defer(new Func0<Observable<ReportOption>>() {
            @Override
            public Observable<ReportOption> call() {
                try {
                    ReportOption reportOption = mSyncDelegate.createReportOption(reportUri, optionLabel, parameters, overwrite);
                    return Observable.just(reportOption);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    @NotNull
    @Override
    public Observable<Void> updateReportOption(@NotNull final String reportUri, @NotNull final String optionId, @NotNull final List<ReportParameter> parameters) {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");
        Preconditions.checkNotNull(optionId, "Option id should not be null");
        Preconditions.checkNotNull(parameters, "Parameters should not be null");

        return Observable.defer(new Func0<Observable<Void>>() {
            @Override
            public Observable<Void> call() {
                try {
                    mSyncDelegate.updateReportOption(reportUri, optionId, parameters);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
                return Observable.just(null);
            }
        });
    }

    @NotNull
    @Override
    public Observable<Void> deleteReportOption(@NotNull final String reportUri, @NotNull final String optionId) throws ServiceException {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");
        Preconditions.checkNotNull(optionId, "Option id should not be null");

        return Observable.defer(new Func0<Observable<Void>>() {
            @Override
            public Observable<Void> call() {
                try {
                    mSyncDelegate.deleteReportOption(reportUri, optionId);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
                return Observable.just(null);
            }
        });
    }
}
