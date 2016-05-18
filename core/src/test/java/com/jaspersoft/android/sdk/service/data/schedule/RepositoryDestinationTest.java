package com.jaspersoft.android.sdk.service.data.schedule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
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
    public void builder_should_not_accept_null_for_default_repo_destination() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Repository folder uri should not be null");
        new RepositoryDestination.Builder().withDefaultReportOutputFolderURI(null);
    }

   @Test
    public void builder_should_not_accept_null_for_output_local_path() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Output local path should not be null");
        new RepositoryDestination.Builder().withOutputLocalFolder(null);
    }

    @Test
    public void should_not_allow_invalid_timestampPattern() throws Exception {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("Unparseable timestamp 'ooo'");
        new RepositoryDestination.Builder().withTimestampPattern("ooo").build();
    }

    @Test
    public void should_use_default_folder_uri_if_one_not_supplied() throws Exception {
        RepositoryDestination destination = new RepositoryDestination.Builder().build();
        assertThat(destination.getUseDefaultReportOutputFolderURI(), is(true));
    }
}