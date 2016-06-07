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

package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.schedule.JobFormEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.JobMailNotificationEntity;
import com.jaspersoft.android.sdk.service.data.schedule.JobForm;
import com.jaspersoft.android.sdk.service.data.schedule.JobMailNotification;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

public class JobMailNotificationMapperTest {

    private final JobFormFactory formFactory = new JobFormFactory();

    private JobMailNotification mappedNotification;
    private JobMailNotificationEntity mappedNotificationEntity;
    private JobMailNotificationMapper mapperUnderTest;
    private JobFormEntity networkForm;
    private JobForm.Builder serviceFormBuilder;
    private JobForm serviceForm;

    @Before
    public void setUp() throws Exception {
        mapperUnderTest = new JobMailNotificationMapper();

        networkForm = formFactory.givenNewJobFormEntity();
        JobMailNotificationEntity mailNotification = new JobMailNotificationEntity();
        mailNotification.setSubject("foo-bar");
        networkForm.setMailNotification(mailNotification);

        serviceFormBuilder = formFactory.givenJobFormBuilderWithValues();
    }

    @Test
    public void should_map_version_from_form_to_entity() throws Exception {
        givenFormWithVersion(200);

        whenMapsFormToEntity();

        assertThat(mappedNotificationEntity.getVersion(), is(200));
    }

    @Test
    public void should_map_version_from_entity_to_form() throws Exception {
        givenEntityWithVersion(200);

        whenMapsEntityToForm();

        assertThat(mappedNotification.getVersion(), is(200));
    }

    @Test
    public void should_map_recipients_from_form_to_entity() throws Exception {
        givenFormWithRecipients(Collections.singleton("a@a.com"));

        whenMapsFormToEntity();

        assertThat(mappedNotificationEntity.getToAddresses(), hasItem("a@a.com"));
    }

    @Test
    public void should_map_recipients_from_entity_to_form() throws Exception {
        givenEntityWithRecipients(Collections.singleton("a@a.com"));

        whenMapsEntityToForm();

        assertThat(mappedNotification.getRecipients(), hasItem("a@a.com"));
    }

    @Test
    public void should_map_cc_recipients_from_form_to_entity() throws Exception {
        givenFormWithCcRecipients(Collections.singleton("cca@a.com"));

        whenMapsFormToEntity();

        assertThat(mappedNotificationEntity.getCcAddresses(), hasItem("cca@a.com"));
    }

    @Test
    public void should_map_cc_recipients_from_entity_to_form() throws Exception {
        givenEntityWithCcRecipients(Collections.singleton("cca@a.com"));

        whenMapsEntityToForm();

        assertThat(mappedNotification.getCcRecipients(), hasItem("cca@a.com"));
    }

    @Test
    public void should_map_bcc_recipients_from_form_to_entity() throws Exception {
        givenFormWithBccRecipients(Collections.singleton("bcca@a.com"));

        whenMapsFormToEntity();

        assertThat(mappedNotificationEntity.getBccAddresses(), hasItem("bcca@a.com"));
    }

    @Test
    public void should_map_bcc_recipients_from_entity_to_form() throws Exception {
        givenEntityWithBccRecipients(Collections.singleton("a@a.com"));

        whenMapsEntityToForm();

        assertThat(mappedNotification.getBccRecipients(), hasItem("a@a.com"));
    }

    @Test
    public void should_map_subject_from_form_to_entity() throws Exception {
        givenFormWithSubject("Subject");

        whenMapsFormToEntity();

        assertThat(mappedNotificationEntity.getSubject(), is("Subject"));
    }

    @Test
    public void should_map_subject_from_entity_to_form() throws Exception {
        givenEntityWithSubject("Subject");

        whenMapsEntityToForm();

        assertThat(mappedNotification.getSubject(), is("Subject"));
    }

    @Test
    public void should_map_message_text_from_form_to_entity() throws Exception {
        givenFormWithMessage("message");

        whenMapsFormToEntity();

        assertThat(mappedNotificationEntity.getMessageText(), is("message"));
    }

    @Test
    public void should_map_message_text_from_entity_to_form() throws Exception {
        givenEntityWithMessage("message");

        whenMapsEntityToForm();

        assertThat(mappedNotification.getMessageText(), is("message"));
    }

    @Test
    public void should_map_result_send_type_from_form_to_entity() throws Exception {
        givenFormWithResultSendType(JobMailNotification.Type.SEND_ATTACHMENT);

        whenMapsFormToEntity();

        assertThat(mappedNotificationEntity.getResultSendType(), is("SEND_ATTACHMENT"));
    }

    @Test
    public void should_map_result_send_type_from_entity_to_form() throws Exception {
        givenEntityWithResultSendType("SEND_ATTACHMENT");

        whenMapsEntityToForm();

        assertThat(mappedNotification.getResultSendType(), is(JobMailNotification.Type.SEND_ATTACHMENT));
    }

    @Test
    public void should_map_skip_empty_reports_from_form_to_entity() throws Exception {
        givenFormWithSkipEmptyReports(true);

        whenMapsFormToEntity();

        assertThat(mappedNotificationEntity.getSkipEmptyReports(), is(true));
    }

    @Test
    public void should_map_skip_empty_reports_from_entity_to_form() throws Exception {
        givenEntityWithSkipEmptyReports(true);

        whenMapsEntityToForm();

        assertThat(mappedNotification.getSkipEmptyReports(), is(true));
    }

    @Test
    public void should_map_include_stacktrace_from_form_to_entity() throws Exception {
        givenFormWithStackTraceWhenJobFails(true);

        whenMapsFormToEntity();

        assertThat(mappedNotificationEntity.getIncludingStackTraceWhenJobFails(), is(true));
    }

    @Test
    public void should_map_include_stacktrace_from_entity_to_form() throws Exception {
        givenEntityWithStackTraceWhenJobFails(true);

        whenMapsEntityToForm();

        assertThat(mappedNotification.getIncludeStackTrace(), is(true));
    }

    @Test
    public void should_map_skip_notification_from_form_to_entity() throws Exception {
        givenFormWithSkipNotificationWhenJobFails(true);

        whenMapsFormToEntity();

        assertThat(mappedNotificationEntity.getSkipNotificationWhenJobFails(), is(true));
    }

    @Test
    public void should_map_skip_notification_from_entity_to_form() throws Exception {
        givenEntityWithSkipNotificationWhenJobFails(true);

        whenMapsEntityToForm();

        assertThat(mappedNotification.getSkipNotification(), is(true));
    }

    @Test
    public void should_map_text_when_job_fails_from_form_to_entity() throws Exception {
        givenFormWithMessageTextWhenJobFails("failed");

        whenMapsFormToEntity();

        assertThat(mappedNotificationEntity.getMessageTextWhenJobFails(), is("failed"));
    }

    @Test
    public void should_map_text_when_job_fails_from_entity_to_form() throws Exception {
        givenEntityWithMessageTextWhenJobFails("failed");

        whenMapsEntityToForm();

        assertThat(mappedNotification.getMessageTextWhenJobFails(), is("failed"));
    }

    private void givenFormWithVersion(int version) {
        JobMailNotification mailNotification = createMailNotificationBuilder()
                .withVersion(version)
                .build();
        createForm(mailNotification);
    }

    private void givenEntityWithVersion(int version) {
        networkForm.getMailNotification().setVersion(version);
    }

    private void givenFormWithRecipients(Set<String> recipients) {
        JobMailNotification mailNotification = createMailNotificationBuilder()
                .withRecipients(recipients)
                .build();
        createForm(mailNotification);
    }

    private void givenEntityWithRecipients(Set<String> recipients) {
        networkForm.getMailNotification().setToAddresses(recipients);
    }

    private void givenFormWithCcRecipients(Set<String> recipients) {
        JobMailNotification mailNotification = createMailNotificationBuilder()
                .withCcRecipients(recipients)
                .build();
        createForm(mailNotification);
    }

    private void givenEntityWithCcRecipients(Set<String> recipients) {
        networkForm.getMailNotification().setCcAddresses(recipients);
    }

    private void givenFormWithBccRecipients(Set<String> recipients) {
        JobMailNotification mailNotification = createMailNotificationBuilder()
                .withBccRecipients(recipients)
                .build();
        createForm(mailNotification);
    }

    private void givenEntityWithBccRecipients(Set<String> recipients) {
        networkForm.getMailNotification().setBccAddresses(recipients);
    }

    private void givenFormWithSubject(String subject) {
        JobMailNotification mailNotification = createMailNotificationBuilder()
                .withSubject(subject)
                .build();
        createForm(mailNotification);
    }

    private void givenEntityWithSubject(String subject) {
        networkForm.getMailNotification().setSubject(subject);
    }

    private void givenFormWithMessage(String message) {
        JobMailNotification mailNotification = createMailNotificationBuilder()
                .withMessageText(message)
                .build();
        createForm(mailNotification);
    }

    private void givenEntityWithMessage(String message) {
        networkForm.getMailNotification().setMessageText(message);
    }

    private void givenFormWithResultSendType(JobMailNotification.Type type) {
        JobMailNotification mailNotification = createMailNotificationBuilder()
                .withResultSendType(type)
                .build();
        createForm(mailNotification);
    }

    private void givenEntityWithResultSendType(String type) {
        networkForm.getMailNotification().setResultSendType(type);
    }

    private void givenFormWithSkipEmptyReports(boolean flag) {
        JobMailNotification mailNotification = createMailNotificationBuilder()
                .withSkipEmptyReports(flag)
                .build();
        createForm(mailNotification);
    }

    private void givenEntityWithSkipEmptyReports(boolean flag) {
        networkForm.getMailNotification().setSkipEmptyReports(flag);
    }

    private void givenFormWithStackTraceWhenJobFails(boolean flag) {
        JobMailNotification mailNotification = createMailNotificationBuilder()
                .withIncludeStackTrace(flag)
                .build();
        createForm(mailNotification);
    }

    private void givenEntityWithStackTraceWhenJobFails(boolean flag) {
        networkForm.getMailNotification().setIncludingStackTraceWhenJobFails(flag);
    }

    private void givenFormWithSkipNotificationWhenJobFails(boolean flag) {
        JobMailNotification mailNotification = createMailNotificationBuilder()
                .withSkipNotification(flag)
                .build();
        createForm(mailNotification);
    }

    private JobMailNotification.Builder createMailNotificationBuilder() {
        return new JobMailNotification.Builder()
                .withSubject("foo-bar");
    }

    private void givenEntityWithSkipNotificationWhenJobFails(boolean flag) {
        networkForm.getMailNotification().setSkipNotificationWhenJobFails(flag);
    }

    private void givenFormWithMessageTextWhenJobFails(String message) {
        JobMailNotification mailNotification = createMailNotificationBuilder()
                .withMessageTextWhenJobFails(message)
                .build();
        createForm(mailNotification);
    }

    private void givenEntityWithMessageTextWhenJobFails(String message) {
        networkForm.getMailNotification().setMessageTextWhenJobFails(message);
    }

    private void createForm(JobMailNotification mailNotification) {
        serviceForm = formFactory.givenJobFormBuilderWithValues()
                .withMailNotification(mailNotification)
                .build();
    }

    private void whenMapsFormToEntity() {
        mapperUnderTest.mapFormOnEntity(serviceForm, networkForm);
        mappedNotificationEntity = networkForm.getMailNotification();
    }

    private void whenMapsEntityToForm() {
        mapperUnderTest.mapEntityOnForm(serviceFormBuilder, networkForm);
        JobForm mappedServiceForm = serviceFormBuilder.build();
        mappedNotification = mappedServiceForm.getMailNotification();
    }
}