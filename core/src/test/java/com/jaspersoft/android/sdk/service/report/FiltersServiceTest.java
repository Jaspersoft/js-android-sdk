package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;

import static org.junit.rules.ExpectedException.none;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class FiltersServiceTest {

    private static final String REPORT_URI = "/my/uri";
    private static final String OPTION_LABEL = "label";
    private static final String OPTION_ID = OPTION_LABEL;
    private static final List<ReportParameter> REPORT_PARAMETERS = Collections.emptyList();

    @Mock
    ReportOptionsUseCase mReportOptionsUseCase;
    @Mock
    ReportControlsUseCase mReportControlsUseCase;

    private FiltersService mFiltersService;

    @Rule
    public ExpectedException expected = none();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mFiltersService = new FiltersService(mReportOptionsUseCase, mReportControlsUseCase);
    }

    @Test
    public void should_delegate_list_controls_call() throws Exception {
        mFiltersService.listControls(REPORT_URI);
        verify(mReportControlsUseCase).requestControls(eq(REPORT_URI), eq(false));
    }

    @Test
    public void should_delegate_load_cascade_controls_call() throws Exception {
        List<ReportParameter> parameters = Collections.emptyList();
        mFiltersService.listControlsValues(REPORT_URI, parameters, false);
        verify(mReportControlsUseCase).requestControlsValues(REPORT_URI, parameters, false);
    }

    @Test
    public void should_delegate_list_report_options_call() throws Exception {
        mFiltersService.listReportOptions(REPORT_URI);
        verify(mReportOptionsUseCase).requestReportOptionsList(eq(REPORT_URI));
    }

    @Test
    public void should_delegate_create_report_option_call() throws Exception {
        mFiltersService.createReportOption(REPORT_URI, OPTION_LABEL, REPORT_PARAMETERS, true);
        verify(mReportOptionsUseCase).createReportOption(REPORT_URI, OPTION_LABEL, REPORT_PARAMETERS, true);
    }
    @Test
    public void should_delegate_update_report_option_call() throws Exception {
        mFiltersService.updateReportOption(REPORT_URI, OPTION_ID, REPORT_PARAMETERS);
        verify(mReportOptionsUseCase).updateReportOption(REPORT_URI, OPTION_ID, REPORT_PARAMETERS);
    }

    @Test
    public void should_not_list_controls_with_null_uri() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Report uri should not be null");
        mFiltersService.listControls(null);
    }

    @Test
    public void should_not_list_controls_values_with_null_uri() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Parameters should not be null");
        mFiltersService.listControlsValues(REPORT_URI, null, false);
    }

    @Test
    public void should_not_list_controls_with_null_parameters() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Report uri should not be null");
        mFiltersService.listControlsValues(null, Collections.<ReportParameter>emptyList(), false);
    }

    @Test
    public void should_not_list_report_options_with_null_uri() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Report uri should not be null");
        mFiltersService.listReportOptions(null);
    }

    @Test
    public void should_not_create_report_option_with_null_uri() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Report uri should not be null");
        mFiltersService.createReportOption(null, OPTION_LABEL, REPORT_PARAMETERS, true);
    }

    @Test
    public void should_not_create_report_option_with_null_option_label() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Option label should not be null");
        mFiltersService.createReportOption(REPORT_URI, null, REPORT_PARAMETERS, true);
    }

    @Test
    public void should_not_create_report_option_with_null_parameters() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Parameters should not be null");
        mFiltersService.createReportOption(REPORT_URI, OPTION_LABEL, null, true);
    }

    @Test
    public void should_not_update_report_option_with_null_uri() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Report uri should not be null");
        mFiltersService.updateReportOption(null, OPTION_ID, REPORT_PARAMETERS);
    }

    @Test
    public void should_not_update_report_option_with_null_option_id() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Option id should not be null");
        mFiltersService.updateReportOption(REPORT_URI, null, REPORT_PARAMETERS);
    }

    @Test
    public void should_not_update_report_option_with_null_parameters() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Parameters should not be null");
        mFiltersService.updateReportOption(REPORT_URI, OPTION_ID, null);
    }

    @Test
    public void should_not_delete_report_option_with_null_uri() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Report uri should not be null");
        mFiltersService.deleteReportOption(null, OPTION_ID);
    }

    @Test
    public void should_not_delete_report_option_with_null_option_id() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Option id should not be null");
        mFiltersService.deleteReportOption(REPORT_URI, null);
    }



}