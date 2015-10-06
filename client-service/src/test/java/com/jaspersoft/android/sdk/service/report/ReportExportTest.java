package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.network.api.ReportExportRestApi;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ReportExportTest {
    private static final String EXECUTION_ID = "any_id";

    @Mock
    ReportExportRestApi.Factory mExportApiFactory;
    @Mock
    ReportExportRestApi mExportRestApi;

    private ReportExport objectUnderTest;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(mExportApiFactory.get()).thenReturn(mExportRestApi);
        objectUnderTest = new ReportExport(EXECUTION_ID, mExportApiFactory);
    }
}