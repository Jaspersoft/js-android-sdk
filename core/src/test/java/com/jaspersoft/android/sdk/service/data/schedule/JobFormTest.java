/*
 * Copyright (C) 2016 TIBCO Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/mobile-sdk-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile SDK for Android.
 *
 * TIBCO Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile SDK for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

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
                .withOutputFormats(Collections.<JobOutputFormat>emptySet())
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
                .withOutputFormats(Collections.singletonList(JobOutputFormat.HTML))
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
                .withOutputFormats(Collections.singletonList(JobOutputFormat.HTML))
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
                .withOutputFormats(Collections.singletonList(JobOutputFormat.HTML))
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
                .withOutputFormats(Collections.singletonList(JobOutputFormat.HTML))
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
        new JobForm.Builder().withOutputFormats(null);
    }

    @Test
    public void form_new_builder() throws Exception {
        JobForm form = new JobForm.Builder()
                .withLabel("my label")
                .withRepositoryDestination(mDestination)
                .withJobSource(mSource)
                .withBaseOutputFilename("output")
                .withOutputFormats(Collections.singletonList(JobOutputFormat.HTML))
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