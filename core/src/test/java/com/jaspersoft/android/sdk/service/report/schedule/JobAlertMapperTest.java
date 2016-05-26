/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile for Android.
 *
 * TIBCO Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.schedule.JobAlertEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.JobFormEntity;
import com.jaspersoft.android.sdk.service.data.schedule.JobAlert;
import com.jaspersoft.android.sdk.service.data.schedule.JobForm;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class JobAlertMapperTest {

    private final JobFormFactory formFactory = new JobFormFactory();

    private JobAlert mappedAlert;
    private JobAlertEntity mappedAlertEntity;
    private JobAlertMapper mapperUnderTest;
    private JobFormEntity networkForm;
    private JobForm.Builder serviceFormBuilder;
    private JobForm serviceForm;

    @Before
    public void setUp() throws Exception {
        mapperUnderTest = new JobAlertMapper();

        networkForm = formFactory.givenNewJobFormEntity();
        JobAlertEntity alert = new JobAlertEntity();
        alert.setSubject("foo-bar");
        networkForm.setAlert(alert);

        serviceFormBuilder = formFactory.givenJobFormBuilderWithValues();
    }

    @Test
    public void should_map_recipient_type_from_form_to_entity() throws Exception {
        givenFormWithRecipientType(JobAlert.RecipientType.OWNER_AND_ADMIN);

        whenMapsFormToEntity();

        assertThat(mappedAlertEntity.getRecipientType(), is("OWNER_AND_ADMIN"));
    }

    @Test
    public void should_map_recipient_type_from_entity_to_form() throws Exception {
        givenEntityWithRecipientType("OWNER_AND_ADMIN");

        whenMapsEntityToForm();

        assertThat(mappedAlert.getRecipientType(), is(JobAlert.RecipientType.OWNER_AND_ADMIN));
    }

    @Test
    public void should_map_job_state_from_form_to_entity() throws Exception {
        givenFormWithJobState(JobAlert.JobState.ALL);

        whenMapsFormToEntity();

        assertThat(mappedAlertEntity.getJobState(), is("ALL"));
    }

    @Test
    public void should_map_job_state_from_entity_to_form() throws Exception {
        givenEntityWithJobState("ALL");

        whenMapsEntityToForm();

        assertThat(mappedAlert.getJobState(), is(JobAlert.JobState.ALL));
    }

    @Test
    public void should_map_version_from_form_to_entity() throws Exception {
        givenFormWithVersion(200);

        whenMapsFormToEntity();

        assertThat(mappedAlertEntity.getVersion(), is(200));
    }

    @Test
    public void should_map_version_from_entity_to_form() throws Exception {
        givenEntityWithVersion(200);

        whenMapsEntityToForm();

        assertThat(mappedAlert.getVersion(), is(200));
    }

    @Test
    public void should_map_recipients_from_form_to_entity() throws Exception {
        givenFormWithRecipients(Collections.singleton("a@a.com"));

        whenMapsFormToEntity();

        assertThat(mappedAlertEntity.getRecipients(), hasItem("a@a.com"));
    }

    @Test
    public void should_map_recipients_from_entity_to_form() throws Exception {
        givenEntityWithRecipients(Collections.singleton("a@a.com"));

        whenMapsEntityToForm();

        assertThat(mappedAlert.getRecipients(), hasItem("a@a.com"));
    }

    @Test
    public void should_map_subject_from_form_to_entity() throws Exception {
        givenFormWithSubject("Subject");

        whenMapsFormToEntity();

        assertThat(mappedAlertEntity.getSubject(), is("Subject"));
    }

    @Test
    public void should_map_subject_from_entity_to_form() throws Exception {
        givenEntityWithSubject("Subject");

        whenMapsEntityToForm();

        assertThat(mappedAlert.getSubject(), is("Subject"));
    }

    @Test
    public void should_map_message_text_from_form_to_entity() throws Exception {
        givenFormWithMessage("message");

        whenMapsFormToEntity();

        assertThat(mappedAlertEntity.getMessageText(), is("message"));
    }

    @Test
    public void should_map_message_text_from_entity_to_form() throws Exception {
        givenEntityWithMessage("message");

        whenMapsEntityToForm();

        assertThat(mappedAlert.getMessageText(), is("message"));
    }


    @Test
    public void should_map_include_stacktrace_from_form_to_entity() throws Exception {
        givenFormWithIncludeStackTrace(true);

        whenMapsFormToEntity();

        assertThat(mappedAlertEntity.getIncludeStackTrace(), is(true));
    }

    @Test
    public void should_map_include_stacktrace_from_entity_to_form() throws Exception {
        givenEntityWithIncludeStackTrace(true);

        whenMapsEntityToForm();

        assertThat(mappedAlert.getIncludeStackTrace(), is(true));
    }

    @Test
    public void should_map_include_report_job_info_from_form_to_entity() throws Exception {
        givenFormWithIncludeReportJobInfo(true);

        whenMapsFormToEntity();

        assertThat(mappedAlertEntity.getIncludeReportJobInfo(), is(true));
    }

    @Test
    public void should_map_include_report_job_info_from_entity_to_form() throws Exception {
        givenEntityWithIncludeReportJobInfo(true);

        whenMapsEntityToForm();

        assertThat(mappedAlert.getIncludeReportJobInfo(), is(true));
    }

    @Test
    public void should_map_text_when_job_fails_from_form_to_entity() throws Exception {
        givenFormWithMessageTextWhenJobFails("failed");

        whenMapsFormToEntity();

        assertThat(mappedAlertEntity.getMessageTextWhenJobFails(), is("failed"));
    }

    @Test
    public void should_map_text_when_job_fails_from_entity_to_form() throws Exception {
        givenEntityWithMessageTextWhenJobFails("failed");

        whenMapsEntityToForm();

        assertThat(mappedAlert.getMessageTextWhenJobFails(), is("failed"));
    }

    private void givenFormWithRecipientType(JobAlert.RecipientType type) {
        JobAlert.Builder alert = defaultAlertForm()
                .withRecipientType(type);
        createForm(alert);
    }

    private void givenEntityWithRecipientType(String type) {
        networkForm.getAlert().setRecipientType(type);
    }

    private void givenFormWithJobState(JobAlert.JobState jobState) {
        JobAlert.Builder alert = defaultAlertForm()
                .withJobState(jobState);
        createForm(alert);
    }

    private void givenEntityWithJobState(String jobState) {
        networkForm.getAlert().setJobState(jobState);
    }

    private void givenFormWithVersion(int version) {
        JobAlert.Builder alert = defaultAlertForm()
                .withVersion(version);
        createForm(alert);
    }

    private void givenEntityWithVersion(int code) {
        networkForm.getAlert().setVersion(code);
    }

    private void givenFormWithRecipients(Set<String> emails) {
        JobAlert.Builder alert = defaultAlertForm()
                .withRecipients(emails);
        createForm(alert);
    }

    private void givenEntityWithRecipients(Set<String> emails) {
        networkForm.getAlert().setRecipients(emails);
    }

    private void givenFormWithSubject(String subject) {
        JobAlert.Builder alert = defaultAlertForm()
                .withSubject(subject);
        createForm(alert);
    }

    private void givenEntityWithSubject(String subject) {
        networkForm.getAlert().setSubject(subject);
    }

    private void givenFormWithMessage(String message) {
        JobAlert.Builder alert = defaultAlertForm()
                .withMessageText(message);
        createForm(alert);
    }

    private void givenEntityWithMessage(String message) {
        networkForm.getAlert().setMessageText(message);
    }

    private void givenFormWithIncludeStackTrace(boolean flag) {
        JobAlert.Builder alert = defaultAlertForm()
                .withIncludeStackTrace(flag);
        createForm(alert);
    }

    private void givenEntityWithIncludeStackTrace(boolean flag) {
        networkForm.getAlert().setIncludeStackTrace(flag);
    }

    private void givenFormWithIncludeReportJobInfo(boolean flag) {
        JobAlert.Builder alert = defaultAlertForm()
                .withIncludeReportJobInfo(flag);
        createForm(alert);
    }

    private void givenEntityWithIncludeReportJobInfo(boolean flag) {
        networkForm.getAlert().setIncludeReportJobInfo(flag);
    }

    private void givenFormWithMessageTextWhenJobFails(String message) {
        JobAlert.Builder alert = defaultAlertForm()
                .withMessageTextWhenJobFails(message);
        createForm(alert);
    }

    private void givenEntityWithMessageTextWhenJobFails(String message) {
        networkForm.getAlert().setMessageTextWhenJobFails(message);
    }

    private void createForm(JobAlert.Builder jobAlert) {
        serviceForm = formFactory.givenJobFormBuilderWithValues()
                .withJobAlert(
                        jobAlert.build()
                )
                .build();
    }

    private JobAlert.Builder defaultAlertForm() {
        return new JobAlert.Builder().withSubject("foo bar");
    }

    private void whenMapsFormToEntity() {
        mapperUnderTest.mapFormOnEntity(serviceForm, networkForm);
        mappedAlertEntity = networkForm.getAlert();
    }

    private void whenMapsEntityToForm() {
        mapperUnderTest.mapEntityOnForm(serviceFormBuilder, networkForm);
        JobForm mappedServiceForm = serviceFormBuilder.build();
        mappedAlert = mappedServiceForm.getJobAlert();
    }
}