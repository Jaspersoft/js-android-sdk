package com.jaspersoft.android.sdk.service.data.schedule;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.rules.ExpectedException.none;

public class RepositoryDestinationTest {
    private static final String DEFAULT_TIMESTAMP = "yyyyMMddHHmm";

    @Rule
    public ExpectedException expected = none();

    @Ignore
    public void builder_should_not_accept_null_for_repo_destination() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Repository folder uri should not be null");
        new RepositoryDestination.Builder().withFolderUri(null);
    }

    @Ignore
    public void should_not_allow_destination_to_be_build_without_folder_uri() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Repository destination can not be built without folder uri");
        new RepositoryDestination.Builder().build();
    }

    @Test
    public void should_not_allow_invalid_timestampPattern() throws Exception {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("Unparseable timestamp 'ooo'");
        new RepositoryDestination.Builder().withTimestampPattern("ooo").build();
    }

    @Test
    public void should_has_default_timestampPattern_if_null_provided() throws Exception {
        RepositoryDestination expected = new RepositoryDestination.Builder().withTimestampPattern(null).build();
        assertThat(expected.getTimestampPattern(), is(DEFAULT_TIMESTAMP));
    }
}