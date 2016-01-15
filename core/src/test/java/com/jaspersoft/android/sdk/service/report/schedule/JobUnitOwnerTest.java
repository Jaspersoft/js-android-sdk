package com.jaspersoft.android.sdk.service.report.schedule;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.rules.ExpectedException.none;

public class JobUnitOwnerTest {
    @Rule
    public ExpectedException expected = none();

    @Test
    public void maps_username_organization_toString() throws Exception {
        JobOwner jobOwner = JobOwner.newOwner("jasperadmin", "organization_1");
        assertThat(jobOwner.toString(), is("jasperadmin|organization_1"));
    }

    @Test
    public void maps_username_toString() throws Exception {
        JobOwner jobOwner = JobOwner.newOwner("jasperadmin", null);
        assertThat(jobOwner.toString(), is("jasperadmin"));
    }

    @Test
    public void factory_fails_with_null_username() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Username should not be null");
        JobOwner.newOwner(null, null);
    }

    @Test
    public void testEquals() throws Exception {
        EqualsVerifier.forClass(JobOwner.class).verify();
    }
}