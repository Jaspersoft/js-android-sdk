package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.network.entity.execution.ExecutionRequestOptions;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionRequestOptions;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ExecutionOptionsDataMapperTest {

    private static final String REPORT_URI = "/report/uri";
    private static final String BASE_URL = "http:://localhost";
    public static final Map<String, Set<String>> REPORT_PARAMS = Collections.emptyMap();

    private ExecutionOptionsDataMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = ExecutionOptionsDataMapper.getInstance();
    }

    @Test
    public void testTransformReportOptions() throws Exception {
        RunReportCriteria criteria = RunReportCriteria.builder()
                .format(ExecutionCriteria.Format.HTML)
                .freshData(true)
                .interactive(false)
                .saveSnapshot(true)
                .pages("1-100")
                .params(REPORT_PARAMS)
                .attachmentPrefix("./")
                .create();
        ReportExecutionRequestOptions options = mapper.transformRunReportOptions(REPORT_URI, BASE_URL, criteria);
        assertThat(options.getReportUnitUri(), is(REPORT_URI));
        assertThat(options.getParameters(), is(REPORT_PARAMS));
        assertThat(options.getAsync(), is(true));
        assertOptions(options);
    }

    @Test
    public void testTransformExportOptions() throws Exception {
        RunExportCriteria criteria = RunExportCriteria.builder()
                .format(ExecutionCriteria.Format.HTML)
                .freshData(true)
                .interactive(false)
                .saveSnapshot(true)
                .pages("1-100")
                .attachmentPrefix("./")
                .create();
        ExecutionRequestOptions options = mapper.transformExportOptions(BASE_URL, criteria);
        assertOptions(options);
    }

    private void assertOptions(ExecutionRequestOptions options) {
        assertThat(options.getFreshData(), is(true));
        assertThat(options.getSaveDataSnapshot(), is(true));
        assertThat(options.getInteractive(), is(false));
        assertThat(options.getSaveDataSnapshot(), is(true));

        assertThat(options.getAllowInlineScripts(), is(nullValue()));
        assertThat(options.getTransformerKey(), is(nullValue()));

        assertThat(options.getBaseUrl(), is(BASE_URL));
        assertThat(options.getAttachmentsPrefix(), is(".%2F"));
        assertThat(options.getOutputFormat(), is("HTML"));
        assertThat(options.getPages(), is("1-100"));
    }
}