package com.jaspersoft.android.sdk.service.report;

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
    public void shouldMapExecConfigurationOnOptions() {
        ExecutionConfiguration configuration = ExecutionConfiguration.builder()
                .format(ExecutionConfiguration.Format.HTML)
                .freshData(true)
                .interactive(false)
                .saveSnapshot(true)
                .pages("1-100")
                .params(REPORT_PARAMS)
                .attachmentPrefix("./")
                .create();

        ReportExecutionRequestOptions options = mapper.transform(REPORT_URI, BASE_URL, configuration);

        assertThat(options.getReportUnitUri(), is(REPORT_URI));
        assertThat(options.getFreshData(), is(true));
        assertThat(options.getSaveDataSnapshot(), is(true));
        assertThat(options.getInteractive(), is(false));
        assertThat(options.getSaveDataSnapshot(), is(true));
        assertThat(options.getAsync(), is(true));

        assertThat(options.getAllowInlineScripts(), is(nullValue()));
        assertThat(options.getTransformerKey(), is(nullValue()));

        assertThat(options.getBaseUrl(), is(BASE_URL));
        assertThat(options.getAttachmentsPrefix(), is(".%2F"));
        assertThat(options.getOutputFormat(), is("HTML"));
        assertThat(options.getPages(), is("1-100"));
        assertThat(options.getParameters(), is(REPORT_PARAMS));
    }
}