package com.jaspersoft.android.sdk.service.data.schedule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.rules.ExpectedException.none;

public class JobOwnerTest {

    @Rule
    public ExpectedException expected = none();

    @Test
    public void factory_method1_should_reject_null_username() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Username should not be null");
        JobOwner.newOwner(null, null);
    }

    @Test
    public void factory_method2_should_reject_null_username() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Raw owner should not be null");
        JobOwner.newOwner(null);
    }

    @Test
    public void factory_method_should_parse_joined_owner() throws Exception {
        JobOwner expected = JobOwner.newOwner("jasperadmin|organization_1");
        assertThat(expected.getOrganization(), is("organization_1"));
        assertThat(expected.getUsername(), is("jasperadmin"));
    }

    @Test
    public void factory_method_should_create_owner_from_supplied_values() throws Exception {
        JobOwner expected = JobOwner.newOwner("jasperadmin", "organization_1");
        assertThat(expected.getOrganization(), is("organization_1"));
        assertThat(expected.getUsername(), is("jasperadmin"));
    }

    @Test
    public void to_string_should_return_raw_presentation_for_user_org() throws Exception {
        JobOwner expected = JobOwner.newOwner("jasperadmin|organization_1");
        assertThat(expected.toString(), is("jasperadmin|organization_1"));
    }

    @Test
    public void to_string_should_return_raw_presentation_for_user() throws Exception {
        JobOwner expected = JobOwner.newOwner("jasperadmin");
        assertThat(expected.toString(), is("jasperadmin"));
    }
}