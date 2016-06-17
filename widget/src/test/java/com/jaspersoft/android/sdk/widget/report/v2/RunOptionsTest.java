package com.jaspersoft.android.sdk.widget.report.v2;

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.rules.ExpectedException.none;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class RunOptionsTest {

    @Rule
    public ExpectedException expectedException = none();

    @Test
    public void should_fail_if_no_page_or_anchor() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("There should be either anchor, page or both.");

        new RunOptions.Builder().build();
    }

    @Test
    public void should_create_options_with_page() throws Exception {
        RunOptions options = new RunOptions.Builder()
                .page(1)
                .build();
        assertThat(options.getPage(), is(1));
    }

    @Test
    public void should_create_options_with_anchor() throws Exception {
        RunOptions options = new RunOptions.Builder()
                .anchor("JR_PAGE_0_1")
                .build();
        assertThat(options.getAnchor(), is("JR_PAGE_0_1"));
    }

    @Test
    public void should_create_options_with_unmodifiable_parameters() throws Exception {
        expectedException.expect(UnsupportedOperationException.class);

        ReportParameter parameter = new ReportParameter("key", Collections.singleton("value"));
        List<ReportParameter> modifiableParams = new ArrayList<>();
        modifiableParams.add(parameter);

        RunOptions options = new RunOptions.Builder()
                .page(1)
                .parameters(modifiableParams)
                .build();

        options.getParameters().add(parameter);
    }
}