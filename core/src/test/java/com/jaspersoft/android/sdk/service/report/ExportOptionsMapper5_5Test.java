package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.network.entity.execution.ExecutionRequestOptions;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class ExportOptionsMapper5_5Test {
    private static final String BASE_URL = "http://localhost";

    private ExportOptionsMapper5_5 mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ExportOptionsMapper5_5(BASE_URL);
    }

    @Test
    public void should_exclude_ignore_pagination_flag() throws Exception {
        ReportExportOptions criteria = ReportExportOptions.builder()
                .withFormat(ReportFormat.PDF)
                .withIgnorePagination(true)
                .build();
        ExecutionRequestOptions options = mapper.transform(criteria);
        assertThat("Failed to remove 'ignorePagination' option", options.getIgnorePagination(), is(nullValue()));
    }


    @Test
    public void should_exclude_base_url() throws Exception {
        ReportExportOptions criteria = ReportExportOptions.builder()
                .withFormat(ReportFormat.PDF)
                .withIgnorePagination(true)
                .build();
        ExecutionRequestOptions options = mapper.transform(criteria);
        assertThat("Failed to remove 'baseUrl' option", options.getBaseUrl(), is(nullValue()));
    }
}