package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.network.entity.export.ExportExecutionDescriptor;
import com.jaspersoft.android.sdk.service.data.report.PageRange;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        ExportExecutionDescriptor.class,
})
public class ExportIdWrapperTest {

    private static final String EXPORT_ID = "export_id";

    @Mock
    ExportExecutionDescriptor mExportDetails;

    private ExportIdWrapper mWrapper5_5;
    private ExportIdWrapper mWrapper5_6Plus;
    private ReportExportOptions mOptions;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void should_generate_specific_exact_id_for_5_5() throws Exception {
        giveExportDetailsWithId();
        givenExportForFirstHtmlPage();
        givenConfigured5_5Wrapper();

        String exactId = mWrapper5_5.getExactId();
        assertThat(exactId, is("HTML;pages=1"));
    }

    @Test
    public void should_return_export_descriptor_id_for_5_5() throws Exception {
        giveExportDetailsWithId();
        givenConfigured5_5Wrapper();

        String serverId = mWrapper5_5.getServerId();
        assertThat(serverId, is(EXPORT_ID));
    }

    @Test
    public void should_generate_specific_exact_id_for_5_6Plus() throws Exception {
        giveExportDetailsWithId();
        givenExportForFirstHtmlPage();
        givenConfigured5_6PlusWrapper();

        String exactId = mWrapper5_6Plus.getExactId();
        assertThat(exactId, is(EXPORT_ID));
    }

    @Test
    public void should_return_export_descriptor_id_for_5_6Plus() throws Exception {
        giveExportDetailsWithId();
        givenConfigured5_6PlusWrapper();

        String serverId = mWrapper5_6Plus.getServerId();
        assertThat(serverId, is(EXPORT_ID));
    }

    private void giveExportDetailsWithId() {
        when(mExportDetails.getExportId()).thenReturn(EXPORT_ID);
    }

    private void givenExportForFirstHtmlPage() {
        mOptions = ReportExportOptions.builder().withFormat(ReportFormat.HTML)
                .withPageRange(PageRange.parse("1"))
                .build();
    }

    private void givenConfigured5_5Wrapper() {
        mWrapper5_5 = ExportIdWrapper5_5.getInstance();
        mWrapper5_5.wrap(mExportDetails, mOptions);
    }

    private void givenConfigured5_6PlusWrapper() {
        mWrapper5_6Plus = ExportIdWrapper5_6Plus.getInstance();
        mWrapper5_6Plus.wrap(mExportDetails, mOptions);
    }
}