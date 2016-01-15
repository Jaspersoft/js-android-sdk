package com.jaspersoft.android.sdk.service.report.schedule;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.rules.ExpectedException.none;

public class JobUnitSearchCriteriaTest {

    private JobSearchCriteria.Builder mBuilder;

    @Rule
    public ExpectedException expected = none();

    @Before
    public void setUp() throws Exception {
        mBuilder = JobSearchCriteria.builder();
    }

    @Test
    public void builds_with_reportUnitURI() throws Exception {
        mBuilder.withReportUri("/my/uri");
        JobSearchCriteria params = mBuilder.build();
        assertThat("Builder Does not maps report uri", params.getReportUri(), is("/my/uri"));

        JobSearchCriteria newParams = params.newBuilder().build();
        assertThat("New builder should inherit report uri", newParams.getReportUri(), is("/my/uri"));
    }

    @Test
    public void builds_with_owner() throws Exception {
        JobOwner owner = JobOwner.newOwner("user", "org");

        mBuilder.withOwner(owner);
        JobSearchCriteria params = mBuilder.build();
        assertThat("Builder does not maps owner", params.getOwner(), is(owner));

        JobSearchCriteria newParams = params.newBuilder().build();
        assertThat("New builder should inherit report uri", newParams.getOwner(), is(owner));
    }

    @Test
    public void builds_with_label() throws Exception {
        mBuilder.withLabel("label");
        JobSearchCriteria params = mBuilder.build();
        assertThat("Builder Does not maps label", params.getLabel(), is("label"));

        JobSearchCriteria newParams = params.newBuilder().build();
        assertThat("New builder should inherit label", newParams.getLabel(), is("label"));
    }

    @Test
    public void builds_with_startIndex() throws Exception {
        mBuilder.withOffset(0);
        JobSearchCriteria params = mBuilder.build();
        assertThat("Builder Does not startIndex", params.getOffset(), is(0));

        JobSearchCriteria newParams = params.newBuilder().build();
        assertThat("New builder should startIndex", newParams.getOffset(), is(0));
    }

    @Test
    public void builds_with_numberOfRows() throws Exception {
        mBuilder.withLimit(JobSearchCriteria.UNLIMITED_ROW_NUMBER);
        JobSearchCriteria params = mBuilder.build();
        assertThat("Builder Does not numberOfRows", params.getLimit(), is(JobSearchCriteria.UNLIMITED_ROW_NUMBER));

        JobSearchCriteria newParams = params.newBuilder().build();
        assertThat("New builder should numberOfRows", newParams.getLimit(), is(JobSearchCriteria.UNLIMITED_ROW_NUMBER));
    }

    @Test
    public void builds_with_sortType() throws Exception {
        mBuilder.withSortType(JobSortType.NONE);
        JobSearchCriteria params = mBuilder.build();
        assertThat("Builder Does not sortType", params.getSortType(), is(JobSortType.NONE));

        JobSearchCriteria newParams = params.newBuilder().build();
        assertThat("New builder should sortType", newParams.getSortType(), is(JobSortType.NONE));
    }

    @Test
    public void builds_with_isAscending() throws Exception {
        mBuilder.withAscending(true);
        JobSearchCriteria params = mBuilder.build();
        assertThat("Builder Does not isAscending", params.getAscending(), is(true));

        JobSearchCriteria newParams = params.newBuilder().build();
        assertThat("New builder should isAscending", newParams.getAscending(), is(true));
    }

    @Test
    public void testEquals() throws Exception {
        EqualsVerifier.forClass(JobSearchCriteria.class).verify();
    }

    @Test
    public void should_not_accept_negative_row_number() throws Exception {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("Row number must be positive");
        mBuilder.withLimit(-1);
    }

    @Test
    public void should_not_accept_zero_row_number() throws Exception {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("Row number must be positive");
        mBuilder.withLimit(0);
    }

    @Test
    public void should_not_accept_zero_start_index() throws Exception {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("Start index must be positive");
        mBuilder.withOffset(-1);
    }
}