package com.jaspersoft.android.sdk.service.filter;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.entity.control.InputControl;
import com.jaspersoft.android.sdk.network.entity.control.InputControlState;
import com.jaspersoft.android.sdk.network.entity.dashboard.DashboardComponentCollection;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.data.report.option.ReportOption;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.DefaultExceptionMapper;
import com.jaspersoft.android.sdk.service.internal.Preconditions;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;
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
    private final ControlLocationMapper mControlLocationMapper;

    @TestOnly
    FiltersService(ReportOptionsUseCase reportOptionsUseCase,
                   ReportControlsUseCase reportControlsUseCase,
                   RepositoryUseCase repositoryUseCase,
                   ControlLocationMapper controlLocationMapper
    ) {
        mReportOptionsUseCase = reportOptionsUseCase;
        mReportControlsUseCase = reportControlsUseCase;
        mRepositoryUseCase = repositoryUseCase;
        mControlLocationMapper = controlLocationMapper;
    }

    @NotNull
    public static FiltersService newService(@NotNull AuthorizedClient client) {
        Preconditions.checkNotNull(client, "Client should not be null");

        ServiceExceptionMapper defaultMapper = new DefaultExceptionMapper();

        ReportOptionMapper reportOptionMapper = new ReportOptionMapper();
        ReportControlsUseCase reportControlsUseCase = new ReportControlsUseCase(defaultMapper, client.inputControlApi());
        ReportOptionsUseCase reportOptionsUseCase = new ReportOptionsUseCase(defaultMapper, client.reportOptionsApi(), reportOptionMapper);
        RepositoryUseCase repositoryUseCase = new RepositoryUseCase(defaultMapper, client.repositoryApi());

        ControlLocationMapper controlLocationMapper = new ControlLocationMapper();

        return new FiltersService(
                reportOptionsUseCase,
                reportControlsUseCase,
                repositoryUseCase,
                controlLocationMapper
        );
    }

    @NotNull
    public List<InputControl> listDashboardControls(@NotNull String dashboardUri) throws ServiceException {
        Preconditions.checkNotNull(dashboardUri, "Dashboard uri should not be null");

        DashboardComponentCollection componentCollection = mRepositoryUseCase.requestDashboardComponents(dashboardUri);
        List<ControlLocation> locations = mControlLocationMapper.transform(dashboardUri, componentCollection);

        List<InputControl> controls = new ArrayList<>();
        for (ControlLocation location : locations) {
            List<InputControl> inputControls = mReportControlsUseCase.requestControls(
                    location.getUri(), location.getIds(), false);
            controls.addAll(inputControls);
        }

        return Collections.unmodifiableList(controls);
    }

    @NotNull
    public List<InputControl> listReportControls(@NotNull String reportUri) throws ServiceException {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");

        return mReportControlsUseCase.requestControls(reportUri, Collections.<String>emptySet(), false);
    }

    @NotNull
    public List<InputControlState> listControlsValues(@NotNull String reportUri,
                                                      @NotNull List<ReportParameter> parameters,
                                                      boolean freshData) throws ServiceException {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");
        Preconditions.checkNotNull(parameters, "Parameters should not be null");

        return mReportControlsUseCase.requestControlsValues(reportUri, parameters, freshData);
    }

    @NotNull
    public List<InputControlState> validateControls(@NotNull String reportUri,
                                                    @NotNull List<ReportParameter> parameters) throws ServiceException {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");
        Preconditions.checkNotNull(parameters, "Parameters should not be null");

        List<InputControlState> states = mReportControlsUseCase.requestControlsValues(reportUri, parameters, true);

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
