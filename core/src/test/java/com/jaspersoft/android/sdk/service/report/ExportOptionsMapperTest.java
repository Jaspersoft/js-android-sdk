package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.network.entity.execution.ExecutionRequestOptions;
import com.jaspersoft.android.sdk.service.data.report.PageRange;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ExportOptionsMapperTest {
    private static final String BASE_URL = "http://localhost";

    private ExportOptionsMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ExportOptionsMapper(BASE_URL) {};
    }

    @Test
    public void should_map_format() {
        ReportExportOptions criteria = ReportExportOptions.builder()
                .withFormat(ReportFormat.PDF)
                .build();
        ExecutionRequestOptions options = mapper.transform(criteria);
        assertThat("Failed to map 'format' option", options.getOutputFormat(), is("pdf"));
    }

    @Test
    public void should_map_pages() {
        PageRange range = PageRange.parse("1-10");
        ReportExportOptions criteria = ReportExportOptions.builder()
                .withFormat(ReportFormat.PDF)
                .withPageRange(range)
                .build();
        ExecutionRequestOptions options = mapper.transform(criteria);
        assertThat("Failed to map 'pages' option", options.getPages(), is("1-10"));
    }

    @Test
    public void should_include_ignore_pagination_flag() throws Exception {
        ReportExportOptions criteria = ReportExportOptions.builder()
                .withFormat(ReportFormat.PDF)
                .withIgnorePagination(true)
                .build();
        ExecutionRequestOptions options = mapper.transform(criteria);
        assertThat("Failed to map 'ignorePagination' option", options.getIgnorePagination(), is(true));
    }

    @Test
    public void should_map_anchor() {
        ReportExportOptions criteria = ReportExportOptions.builder()
                .withFormat(ReportFormat.PDF)
                .withAnchor("anchor")
                .build();
        ExecutionRequestOptions options = mapper.transform(criteria);
        assertThat("Failed to map 'anchor' option", options.getAnchor(), is("anchor"));
    }

    @Test
    public void should_map_decoded_attachmentPrefix() {
        ReportExportOptions criteria = ReportExportOptions.builder()
                .withFormat(ReportFormat.PDF)
                .withAttachmentPrefix("./")
                .build();
        ExecutionRequestOptions options = mapper.transform(criteria);
        assertThat("Failed to map 'attachmentPrefix' option", options.getAttachmentsPrefix(), is(".%2F"));
    }

    @Test
    public void should_map_allowInlineScripts() {
        ReportExportOptions criteria = ReportExportOptions.builder()
                .withFormat(ReportFormat.PDF)
                .withAllowInlineScripts(true)
                .build();
        ExecutionRequestOptions options = mapper.transform(criteria);
        assertThat("Failed to map 'allowInlineScripts' option", options.getAllowInlineScripts(), is(true));
    }

    @Test
    public void should_map_baseUrl() {
        ReportExportOptions criteria = ReportExportOptions.builder()
                .withFormat(ReportFormat.PDF)
                .build();
        ExecutionRequestOptions options = mapper.transform(criteria);
        assertThat("Failed to map 'baseUrl' option", options.getBaseUrl(), is(BASE_URL));
    }
}