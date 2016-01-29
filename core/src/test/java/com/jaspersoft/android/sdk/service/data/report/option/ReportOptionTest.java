package com.jaspersoft.android.sdk.service.data.report.option;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.rules.ExpectedException.none;

public class ReportOptionTest {

    private ReportOption.Builder mBuilder;

    @Rule
    public ExpectedException expected = none();

    @Before
    public void setUp() throws Exception {
        mBuilder = new ReportOption.Builder();
    }

    @Test
    public void testEquals() throws Exception {
        EqualsVerifier.forClass(ReportOption.class).verify();
    }

    @Test
    public void builder_should_not_allow_null_id() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Id should not be null");
        new ReportOption.Builder().withId(null);
    }

    @Test
    public void builder_should_not_allow_null_uri() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Uri should not be null");
        new ReportOption.Builder().withUri(null);
    }

    @Test
    public void builder_should_not_allow_null_label() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Label should not be null");
        new ReportOption.Builder().withLabel(null);
    }

    @Test
    public void build_should_not_allow_null_id() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Report option can not be created without id");
        new ReportOption.Builder()
                .withUri("/my/uri")
                .withLabel("label")
                .build();
    }

    @Test
    public void build_should_not_allow_null_uri() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Report option can not be created without uri");
        new ReportOption.Builder()
                .withId("id")
                .withLabel("label")
                .build();
    }

    @Test
    public void build_should_not_allow_null_label() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Report option can not be created without label");
        new ReportOption.Builder()
                .withId("id")
                .withUri("/my/uri")
                .build();
    }
}