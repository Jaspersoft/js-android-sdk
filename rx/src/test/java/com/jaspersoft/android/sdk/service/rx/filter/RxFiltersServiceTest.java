package com.jaspersoft.android.sdk.service.rx.filter;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.entity.control.InputControl;
import com.jaspersoft.android.sdk.network.entity.control.InputControlState;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.network.entity.report.option.ReportOption;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.filter.FiltersService;
import com.jaspersoft.android.sdk.service.report.ReportExecution;
import com.jaspersoft.android.sdk.service.rx.report.RxReportService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import rx.observers.TestSubscriber;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * TODO: investigate while tests broken during run. It looks like mockito fails to deliver proper mocking behavior.
 *
 * @author Tom Koptel
 * @since 2.0
 */
public class RxFiltersServiceTest {

    private static final String RESOURCE_URI = "/my/uri";
    private static final List<ReportParameter> PARAMS = Collections.emptyList();
    private static final String OPTION_LABEL = "label";
    private static final String OPTION_ID = OPTION_LABEL;

    @Mock
    FiltersService mSyncDelegate;
    @Mock
    ReportExecution mReportExecution;
    @Mock
    ServiceException mServiceException;

    private ReportOption fakeReportOption = new ReportOption();

    @Mock
    AuthorizedClient mAuthorizedClient;

    private RxFiltersService rxFiltersService;

    @Rule
    public ExpectedException expected = none();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        rxFiltersService = new RxFiltersService(mSyncDelegate);
    }

    @Test
    public void should_delegate_service_exception_to_subscription_on_list_controls() throws Exception {
        when(mSyncDelegate.listReportControls(anyString())).thenThrow(mServiceException);

        TestSubscriber<List<InputControl>> test = TestSubscriber.create();
        rxFiltersService.listReportControls(RESOURCE_URI).subscribe(test);

        test.assertError(mServiceException);
        test.assertNotCompleted();

        verify(mSyncDelegate).listReportControls(RESOURCE_URI);
    }

    @Test
    public void should_execute_delegate_as_observable_on_list_controls() throws Exception {
        when(mSyncDelegate.listReportControls(anyString())).thenReturn(Collections.<InputControl>emptyList());

        TestSubscriber<List<InputControl>> test = TestSubscriber.create();
        rxFiltersService.listReportControls(RESOURCE_URI).subscribe(test);

        test.assertCompleted();
        test.assertNoErrors();
        test.assertValueCount(1);

        verify(mSyncDelegate).listReportControls(RESOURCE_URI);
    }

    @Test
    public void should_delegate_service_exception_to_subscription_on_list_controls_values() throws Exception {
        when(mSyncDelegate.listControlsValues(anyString(), anyListOf(ReportParameter.class), anyBoolean())).thenThrow(mServiceException);

        TestSubscriber<List<InputControlState>> test = TestSubscriber.create();
        rxFiltersService.listControlsValues(RESOURCE_URI, PARAMS, false).subscribe(test);

        test.assertError(mServiceException);
        test.assertNotCompleted();

        verify(mSyncDelegate).listControlsValues(RESOURCE_URI, PARAMS, false);
    }

    @Test
    public void should_execute_delegate_as_observable_on_list_controls_values() throws Exception {
        when(mSyncDelegate.listControlsValues(anyString(), anyListOf(ReportParameter.class), anyBoolean()))
                .thenReturn(Collections.<InputControlState>emptyList());

        TestSubscriber<List<InputControlState>> test = TestSubscriber.create();
        rxFiltersService.listControlsValues(RESOURCE_URI, PARAMS, false).subscribe(test);

        test.assertCompleted();
        test.assertNoErrors();
        test.assertValueCount(1);

        verify(mSyncDelegate).listControlsValues(RESOURCE_URI, PARAMS, false);
    }

    @Test
    public void should_delegate_service_exception_to_subscription_on_list_report_options() throws Exception {
        when(mSyncDelegate.listReportOptions(anyString())).thenThrow(mServiceException);

        TestSubscriber<Set<ReportOption>> test = TestSubscriber.create();
        rxFiltersService.listReportOptions(RESOURCE_URI).subscribe(test);

        test.assertError(mServiceException);
        test.assertNotCompleted();

        verify(mSyncDelegate).listReportOptions(RESOURCE_URI);
    }

    @Test
    public void should_delegate_service_exception_to_subscription_on_list_dashboard_controls() throws Exception {
        when(mSyncDelegate.listDashboardControls(anyString())).thenThrow(mServiceException);

        TestSubscriber<List<InputControl>> test = TestSubscriber.create();
        rxFiltersService.listDashboardControls(RESOURCE_URI).subscribe(test);

        test.assertError(mServiceException);
        test.assertNotCompleted();

        verify(mSyncDelegate).listDashboardControls(RESOURCE_URI);
    }

    @Test
    public void should_execute_delegate_as_observable_on_list_report_options() throws Exception {
        when(mSyncDelegate.listReportOptions(anyString()))
                .thenReturn(Collections.<ReportOption>emptySet());

        TestSubscriber<Set<ReportOption>> test = TestSubscriber.create();
        rxFiltersService.listReportOptions(RESOURCE_URI).subscribe(test);

        test.assertCompleted();
        test.assertNoErrors();
        test.assertValueCount(1);

        verify(mSyncDelegate).listReportOptions(RESOURCE_URI);
    }

    @Test
    public void should_delegate_service_exception_to_subscription_on_create_report_option() throws Exception {
        when(mSyncDelegate.createReportOption(anyString(), anyString(), anyListOf(ReportParameter.class), anyBoolean()))
                .thenThrow(mServiceException);

        TestSubscriber<ReportOption> test = TestSubscriber.create();
        rxFiltersService.createReportOption(RESOURCE_URI, OPTION_LABEL, PARAMS, true).subscribe(test);

        test.assertError(mServiceException);
        test.assertNotCompleted();

        verify(mSyncDelegate).createReportOption(RESOURCE_URI, OPTION_LABEL, PARAMS, true);
    }

    @Test
    public void should_execute_delegate_as_observable_on_create_report_option() throws Exception {
        when(mSyncDelegate.createReportOption(anyString(), anyString(), anyListOf(ReportParameter.class), anyBoolean()))
                .thenReturn(fakeReportOption);

        TestSubscriber<ReportOption> test = TestSubscriber.create();
        rxFiltersService.createReportOption(RESOURCE_URI, OPTION_LABEL, PARAMS, true).subscribe(test);

        test.assertCompleted();
        test.assertNoErrors();
        test.assertValueCount(1);

        verify(mSyncDelegate).createReportOption(RESOURCE_URI, OPTION_LABEL, PARAMS, true);
    }

    @Test
    public void should_delegate_service_exception_to_subscription_on_update_report_option() throws Exception {
        doThrow(mServiceException).when(mSyncDelegate).updateReportOption(anyString(),
                anyString(), anyListOf(ReportParameter.class));

        TestSubscriber<Void> test = TestSubscriber.create();
        rxFiltersService.updateReportOption(RESOURCE_URI, OPTION_ID, PARAMS).subscribe(test);

        test.assertError(mServiceException);
        test.assertNotCompleted();

        verify(mSyncDelegate).updateReportOption(RESOURCE_URI, OPTION_ID, PARAMS);
    }

    @Test
    public void should_execute_delegate_as_observable_on_update_report_option() throws Exception {
        TestSubscriber<Void> test = TestSubscriber.create();
        rxFiltersService.updateReportOption(RESOURCE_URI, OPTION_ID, PARAMS).subscribe(test);

        test.assertCompleted();
        test.assertNoErrors();
        test.assertValueCount(1);

        verify(mSyncDelegate).updateReportOption(RESOURCE_URI, OPTION_ID, PARAMS);
    }

    @Test
    public void should_delegate_service_exception_to_subscription_on_delete_report_option() throws Exception {
        doThrow(mServiceException).when(mSyncDelegate).deleteReportOption(anyString(), anyString());

        TestSubscriber<Void> test = TestSubscriber.create();
        rxFiltersService.deleteReportOption(RESOURCE_URI, OPTION_ID).subscribe(test);

        test.assertError(mServiceException);
        test.assertNotCompleted();

        verify(mSyncDelegate).deleteReportOption(RESOURCE_URI, OPTION_ID);
    }

    @Test
    public void should_execute_delegate_as_observable_on_delete_report_option() throws Exception {
        TestSubscriber<Void> test = TestSubscriber.create();
        rxFiltersService.deleteReportOption(RESOURCE_URI, OPTION_ID).subscribe(test);

        test.assertCompleted();
        test.assertNoErrors();
        test.assertValueCount(1);

        verify(mSyncDelegate).deleteReportOption(RESOURCE_URI, OPTION_ID);
    }

    @Test
    public void should_execute_delegate_as_observable_on_list_dashboards_controls() throws Exception {
        TestSubscriber<List<InputControl>> test = TestSubscriber.create();
        rxFiltersService.listDashboardControls(RESOURCE_URI).subscribe(test);

        test.assertNoErrors();
        test.assertCompleted();
        test.assertValueCount(1);

        verify(mSyncDelegate).listDashboardControls(RESOURCE_URI);
    }

    @Test
    public void should_not_accept_null_for_factory_method() throws Exception {
        expected.expectMessage("Client should not be null");
        expected.expect(NullPointerException.class);
        RxFiltersService.newService(null);
    }

    @Test
    public void should_provide_impl_with_factory_method() throws Exception {
        RxFiltersService service = RxFiltersService.newService(mAuthorizedClient);
        assertThat(service, is(notNullValue()));
    }
}
