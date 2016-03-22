package com.jaspersoft.android.sdk.service.data.schedule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.rules.ExpectedException.none;

public class RepositoryDestinationTest {
    @Rule
    public ExpectedException expected = none();

    @Test
    public void builder_should_not_accept_null_for_repo_destination() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Repository folder uri should not be null");
        new RepositoryDestination.Builder().withFolderUri(null);
    }

    @Test
    public void should_not_allow_destination_to_be_build_without_folder_uri() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Repository destination can not be built without folder uri");
        new RepositoryDestination.Builder().build();
    }
}