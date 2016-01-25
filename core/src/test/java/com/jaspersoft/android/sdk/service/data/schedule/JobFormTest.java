package com.jaspersoft.android.sdk.service.data.schedule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import java.util.Collections;

import static org.junit.rules.ExpectedException.none;
import static org.mockito.MockitoAnnotations.initMocks;

public class JobFormTest {
    @Rule
    public ExpectedException expected = none();

    @Mock
    JobTrigger mJobTrigger;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void should_not_allow_job_with_no_output_formats() throws Exception {
        expected.expect(IllegalStateException.class);
        expected.expectMessage("Job can not be scheduled without output format");

        new JobForm.Builder()
                .addOutputFormats(Collections.<JobOutputFormat>emptySet())
                .build();
    }

    @Test
    public void should_not_allow_job_without_trigger() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Job can not be scheduled without trigger");

        new JobForm.Builder()
                .withLabel("my label")
                .withRepositoryDestination("/temp")
                .withSource("/my/uri")
                .withBaseOutputFilename("output")
                .addOutputFormat(JobOutputFormat.HTML)
                .build();
    }

    @Test
    public void should_not_allow_job_without_label() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Job can not be scheduled without label");

        new JobForm.Builder()
                .withRepositoryDestination("/temp")
                .withSource("/my/uri")
                .withBaseOutputFilename("output")
                .withTrigger(mJobTrigger)
                .addOutputFormat(JobOutputFormat.HTML)
                .build();
    }

    @Test
    public void should_not_allow_job_without_repo_destination() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Job can not be scheduled without repo destination");

        new JobForm.Builder()
                .withLabel("my label")
                .withSource("/my/uri")
                .withBaseOutputFilename("output")
                .withTrigger(mJobTrigger)
                .addOutputFormat(JobOutputFormat.HTML)
                .build();
    }

    @Test
    public void should_not_allow_job_without_source() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Job can not be scheduled without source");

        new JobForm.Builder()
                .withLabel("my label")
                .withRepositoryDestination("/temp")
                .withBaseOutputFilename("output")
                .addOutputFormat(JobOutputFormat.HTML)
                .withTrigger(mJobTrigger)
                .build();
    }


    @Test
    public void should_not_allow_job_without_output_file_name() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Job can not be scheduled without output file name");

        new JobForm.Builder()
                .withLabel("my label")
                .withRepositoryDestination("/temp")
                .withSource("/my/uri")
                .withTrigger(mJobTrigger)
                .addOutputFormat(JobOutputFormat.HTML)
                .build();
    }

    @Test
    public void builder_should_not_accept_null_for_label() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Label should not be null");
        new JobForm.Builder().withLabel(null);
    }

    @Test
    public void builder_should_not_accept_null_for_output_file_name() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Output file name should not be null");
        new JobForm.Builder().withBaseOutputFilename(null);
    }

    @Test
    public void builder_should_not_accept_null_for_source() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Source should not be null");
        new JobForm.Builder().withSource(null);
    }

    @Test
    public void builder_should_not_accept_null_for_repo_destination() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Repository destination should not be null");
        new JobForm.Builder().withRepositoryDestination(null);
    }

    @Test
    public void builder_should_not_accept_null_for_formats() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Formats should not be null");
        new JobForm.Builder().addOutputFormats(null);
    }


    @Test
    public void builder_should_not_accept_null_for_format() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Format should not be null");
        new JobForm.Builder().addOutputFormat(null);
    }

    @Test
    public void builder_should_not_accept_null_for_trigger() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Trigger should not be null");
        new JobForm.Builder().withTrigger(null);
    }
}