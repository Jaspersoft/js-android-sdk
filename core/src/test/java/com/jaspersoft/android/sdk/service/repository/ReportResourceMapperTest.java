package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.TestConstants;
import com.jaspersoft.android.sdk.network.entity.resource.ReportLookup;
import com.jaspersoft.android.sdk.service.data.report.ReportResource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ReportResourceMapperTest {
    @Mock
    ReportLookup mReportLookup;

    private ReportResourceMapper mapper;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mapper = new ReportResourceMapper(TestConstants.DATE_TIME_FORMAT);
        mReportLookup = MockResourceFactory.mockCommonFields(mReportLookup);
    }

    @Test
    public void should_map_file_lookup() throws Exception {
        mockFileLookup();
        ReportResource resource = mapper.transform(mReportLookup);
        assertThat(resource.alwaysPromptControls(), is(true));
    }

    private void mockFileLookup() {
        when(mReportLookup.alwaysPromptControls()).thenReturn(true);
        when(mReportLookup.getResourceType()).thenReturn("reportUnit");
    }
}