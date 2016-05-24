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

import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.5
 */
public class JobMailNotification {
    private final Set<String> recipients;
    private final Set<String> ccRecipients;
    private final Set<String> bccRecipients;
    private final String subject;
    private final String messageText;
    private final Type resultSendType;
    private final Boolean skipEmptyReports;
    private final String messageTextWhenJobFails;
    private final Boolean includeStackTraceWhenJobFails;
    private final Boolean skipNotificationWhenJobFails;

    private JobMailNotification(Builder builder) {
        this.recipients = builder.recipients;
        this.ccRecipients = builder.ccAddresses;
        this.bccRecipients = builder.bccAddresses;
        this.subject = builder.subject;
        this.messageText = builder.messageText;
        this.resultSendType = builder.resultSendType;
        this.skipEmptyReports = builder.skipEmptyReports;
        this.messageTextWhenJobFails = builder.messageTextWhenJobFails;
        this.includeStackTraceWhenJobFails = builder.includeStackTraceWhenJobFails;
        this.skipNotificationWhenJobFails = builder.skipNotificationWhenJobFails;
    }

    public JobMailNotification.Builder newBuilder() {
        return new Builder(this);
    }

    public enum Type {
        /**
         * The email notification should contain links to the job output generated in the repository
         */
        SEND,
        /**
         * The email notification should contain the job output as attachments
         */
        SEND_ATTACHMENT,
        /**
         * The email notification should contain the job output as non-zip format attachments
         */
        SEND_ATTACHMENT_NOZIP,
        /**
         * The email notification should embed the HTML job output in the email body
         */
        SEND_EMBED,
        /**
         * The email notification should contain the job output as attachments in one zip file
         */
        SEND_ATTACHMENT_ZIP_ALL,
        /**
         * The email notification should embed the HTML job output in the email body and put the other output type attachments
         */
        SEND_EMBED_ZIP_ALL_OTHERS,
    }

    public Set<String> getRecipients() {
        return recipients;
    }

    public Set<String> getCcRecipients() {
        return ccRecipients;
    }

    public Set<String> getBccRecipients() {
        return bccRecipients;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessageText() {
        return messageText;
    }

    public Type getResultSendType() {
        return resultSendType;
    }

    public Boolean getSkipEmptyReports() {
        return skipEmptyReports;
    }

    public String getMessageTextWhenJobFails() {
        return messageTextWhenJobFails;
    }

    public Boolean getIncludeStackTraceWhenJobFails() {
        return includeStackTraceWhenJobFails;
    }

    public Boolean getSkipNotificationWhenJobFails() {
        return skipNotificationWhenJobFails;
    }

    public static class Builder {

        private Set<String> recipients;
        private Set<String> ccAddresses;
        private Set<String> bccAddresses;
        private String subject;
        private String messageText;
        private Type resultSendType;
        private Boolean skipEmptyReports;
        private String messageTextWhenJobFails;
        private Boolean includeStackTraceWhenJobFails;
        private Boolean skipNotificationWhenJobFails;

        public Builder() {}

        private Builder(JobMailNotification builder) {
            this.recipients = builder.recipients;
            this.ccAddresses = builder.ccRecipients;
            this.bccAddresses = builder.bccRecipients;
            this.subject = builder.subject;
            this.messageText = builder.messageText;
            this.resultSendType = builder.resultSendType;
            this.skipEmptyReports = builder.skipEmptyReports;
            this.messageTextWhenJobFails = builder.messageTextWhenJobFails;
            this.includeStackTraceWhenJobFails = builder.includeStackTraceWhenJobFails;
            this.skipNotificationWhenJobFails = builder.skipNotificationWhenJobFails;
        }

        /**
         * The list of direct recipients of the email notification
         *
         * @param emails list of mails of the notification recipients
         * @return builder for convenient configuration
         */
        public Builder withRecipients(Set<String> emails) {
            this.recipients = emails;
            return this;
        }

        /**
         * The list of CC (Carbon copy) recipients of the email notification
         *
         * @param emails list of mails of the notification CC recipients
         * @return builder for convenient configuration
         */
        public Builder withCcRecipients(Set<String> emails) {
            this.ccAddresses = emails;
            return this;
        }

        /**
         * The list of Bcc (Blind carbon copy) recipients of the email notification
         *
         * @param emails list of mails of the notification Bcc recipients
         * @return builder for convenient configuration
         */
        public Builder withBccRecipients(Set<String> emails) {
            this.bccAddresses = emails;
            return this;
        }

        /**
         * The subject to be used for the email notification
         *
         * @param subject theme of notification
         * @return builder for convenient configuration
         */
        public Builder withSubject(String subject) {
            this.subject = subject;
            return this;
        }

        /**
         * The text of the email notification. At job execution time, links to the output and errors might get appended to the notification message text
         *
         * @param messageText can be any text that is supported by mail protocol
         * @return builder for convenient configuration
         */
        public Builder withMessageText(String messageText) {
            this.messageText = messageText;
            return this;
        }

        /**
         * Determines whether the notification would include the job output as attachments, or include links to the output in the repository.
         *
         * @param resultSendType tune up configuration of the job output process. Default: SEND
         * @return builder for convenient configuration
         */
        public Builder withResultSendType(Type resultSendType) {
            this.resultSendType = resultSendType;
            return this;
        }

        /**
         * Specifies whether the email notification should be skipped for job executions the produce empty reports.
         * An executed report is considered empty if it doesn't have any generated content.
         *
         * @param skipEmptyReports Supported values: true, false. Defalut: false
         * @return builder for convenient configuration
         */
        public Builder withSkipEmptyReports(Boolean skipEmptyReports) {
            this.skipEmptyReports = skipEmptyReports;
            return this;
        }

        /**
         * The text of the email notification when the job fails.
         * At job execution time, links to the output and errors might get appended to the notification message text.
         *
         * @param messageTextWhenJobFails can be any text that is supported by mail protocol
         * @return builder for convenient configuration
         */
        public Builder withMessageTextWhenJobFails(String messageTextWhenJobFails) {
            this.messageTextWhenJobFails = messageTextWhenJobFails;
            return this;
        }

        /**
         * Specifies whether the mail notification would include detail stack trace of exception.
         *
         * @param includingStackTraceWhenJobFails Supported values: true, false. Default: false
         * @return builder for convenient configuration
         */
        public Builder withIncludeStackTraceWhenJobFails(Boolean includingStackTraceWhenJobFails) {
            this.includeStackTraceWhenJobFails = includingStackTraceWhenJobFails;
            return this;
        }

        /**
         * Specifies whether the mail notification should send if job fails.
         *
         * @param skipNotificationWhenJobFails Supported values: true, false. Default: false
         * @return builder for convenient configuration
         */
        public Builder withSkipNotificationWhenJobFails(Boolean skipNotificationWhenJobFails) {
            this.skipNotificationWhenJobFails = skipNotificationWhenJobFails;
            return this;
        }

        public JobMailNotification build() {
            return new JobMailNotification(this);
        }
    }
}
