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

package com.jaspersoft.android.sdk.network.entity.schedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class JobAlertEntity {
    @Expose
    private String recipientType;
    @Expose
    private Address recipients;
    @Expose
    private String jobState;
    @Expose
    private String subject;
    @Expose
    private String messageText;
    @Expose
    private String messageTextWhenJobFails;
    @Expose
    @SerializedName("includingStackTrace")
    private Boolean includeStackTrace;
    @Expose
    @SerializedName("includingReportJobInfo")
    private Boolean includeReportJobInfo;

    public String getRecipientType() {
        return recipientType;
    }

    public void setRecipientType(String recipientType) {
        this.recipientType = recipientType;
    }

    public Address getRecipients() {
        return recipients;
    }

    public void setRecipients(Address recipients) {
        this.recipients = recipients;
    }

    public String getJobState() {
        return jobState;
    }

    public void setJobState(String jobState) {
        this.jobState = jobState;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageTextWhenJobFails() {
        return messageTextWhenJobFails;
    }

    public void setMessageTextWhenJobFails(String messageTextWhenJobFails) {
        this.messageTextWhenJobFails = messageTextWhenJobFails;
    }

    public Boolean getIncludeStackTrace() {
        return includeStackTrace;
    }

    public void setIncludeStackTrace(Boolean includeStackTrace) {
        this.includeStackTrace = includeStackTrace;
    }

    public Boolean getIncludeReportJobInfo() {
        return includeReportJobInfo;
    }

    public void setIncludeReportJobInfo(Boolean includeReportJobInfo) {
        this.includeReportJobInfo = includeReportJobInfo;
    }
}
