package com.jaspersoft.android.sdk.service.filter;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.entity.control.InputControl;
import com.jaspersoft.android.sdk.network.entity.control.InputControlState;
import com.jaspersoft.android.sdk.network.entity.dashboard.DashboardComponentCollection;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.data.dashboard.DashboardControlComponent;
import com.jaspersoft.android.sdk.service.data.report.option.ReportOption;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.FiltersExceptionMapper;
import com.jaspersoft.android.sdk.service.internal.Preconditions;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.util.*;

/**
 * The corresponding service exposes API related to the handling of input controls, input control states of both report and dashboard resources.
 * Allows to validate input controls and load dashboard components.
 * Also, one allows performing CRUD operations over report operations.
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
 *  FiltersService service = FiltersService.newService(client);
 *
 *  String reportUri = "/my/report/uri";
 *  String dashboardUri = "/my/dashboard/uri";
 *
 *  try {
 *      List<InputControl> dashboardControls = service.listDashboardControls(dashboardUri);
 *
 *      List<DashboardControlComponent> dashboardControlComponents = service.listDashboardControlComponents(dashboardUri);
 *
 *      List<InputControl> reportControls = service.listReportControls(reportUri);
 *
 *      boolean freshData = true;
 *      List<InputControlState> states = service.listResourceStates(reportUri, freshData);
 *
 *      List<ReportParameter> parameters = Collections.singletonList(
 *              new ReportParameter("key", Collections.singleton("value"))
 *      );
 *      List<InputControlState> cascadeStates = service.listControlsStates(reportUri, parameters, freshData);
 *
 *      List<InputControlState> validateStates = service.validateControls(reportUri, parameters, freshData);
 *
 *      Set<ReportOption> reportOptions = service.listReportOptions(reportUri);
 *
 *      boolean overwrite = true;
 *      ReportOption reportOption = service.createReportOption(reportUri, "report option", parameters, overwrite);
 *
 *      String reportId = reportOption.getId();
 *      service.updateReportOption(reportUri, reportId, parameters);
 *
 *      service.deleteReportOption(reportUri, reportId);
 *
 *  } catch (ServiceException e) {
 *      // handle network issue
 *  }
 * }
 * </pre>
 * @author Tom Koptel
 * @since 2.3
 */
public class FiltersService {
    private final ReportOptionsUseCase mReportOptionsUseCase;
    private final ReportControlsUseCase mReportControlsUseCase;
    private final RepositoryUseCase mRepositoryUseCase;
    private final DashboardComponentsMapper mDashboardComponentsMapper;

    @TestOnly
    FiltersService(ReportOptionsUseCase reportOptionsUseCase,
                   ReportControlsUseCase reportControlsUseCase,
                   RepositoryUseCase repositoryUseCase,
                   DashboardComponentsMapper dashboardComponentsMapper
    ) {
        mReportOptionsUseCase = reportOptionsUseCase;
        mReportControlsUseCase = reportControlsUseCase;
        mRepositoryUseCase = repositoryUseCase;
        mDashboardComponentsMapper = dashboardComponentsMapper;
    }

    /**
     * Factory method to create new service
     *
     * @param client authorized network client
     * @return instance of newly created service
     */
    @NotNull
    public static FiltersService newService(@NotNull AuthorizedClient client) {
        Preconditions.checkNotNull(client, "Client should not be null");

        ServiceExceptionMapper exceptionMapper = FiltersExceptionMapper.getInstance();

        ReportOptionMapper reportOptionMapper = new ReportOptionMapper();
        ReportControlsUseCase reportControlsUseCase = new ReportControlsUseCase(exceptionMapper, client.inputControlApi());
        ReportOptionsUseCase reportOptionsUseCase = new ReportOptionsUseCase(exceptionMapper, client.reportOptionsApi(), reportOptionMapper);
        RepositoryUseCase repositoryUseCase = new RepositoryUseCase(exceptionMapper, client.repositoryApi());

        DashboardComponentsMapper dashboardComponentsMapper = new DashboardComponentsMapper();

        return new FiltersService(
                reportOptionsUseCase,
                reportControlsUseCase,
                repositoryUseCase,
                dashboardComponentsMapper
        );
    }

    /**
     * Performs network call that retrieves dashboard controls on the basis of passed uri
     *
     * @param dashboardUri unique dashboard uri
     * @return list of dashboard input controls
     * @throws ServiceException wraps both http/network/api related errors
     */
    @NotNull
    public List<InputControl> listDashboardControls(@NotNull String dashboardUri) throws ServiceException {
        Preconditions.checkNotNull(dashboardUri, "Dashboard uri should not be null");

        DashboardComponentCollection componentCollection = mRepositoryUseCase.requestDashboardComponents(dashboardUri);
        List<ControlLocation> locations = mDashboardComponentsMapper.toLocations(dashboardUri, componentCollection);

        List<InputControl> controls = new ArrayList<>();
        for (ControlLocation location : locations) {
            List<InputControl> inputControls = mReportControlsUseCase.requestControls(
                    location.getUri(), location.getIds(), false);
            controls.addAll(inputControls);
        }

        return Collections.unmodifiableList(controls);
    }

    /**
     * Performs network call that retrieves dashboard control components on the basis of passed uri
     *
     * @param dashboardUri unique dashboard uri
     * @return list of dashboard control components
     * @throws ServiceException wraps both http/network/api related errors
     */
    @NotNull
    public List<DashboardControlComponent> listDashboardControlComponents(@NotNull String dashboardUri) throws ServiceException {
        Preconditions.checkNotNull(dashboardUri, "Dashboard uri should not be null");

        DashboardComponentCollection componentCollection = mRepositoryUseCase.requestDashboardComponents(dashboardUri);
        List<DashboardControlComponent> dashboardComponentsMapper = mDashboardComponentsMapper.toComponents(componentCollection);
        return Collections.unmodifiableList(dashboardComponentsMapper);
    }

    /**
     * Performs network call that retrieves report controls on the basis of passed uri
     *
     * @param reportUri unique report uri
     * @return list of report input controls
     * @throws ServiceException wraps both http/network/api related errors
     */
    @NotNull
    public List<InputControl> listReportControls(@NotNull String reportUri) throws ServiceException {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");

        return mReportControlsUseCase.requestControls(reportUri, Collections.<String>emptySet(), false);
    }

    /**
     * Performs network call that retrieves control states
     *
     * @param reportUri unique report uri
     * @param parameters that are passed to load cascade states
     * @param freshData flag that forces data to be retrieved directly from sources
     * @return list of control states with corresponding report options
     * @throws ServiceException wraps both http/network/api related errors
     */
    @NotNull
    public List<InputControlState> listControlsStates(@NotNull String reportUri,
                                                      @NotNull List<ReportParameter> parameters,
                                                      boolean freshData) throws ServiceException {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");
        Preconditions.checkNotNull(parameters, "Parameters should not be null");

        return mReportControlsUseCase.requestControlsValues(reportUri, parameters, freshData);
    }

    /**
     * Validates report parameters of corresponding input controls
     * 
     * @param reportUri unique report uri
     * @param parameters that are passed to validate input controls validity
     * @param freshData flag that forces data to be retrieved directly from sources
     * @return empty list if validation succeed otherwise collects states with corresponding errors 
     * @throws ServiceException wraps both http/network/api related errors
     */
    @NotNull
    public List<InputControlState> validateControls(@NotNull String reportUri,
                                                    @NotNull List<ReportParameter> parameters,
                                                    boolean freshData) throws ServiceException {
        List<InputControlState> states = listControlsStates(reportUri, parameters, freshData);

        List<InputControlState> invalidStates = new ArrayList<>(states);
        Iterator<InputControlState> iterator = invalidStates.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getError() == null) {
                iterator.remove();
            }
        }

        return Collections.unmodifiableList(invalidStates);
    }

    /**
     * Retrieves all input controls states
     *
     * @param reportUri unique report uri
     * @param freshData flag that forces data to be retrieved directly from sources
     * @return list of report input controls states
     * @throws ServiceException wraps both http/network/api related errors
     */
    @NotNull
    public List<InputControlState> listResourceStates(@NotNull String reportUri,
                                                      boolean freshData) throws ServiceException {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");

        return mReportControlsUseCase.requestResourceValues(reportUri, freshData);
    }

    /**
     * List report options of corresponding report
     * 
     * @param reportUri unique report uri
     * @return set of report options
     * @throws ServiceException wraps both http/network/api related errors
     */
    @NotNull
    public Set<ReportOption> listReportOptions(@NotNull String reportUri) throws ServiceException {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");

        return mReportOptionsUseCase.requestReportOptionsList(reportUri);
    }

    /**
     * Public API that allows create/override report option
     * 
     * @param reportUri unique report uri
     * @param optionLabel label of report option
     * @param parameters that is associated with report option
     * @param overwrite if passed true than will overwrite report with same label on serve side
     * @return newly created/override report option
     * @throws ServiceException wraps both http/network/api related errors
     */
    @NotNull
    public ReportOption createReportOption(@NotNull String reportUri,
                                           @NotNull String optionLabel,
                                           @NotNull List<ReportParameter> parameters,
                                           boolean overwrite) throws ServiceException {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");
        Preconditions.checkNotNull(optionLabel, "Option label should not be null");
        Preconditions.checkNotNull(parameters, "Parameters should not be null");

        return mReportOptionsUseCase.createReportOption(reportUri, optionLabel, parameters, overwrite);
    }

    /**
     * Updates report option with new report parameter
     * 
     * @param reportUri unique report uri
     * @param optionId unique identifier of corresponding report option
     * @param parameters report values that are passed to override latest version of option
     * @throws ServiceException wraps both http/network/api related errors
     */
    public void updateReportOption(@NotNull String reportUri, @NotNull String optionId, @NotNull List<ReportParameter> parameters) throws ServiceException {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");
        Preconditions.checkNotNull(optionId, "Option id should not be null");
        Preconditions.checkNotNull(parameters, "Parameters should not be null");

        mReportOptionsUseCase.updateReportOption(reportUri, optionId, parameters);
    }

    /**
     * Deletes report option
     *
     * @param reportUri unique report uri
     * @param optionId unique identifier of corresponding report option
     * @throws ServiceException wraps both http/network/api related errors
     */
    public void deleteReportOption(@NotNull String reportUri, @NotNull String optionId) throws ServiceException {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");
        Preconditions.checkNotNull(optionId, "Option id should not be null");

        mReportOptionsUseCase.deleteReportOption(reportUri, optionId);
    }
}
