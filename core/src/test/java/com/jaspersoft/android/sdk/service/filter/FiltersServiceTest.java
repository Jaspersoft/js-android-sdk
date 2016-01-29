package com.jaspersoft.android.sdk.service.filter;

import com.jaspersoft.android.sdk.network.entity.dashboard.DashboardComponentCollection;
import com.jaspersoft.android.sdk.network.entity.dashboard.InputControlDashboardComponent;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.rules.ExpectedException.none;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class FiltersServiceTest {

    private static final String RESOURCE_URI = "/my/uri";
    private static final String LOCATION_URI = "/location";
    private static final String OPTION_LABEL = "label";
    private static final String OPTION_ID = OPTION_LABEL;
    private static final List<ReportParameter> REPORT_PARAMETERS = Collections.emptyList();

    private static final DashboardComponentCollection COMPONENTS =
            new DashboardComponentCollection(Collections.<InputControlDashboardComponent>emptyList());

    private static final ControlLocation LOCATION = new ControlLocation(LOCATION_URI).addId("id");
    private static final List<ControlLocation> LOCATIONS = Collections.singletonList(LOCATION);
    @Mock
    ReportOptionsUseCase mReportOptionsUseCase;
    @Mock
    ReportControlsUseCase mReportControlsUseCase;
    @Mock
    RepositoryUseCase mRepositoryUseCase;

    @Mock
    ControlLocationMapper mControlLocationMapper;

    private FiltersService mFiltersService;

    @Rule
    public ExpectedException expected = none();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mFiltersService = new FiltersService(
                mReportOptionsUseCase,
                mReportControlsUseCase,
                mRepositoryUseCase,
                mControlLocationMapper
        );
    }

    @Test
    public void should_delegate_list_report_controls_call() throws Exception {
        mFiltersService.listReportControls(RESOURCE_URI);
        verify(mReportControlsUseCase).requestControls(eq(RESOURCE_URI), eq(Collections.<String>emptySet()), eq(false));
    }

    @Test
    public void should_list_report_controls_call() throws Exception {
        when(mRepositoryUseCase.requestDashboardComponents(anyString())).thenReturn(COMPONENTS);
        when(mControlLocationMapper.transform(anyString(), any(DashboardComponentCollection.class))).thenReturn(LOCATIONS);

        mFiltersService.listDashboardControls(RESOURCE_URI);

        verify(mRepositoryUseCase).requestDashboardComponents(eq(RESOURCE_URI));
        verify(mControlLocationMapper).transform(RESOURCE_URI, COMPONENTS);
        verify(mReportControlsUseCase).requestControls(LOCATION_URI, new HashSet<>(Arrays.asList("id")), false);
    }

    @Test
    public void should_delegate_load_cascade_controls_call() throws Exception {
        List<ReportParameter> parameters = Collections.emptyList();
        mFiltersService.listResourceValues(RESOURCE_URI, false);
        verify(mReportControlsUseCase).requestResourceValues(RESOURCE_URI, false);
    }

    @Test
    public void should_delegate_list_report_options_call() throws Exception {
        mFiltersService.listReportOptions(RESOURCE_URI);
        verify(mReportOptionsUseCase).requestReportOptionsList(eq(RESOURCE_URI));
    }

    @Test
    public void should_delegate_create_report_option_call() throws Exception {
        mFiltersService.createReportOption(RESOURCE_URI, OPTION_LABEL, REPORT_PARAMETERS, true);
        verify(mReportOptionsUseCase).createReportOption(RESOURCE_URI, OPTION_LABEL, REPORT_PARAMETERS, true);
    }
    @Test
    public void should_delegate_update_report_option_call() throws Exception {
        mFiltersService.updateReportOption(RESOURCE_URI, OPTION_ID, REPORT_PARAMETERS);
        verify(mReportOptionsUseCase).updateReportOption(RESOURCE_URI, OPTION_ID, REPORT_PARAMETERS);
    }

    @Test
    public void should_not_list_dashboard_controls_with_null_uri() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Dashboard uri should not be null");
        mFiltersService.listDashboardControls(null);
    }

    @Test
    public void should_not_list_controls_with_null_uri() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Report uri should not be null");
        mFiltersService.listReportControls(null);
    }

    @Test
    public void should_not_list_controls_with_null_parameters() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Report uri should not be null");
        mFiltersService.listResourceValues(null, false);
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
        mFiltersService.createReportOption(RESOURCE_URI, null, REPORT_PARAMETERS, true);
    }

    @Test
    public void should_not_create_report_option_with_null_parameters() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Parameters should not be null");
        mFiltersService.createReportOption(RESOURCE_URI, OPTION_LABEL, null, true);
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
        mFiltersService.updateReportOption(RESOURCE_URI, null, REPORT_PARAMETERS);
    }

    @Test
    public void should_not_update_report_option_with_null_parameters() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Parameters should not be null");
        mFiltersService.updateReportOption(RESOURCE_URI, OPTION_ID, null);
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
        mFiltersService.deleteReportOption(RESOURCE_URI, null);
    }



}