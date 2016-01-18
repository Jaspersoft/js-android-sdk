package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.entity.control.InputControl;
import com.jaspersoft.android.sdk.network.entity.control.InputControlState;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.network.entity.report.option.ReportOption;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.DefaultExceptionMapper;
import com.jaspersoft.android.sdk.service.internal.Preconditions;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.util.List;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class FiltersService {
    protected final ReportOptionsUseCase mReportOptionsUseCase;
    protected final ReportControlsUseCase mReportControlsUseCase;

    @TestOnly
    FiltersService(ReportOptionsUseCase reportOptionsUseCase, ReportControlsUseCase reportControlsUseCase) {
        mReportOptionsUseCase = reportOptionsUseCase;
        mReportControlsUseCase = reportControlsUseCase;
    }

    @NotNull
    public static FiltersService newService(@NotNull AuthorizedClient client) {
        Preconditions.checkNotNull(client, "Client should not be null");

        ServiceExceptionMapper defaultMapper = new DefaultExceptionMapper();

        ReportControlsUseCase reportControlsUseCase = new ReportControlsUseCase(defaultMapper, client.inputControlApi());
        ReportOptionsUseCase reportOptionsUseCase = new ReportOptionsUseCase(defaultMapper, client.reportOptionsApi());

        return new FiltersService(reportOptionsUseCase, reportControlsUseCase);
    }


    @NotNull
    public final List<InputControl> listControls(@NotNull String reportUri) throws ServiceException {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");

        return mReportControlsUseCase.requestControls(reportUri, false);
    }

    @NotNull
    public final List<InputControlState> listControlsValues(@NotNull String reportUri,
                                                            @NotNull List<ReportParameter> parameters,
                                                            boolean freshData) throws ServiceException {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");
        Preconditions.checkNotNull(parameters, "Parameters should not be null");

        return mReportControlsUseCase.requestControlsValues(reportUri, parameters, freshData);
    }

    @NotNull
    public final Set<ReportOption> listReportOptions(@NotNull String reportUri) throws ServiceException {
        Preconditions.checkNotNull(reportUri, "Report uri should not be null");

        return mReportOptionsUseCase.requestReportOptionsList(reportUri);
    }

    @NotNull
    public final ReportOption createReportOption(@NotNull String reportUri,
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
