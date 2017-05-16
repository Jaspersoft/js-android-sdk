package com.jaspersoft.android.sdk.service.rx.dashboard;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.service.dashboard.DashboardService;
import com.jaspersoft.android.sdk.service.data.dashboard.DashboardExportOptions;
import com.jaspersoft.android.sdk.service.data.report.ResourceOutput;
import com.jaspersoft.android.sdk.service.exception.ServiceException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import rx.observers.TestSubscriber;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class RxDashboardServiceTest {
    @Mock
    AuthorizedClient authorizedClient;
    @Mock
    DashboardService dashboardService;
    @Mock
    DashboardExportOptions dashboardExportOptions;
    @Mock
    ResourceOutput resourceOutput;
    @Mock
    ServiceException serviceException;

    @Rule
    public ExpectedException expected = none();

    private RxDashboardService rxDashboardService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        rxDashboardService = new RxDashboardService(dashboardService);
    }

    @Test
    public void testDelegateExportRequest() throws Exception {
        when(dashboardService.export(dashboardExportOptions)).thenReturn(resourceOutput);

        TestSubscriber<ResourceOutput> test = new TestSubscriber<>();
        rxDashboardService.export(dashboardExportOptions).subscribe(test);

        test.assertNoErrors();
        test.assertCompleted();
        test.assertValueCount(1);
        test.assertValue(resourceOutput);

        verify(dashboardService).export(dashboardExportOptions);
    }

    @Test
    public void testDelegateExportError() throws Exception {
        doThrow(serviceException).when(dashboardService).export(dashboardExportOptions);

        TestSubscriber<ResourceOutput> test = new TestSubscriber<>();
        rxDashboardService.export(dashboardExportOptions).subscribe(test);

        test.assertError(serviceException);
        test.assertNotCompleted();

        verify(dashboardService).export(dashboardExportOptions);
    }

    @Test
    public void testServiceCreation() throws Exception {
        RxDashboardService service = RxDashboardService.newService(authorizedClient);
        assertThat(service, is(instanceOf(RxDashboardService.class)));
        assertThat(service, is(notNullValue()));
    }

    @Test
    public void testServiceCreationWithNullClient() throws Exception {
        expected.expectMessage("Client should not be null");
        expected.expect(NullPointerException.class);

        RxDashboardService.newService(null);
    }
}
