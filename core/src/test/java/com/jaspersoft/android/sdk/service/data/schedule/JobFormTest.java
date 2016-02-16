package com.jaspersoft.android.sdk.service.data.schedule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.MockitoAnnotations.initMocks;

public class JobFormTest {
    @Rule
    public ExpectedException expected = none();

    @Mock
    JobSource mSource;
    @Mock
    RepositoryDestination mDestination;

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
    public void should_not_allow_job_without_label() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Job can not be scheduled without label");

        new JobForm.Builder()
                .withRepositoryDestination(mDestination)
                .withJobSource(mSource)
                .withBaseOutputFilename("output")
                .addOutputFormat(JobOutputFormat.HTML)
                .build();
    }

    @Test
    public void should_not_allow_job_without_repo_destination() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Job can not be scheduled without repository destination");

        new JobForm.Builder()
                .withLabel("my label")
                .withJobSource(mSource)
                .withBaseOutputFilename("output")
                .addOutputFormat(JobOutputFormat.HTML)
                .build();
    }

    @Test
    public void should_not_allow_job_without_source() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Job can not be scheduled without source");

        new JobForm.Builder()
                .withLabel("my label")
                .withRepositoryDestination(mDestination)
                .withBaseOutputFilename("output")
                .addOutputFormat(JobOutputFormat.HTML)
                .build();
    }

    @Test
    public void should_not_allow_job_without_output_file_name() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Job can not be scheduled without output file name");

        new JobForm.Builder()
                .withLabel("my label")
                .withRepositoryDestination(mDestination)
                .withJobSource(mSource)
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
    public void form_new_builder() throws Exception {
        JobForm form = new JobForm.Builder()
                .withLabel("my label")
                .withRepositoryDestination(mDestination)
                .withJobSource(mSource)
                .withBaseOutputFilename("output")
                .addOutputFormat(JobOutputFormat.HTML)
                .build();
        JobForm expected = form.newBuilder().build();
        assertThat(expected.getLabel(), is(form.getLabel()));
        assertThat(expected.getDescription(), is(form.getDescription()));
        assertThat(expected.getRepositoryDestination(), is(form.getRepositoryDestination()));
        assertThat(expected.getSource(), is(form.getSource()));
        assertThat(expected.getBaseOutputFilename(), is(form.getBaseOutputFilename()));
        assertThat(expected.getOutputFormats(), is(form.getOutputFormats()));
    }
}