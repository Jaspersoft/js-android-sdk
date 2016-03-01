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
 * The corresponding service exposes API related to the handling of input controls, input control states of both report and dashboard resources.
 * Allows to validate input controls and load dashboard components.
 * Also, one allows performing CRUD operations over report operations.
 * All responses wrapped as Rx {@link rx.Observable}
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
 * AuthorizedClient client = server.newClient(credentials).create();
 *
 * final RxFiltersService service = RxFiltersService.newService(client);
 *
 * final String reportUri = "/my/report/uri";
 * String dashboardUri = "/my/dashboard/uri";
 *
 * final Action1<Throwable> errorHandler = new Action1<Throwable>() {
 *     &#064;
 *     public void call(Throwable throwable) {
 *         // handle API error
 *     }
 * };
 *
 * service.listDashboardControls(dashboardUri).subscribe(new Action1<List<InputControl>>() {
 *     &#064;
 *     public void call(List<InputControl> controls) {
 *         // success
 *     }
 * }, errorHandler);
 *
 * service.listDashboardControlComponents(dashboardUri).subscribe(new Action1<List<DashboardControlComponent>>() {
 *     &#064;
 *     public void call(List<DashboardControlComponent> dashboardControlComponents) {
 *         // success
 *     }
 * }, errorHandler);
 *
 * service.listReportControls(reportUri).subscribe(new Action1<List<InputControl>>() {
 *     &#064;
 *     public void call(List<InputControl> controls) {
 *         // success
 *     }
 * }, errorHandler);
 *
 * boolean freshData = true;
 * service.listResourceStates(reportUri, freshData).subscribe(new Action1<List<InputControlState>>() {
 *     &#064;
 *     public void call(List<InputControlState> inputControlStates) {
 *         // success
 *     }
 * }, errorHandler);
 *
 * final List<ReportParameter> parameters = Collections.singletonList(
 *         new ReportParameter("key", Collections.singleton("value"))
 * );
 * service.listControlsStates(reportUri, parameters, freshData).subscribe(new Action1<List<InputControlState>>() {
 *     &#064;
 *     public void call(List<InputControlState> inputControlStates) {
 *         // success
 *     }
 * }, errorHandler);
 *
 * service.validateControls(reportUri, parameters, freshData).subscribe(new Action1<List<InputControlState>>() {
 *     &#064;
 *     public void call(List<InputControlState> inputControlStates) {
 *         // success
 *     }
 * }, errorHandler);
 *
 * service.listReportOptions(reportUri).subscribe(new Action1<Set<ReportOption>>() {
 *     &#064;
 *     public void call(Set<ReportOption> reportOptions) {
 *         // success
 *     }
 * }, errorHandler);
 *
 * boolean overwrite = true;
 * service.createReportOption(reportUri, "report option", parameters, overwrite).subscribe(new Action1<ReportOption>() {
 *     &#064;
 *     public void call(ReportOption reportOption) {
 *         String reportId = reportOption.getId();
 *         service.updateReportOption(reportUri, reportId, parameters).subscribe(new Action1<Void>() {
 *             &#064;
 *             public void call(Void aVoid) {
 *                 // success
 *             }
 *         }, errorHandler);
 *
 *         service.deleteReportOption(reportUri, reportId).subscribe(new Action1<Void>() {
 *             &#064;
 *             public void call(Void aVoid) {
 *                 // success
 *             }
 *         }, errorHandler);
 *     }
 * }, errorHandler);
 * }
 * </pre>
 *
 * @author Tom Koptel
 * @since 2.3
 */
public class RxFiltersService {

    private final FiltersService mSyncDelegate;

    @TestOnly
    RxFiltersService(FiltersService syncDelegate) {
        mSyncDelegate = syncDelegate;
    }

    /**
     * Factory method to create new service
     *
     * @param authorizedClient authorized network client
     * @return instance of newly created service
     */
    @NotNull
    public static RxFiltersService newService(@NotNull AuthorizedClient authorizedClient) {
        Preconditions.checkNotNull(authorizedClient, "Client should not be null");
        FiltersService filtersService = FiltersService.newService(authorizedClient);
        return new RxFiltersService(filtersService);
    }

    /**
     * Performs network call that retrieves dashboard controls on the basis of passed uri
     *
     * @param dashboardUri unique dashboard uri
     * @return list of dashboard input controls
     */
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

    /**
     * Performs network call that retrieves dashboard control components on the basis of passed uri
     *
     * @param dashboardUri unique dashboard uri
     * @return list of dashboard control components
     */
    @NotNull
    public Observable<List<DashboardControlComponent>> listDashboardControlComponents(@NotNull final String dashboardUri) {
        Preconditions.checkNotNull(dashboardUri, "Dashboard uri should not be null");

        return Observable.defer(new Func0<Observable<List<DashboardControlComponent>>>() {
            @Override
            public Observable<List<DashboardControlComponent>> call() {
                try {
                    List<DashboardControlComponent> inputControls =
                            mSyncDelegate.listDashboardControlComponents(dashboardUri);
                    return Observable.just(inputControls);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    /**
     * Performs network call that retrieves report controls on the basis of passed uri
     *
     * @param reportUri unique report uri
     * @return list of report input controls
     */
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

    /**
     * Performs network call that retrieves control states
     *
     * @param reportUri unique report uri
     * @param parameters that are passed to load cascade states
     * @param freshData flag that forces data to be retrieved directly from sources
     * @return list of control states with corresponding report options
     */
    @NotNull
    public Observable<List<InputControlState>> listControlsStates(@NotNull final String reportUri,
                                                                  @NotNull final List<ReportParameter> parameters,
                                                                  final boolean freshData
    ) {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");
        Preconditions.checkNotNull(parameters, "Input controls should not be null");

        return Observable.defer(new Func0<Observable<List<InputControlState>>>() {
            @Override
            public Observable<List<InputControlState>> call() {
                try {
                    List<InputControlState> inputControlStates = mSyncDelegate.listControlsStates(reportUri, parameters, freshData);
                    return Observable.just(inputControlStates);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    /**
     * Validates report parameters of corresponding input controls
     *
     * @param reportUri unique report uri
     * @param parameters that are passed to validate input controls validity
     * @param freshData flag that forces data to be retrieved directly from sources
     * @return empty list if validation succeed otherwise collects states with corresponding errors
     */
    @NotNull
    public Observable<List<InputControlState>> validateControls(@NotNull final String reportUri,
                                                                @NotNull final List<ReportParameter> parameters,
                                                                final boolean freshData
    ) {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");
        Preconditions.checkNotNull(parameters, "Input controls should not be null");

        return Observable.defer(new Func0<Observable<List<InputControlState>>>() {
            @Override
            public Observable<List<InputControlState>> call() {
                try {
                    List<InputControlState> inputControlStates = mSyncDelegate.validateControls(reportUri, parameters, freshData);
                    return Observable.just(inputControlStates);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    /**
     * Retrieves all input controls states
     *
     * @param reportUri unique report uri
     * @param freshData flag that forces data to be retrieved directly from sources
     * @return list of report input controls states
     */
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

    /**
     * List report options of corresponding report
     *
     * @param reportUri unique report uri
     * @return set of report options
     */
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

    /**
     * Public API that allows create/override report option
     *
     * @param reportUri unique report uri
     * @param optionLabel label of report option
     * @param parameters that is associated with report option
     * @param overwrite if passed true than will overwrite report with same label on serve side
     * @return newly created/override report option
     */
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

    /**
     * Updates report option with new report parameter
     *
     * @param reportUri unique report uri
     * @param optionId unique identifier of corresponding report option
     * @param parameters report values that are passed to override latest version of option
     */
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

    /**
     * Deletes report option
     *
     * @param reportUri unique report uri
     * @param optionId unique identifier of corresponding report option
     */
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
