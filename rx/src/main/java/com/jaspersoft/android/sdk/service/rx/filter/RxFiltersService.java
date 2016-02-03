package com.jaspersoft.android.sdk.service.rx.filter;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.entity.control.InputControl;
import com.jaspersoft.android.sdk.network.entity.control.InputControlState;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.data.dashboard.DashboardControlComponent;
import com.jaspersoft.android.sdk.service.data.report.option.ReportOption;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.filter.FiltersService;
import com.jaspersoft.android.sdk.service.internal.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;
import rx.Observable;
import rx.functions.Func0;

import java.util.List;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class RxFiltersService {

    private final FiltersService mSyncDelegate;

    @TestOnly
    RxFiltersService(FiltersService syncDelegate) {
        mSyncDelegate = syncDelegate;
    }

    @NotNull
    public static RxFiltersService newService(@NotNull AuthorizedClient authorizedClient) {
        Preconditions.checkNotNull(authorizedClient, "Client should not be null");
        FiltersService filtersService = FiltersService.newService(authorizedClient);
        return new RxFiltersService(filtersService);
    }

    @NotNull
    public Observable<List<InputControl>> listReportControls(@NotNull final String reportUri) {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");

        return Observable.defer(new Func0<Observable<List<InputControl>>>() {
            @Override
            public Observable<List<InputControl>> call() {
                try {
                    List<InputControl> inputControls = mSyncDelegate.listReportControls(reportUri);
                    return Observable.just(inputControls);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    @NotNull
    public Observable<List<InputControl>> listDashboardControls(@NotNull final String dashboardUri) {
        Preconditions.checkNotNull(dashboardUri, "Dashboard uri should not be null");

        return Observable.defer(new Func0<Observable<List<InputControl>>>() {
            @Override
            public Observable<List<InputControl>> call() {
                try {
                    List<InputControl> inputControls = mSyncDelegate.listDashboardControls(dashboardUri);
                    return Observable.just(inputControls);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    @NotNull
    public Observable<List<DashboardControlComponent>> listDashboardControlComponents(@NotNull final String dashboardUri) {
        Preconditions.checkNotNull(dashboardUri, "Dashboard uri should not be null");

        return Observable.defer(new Func0<Observable<List<DashboardControlComponent>>>() {
            @Override
            public Observable<List<DashboardControlComponent>> call() {
                try {
                    List<DashboardControlComponent>inputControls =
                            mSyncDelegate.listDashboardControlComponents(dashboardUri);
                    return Observable.just(inputControls);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    @NotNull
    public Observable<List<InputControlState>> listControlsStates(@NotNull final String reportUri,
                                                                  @NotNull final List<ReportParameter> controls,
                                                                  final boolean freshData
    ) {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");
        Preconditions.checkNotNull(controls, "Input controls should not be null");

        return Observable.defer(new Func0<Observable<List<InputControlState>>>() {
            @Override
            public Observable<List<InputControlState>> call() {
                try {
                    List<InputControlState> inputControlStates = mSyncDelegate.listControlsStates(reportUri, controls, freshData);
                    return Observable.just(inputControlStates);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    @NotNull
    public Observable<List<InputControlState>> validateControls(@NotNull final String reportUri,
                                                                @NotNull final List<ReportParameter> controls,
                                                                final boolean freshData
    ) {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");
        Preconditions.checkNotNull(controls, "Input controls should not be null");

        return Observable.defer(new Func0<Observable<List<InputControlState>>>() {
            @Override
            public Observable<List<InputControlState>> call() {
                try {
                    List<InputControlState> inputControlStates = mSyncDelegate.validateControls(reportUri, controls, freshData);
                    return Observable.just(inputControlStates);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    @NotNull
    public Observable<List<InputControlState>> listResourceStates(@NotNull final String reportUri,
                                                                  final boolean freshData
    ) {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");

        return Observable.defer(new Func0<Observable<List<InputControlState>>>() {
            @Override
            public Observable<List<InputControlState>> call() {
                try {
                    List<InputControlState> inputControlStates = mSyncDelegate.listResourceStates(reportUri, freshData);
                    return Observable.just(inputControlStates);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    @NotNull
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
    public Observable<Void> deleteReportOption(@NotNull final String reportUri, @NotNull final String optionId) {
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
