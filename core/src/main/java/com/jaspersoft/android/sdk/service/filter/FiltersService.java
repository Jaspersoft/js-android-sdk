package com.jaspersoft.android.sdk.service.filter;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.entity.control.InputControl;
import com.jaspersoft.android.sdk.network.entity.control.InputControlState;
import com.jaspersoft.android.sdk.network.entity.dashboard.DashboardComponentCollection;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.data.dashboard.DashboardControlComponent;
import com.jaspersoft.android.sdk.service.data.report.option.ReportOption;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.DefaultExceptionMapper;
import com.jaspersoft.android.sdk.service.internal.Preconditions;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;
import com.jaspersoft.android.sdk.service.internal.FiltersExceptionMapper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.util.*;

/**
 * @author Tom Koptel
 * @since 2.0
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

    @NotNull
    public List<DashboardControlComponent> listDashboardControlComponents(@NotNull String dashboardUri) throws ServiceException {
        Preconditions.checkNotNull(dashboardUri, "Dashboard uri should not be null");

        DashboardComponentCollection componentCollection = mRepositoryUseCase.requestDashboardComponents(dashboardUri);
        List<DashboardControlComponent> dashboardComponentsMapper = mDashboardComponentsMapper.toComponents(componentCollection);
        return Collections.unmodifiableList(dashboardComponentsMapper);
    }

    @NotNull
    public List<InputControl> listReportControls(@NotNull String reportUri) throws ServiceException {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");

        return mReportControlsUseCase.requestControls(reportUri, Collections.<String>emptySet(), false);
    }

    @NotNull
    public List<InputControlState> listControlsStates(@NotNull String reportUri,
                                                      @NotNull List<ReportParameter> parameters,
                                                      boolean freshData) throws ServiceException {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");
        Preconditions.checkNotNull(parameters, "Parameters should not be null");

        return mReportControlsUseCase.requestControlsValues(reportUri, parameters, freshData);
    }

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

    @NotNull
    public List<InputControlState> listResourceStates(@NotNull String resourceUri,
                                                      boolean freshData) throws ServiceException {
        Preconditions.checkNotNull(resourceUri, "Report uri should not be null");

        return mReportControlsUseCase.requestResourceValues(resourceUri, freshData);
    }

    @NotNull
    public Set<ReportOption> listReportOptions(@NotNull String reportUri) throws ServiceException {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");

        return mReportOptionsUseCase.requestReportOptionsList(reportUri);
    }

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

    public void updateReportOption(@NotNull String reportUri, @NotNull String optionId, @NotNull List<ReportParameter> parameters) throws ServiceException {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");
        Preconditions.checkNotNull(optionId, "Option id should not be null");
        Preconditions.checkNotNull(parameters, "Parameters should not be null");

        mReportOptionsUseCase.updateReportOption(reportUri, optionId, parameters);
    }

    public void deleteReportOption(@NotNull String reportUri, @NotNull String optionId) throws ServiceException {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");
        Preconditions.checkNotNull(optionId, "Option id should not be null");

        mReportOptionsUseCase.deleteReportOption(reportUri, optionId);
    }
}
