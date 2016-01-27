package com.jaspersoft.android.sdk.service.data.schedule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.rules.ExpectedException.none;

public class JobSourceTest {
    @Rule
    public ExpectedException expected = none();

    @Test
    public void builder_should_not_accept_null_for_source() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Source uri should not be null");
        new JobSource.Builder().withUri(null);
    }

    @Test
    public void should_not_allow_destination_to_be_build_without_folder_uri() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Source can not be created without uri");
        new JobSource.Builder().build();
    }
}