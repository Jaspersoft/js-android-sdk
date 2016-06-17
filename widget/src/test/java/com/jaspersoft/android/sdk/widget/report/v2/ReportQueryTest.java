package com.jaspersoft.android.sdk.widget.report.v2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.rules.ExpectedException.none;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class ReportQueryTest {

    @Rule
    public ExpectedException expectedException = none();

    @Test
    public void should_fail_if_no_page_or_anchor() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("There should be either anchor, page or both.");

        new ReportQuery.Builder().build();
    }

    @Test
    public void should_create_query_with_page() throws Exception {
        ReportQuery query = new ReportQuery.Builder()
                .page(1)
                .build();
        assertThat(query.getPage(), is(1));
    }

    @Test
    public void should_create_query_with_anchor() throws Exception {
        ReportQuery query = new ReportQuery.Builder()
                .anchor("JR_PAGE_0_1")
                .build();
        assertThat(query.getAnchor(), is("JR_PAGE_0_1"));
    }
}