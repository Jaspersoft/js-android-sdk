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

package com.jaspersoft.android.sdk.service.data.schedule;

import com.jaspersoft.android.sdk.service.internal.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class JobAlert {
    private final Integer version;
    private final RecipientType recipientType;
    private final Set<String> recipients;
    private final JobState jobState;
    private final String subject;
    private final String messageText;
    private final String messageTextWhenJobFails;
    private final Boolean includeStackTrace;
    private final Boolean includeReportJobInfo;

    JobAlert(Builder builder) {
        this.version = builder.version;
        this.recipientType = builder.recipientType;
        this.recipients = builder.recipients;
        this.jobState = builder.jobState;
        this.subject = builder.subject;
        this.messageText = builder.messageText;
        this.messageTextWhenJobFails = builder.messageTextWhenJobFails;
        this.includeStackTrace = builder.includingStackTrace;
        this.includeReportJobInfo = builder.includingReportJobInfo;
    }

    public Integer getVersion() {
        return version;
    }

    public enum RecipientType {
        NONE, OWNER, OWNER_AND_ADMIN
    }

    @Nullable
    public RecipientType getRecipientType() {
        return recipientType;
    }

    @Nullable
    public Set<String> getRecipients() {
        return recipients;
    }

    @Nullable
    public JobState getJobState() {
        return jobState;
    }

    @NotNull
    public String getSubject() {
        return subject;
    }

    @Nullable
    public String getMessageText() {
        return messageText;
    }

    @Nullable
    public String getMessageTextWhenJobFails() {
        return messageTextWhenJobFails;
    }

    @Nullable
    public Boolean getIncludeStackTrace() {
        return includeStackTrace;
    }

    @Nullable
    public Boolean getIncludeReportJobInfo() {
        return includeReportJobInfo;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JobAlert jobAlert = (JobAlert) o;

        if (recipientType != jobAlert.recipientType) return false;
        if (recipients != null ? !recipients.equals(jobAlert.recipients) : jobAlert.recipients != null) return false;
        if (jobState != jobAlert.jobState) return false;
        if (subject != null ? !subject.equals(jobAlert.subject) : jobAlert.subject != null) return false;
        if (messageText != null ? !messageText.equals(jobAlert.messageText) : jobAlert.messageText != null)
            return false;
        if (messageTextWhenJobFails != null ? !messageTextWhenJobFails.equals(jobAlert.messageTextWhenJobFails) : jobAlert.messageTextWhenJobFails != null)
            return false;
        if (includeStackTrace != null ? !includeStackTrace.equals(jobAlert.includeStackTrace) : jobAlert.includeStackTrace != null)
            return false;
        if (includeReportJobInfo != null ? !includeReportJobInfo.equals(jobAlert.includeReportJobInfo) : jobAlert.includeReportJobInfo != null)
            return false;
        return !(version != null ? !version.equals(jobAlert.version) : jobAlert.version != null);
    }

    @Override
    public int hashCode() {
        int result = recipientType != null ? recipientType.hashCode() : 0;
        result = 31 * result + (recipients != null ? recipients.hashCode() : 0);
        result = 31 * result + (jobState != null ? jobState.hashCode() : 0);
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + (messageText != null ? messageText.hashCode() : 0);
        result = 31 * result + (messageTextWhenJobFails != null ? messageTextWhenJobFails.hashCode() : 0);
        result = 31 * result + (includeStackTrace != null ? includeStackTrace.hashCode() : 0);
        result = 31 * result + (includeReportJobInfo != null ? includeReportJobInfo.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }

    public static class Builder {

        private Integer version;
        private RecipientType recipientType;
        private Set<String> recipients;
        private JobState jobState;
        private String subject;
        private String messageText;
        private String messageTextWhenJobFails;
        private Boolean includingStackTrace;
        private Boolean includingReportJobInfo;

        public Builder() {
        }

        private Builder(JobAlert builder) {
            this.version = builder.version;
            this.recipientType = builder.recipientType;
            this.recipients = builder.recipients;
            this.jobState = builder.jobState;
            this.subject = builder.subject;
            this.messageText = builder.messageText;
            this.messageTextWhenJobFails = builder.messageTextWhenJobFails;
            this.includingStackTrace = builder.includeStackTrace;
            this.includingReportJobInfo = builder.includeReportJobInfo;
        }

        /**
         * Allows to specify version of form. One is required for update operations.
         *
         * @param version can be any whole number that represents current update
         * @return builder for convenient configuration
         */
        public Builder withVersion(Integer version) {
            this.version = version;
            return this;
        }

        /**
         * Specifies whether the alert would send it to owner, admin, none or both (admin and owner).
         *
         * @param recipientType Supported values: NONE, OWNER, ADMIN, OWNER_AND_ADMIN Default: OWNER_AND_ADMIN
         * @return builder for convenient configuration
         */
        public Builder withRecipientType(RecipientType recipientType) {
            this.recipientType = recipientType;
            return this;
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
         * Specifies whether the alert would send it when job fails, succeeds, none, or both (fail and success)
         *
         * @param jobState Supported values: NONE, ALL, FAIL_ONLY, SUCCESS_ONLY Default: FAIL_ONLY
         * @return builder for convenient configuration
         */
        public Builder withJobState(JobState jobState) {
            this.jobState = jobState;
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
         * @param includingStackTrace Supported values: true, false. Default: false
         * @return builder for convenient configuration
         */
        public Builder withIncludeStackTrace(Boolean includingStackTrace) {
            this.includingStackTrace = includingStackTrace;
            return this;
        }

        /**
         * Specifies whether the alert would include report job info.
         *
         * @param includingReportJobInfo Supported values: true, false.
         * @return builder for convenient configuration
         */
        public Builder withIncludeReportJobInfo(Boolean includingReportJobInfo) {
            this.includingReportJobInfo = includingReportJobInfo;
            return this;
        }

        public JobAlert build() {
            Preconditions.checkNotNull(subject, "subject should not be null");
            return new JobAlert(this);
        }
    }
}
