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

package com.jaspersoft.android.sdk.network.entity.schedule;

import com.google.gson.annotations.Expose;

import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.5
 */
public class JobMailNotificationEntity {
    @Expose
    private Set<String> toAddresses;
    @Expose
    private Set<String> ccAddresses;
    @Expose
    private Set<String> bccAddresses;
    @Expose
    private String subject;
    @Expose
    private String messageText;
    @Expose
    private String resultSendType;
    @Expose
    private Boolean skipEmptyReports;
    @Expose
    private String messageTextWhenJobFails;
    @Expose
    private Boolean includingStackTraceWhenJobFails;
    @Expose
    private Boolean skipNotificationWhenJobFails;

    public Set<String> getToAddresses() {
        return toAddresses;
    }

    public void setToAddresses(Set<String> toAddresses) {
        this.toAddresses = toAddresses;
    }

    public Set<String> getCcAddresses() {
        return ccAddresses;
    }

    public void setCcAddresses(Set<String> ccAddresses) {
        this.ccAddresses = ccAddresses;
    }

    public Set<String> getBccAddresses() {
        return bccAddresses;
    }

    public void setBccAddresses(Set<String> bccAddresses) {
        this.bccAddresses = bccAddresses;
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

    public String getResultSendType() {
        return resultSendType;
    }

    public void setResultSendType(String resultSendType) {
        this.resultSendType = resultSendType;
    }

    public Boolean getSkipEmptyReports() {
        return skipEmptyReports;
    }

    public void setSkipEmptyReports(Boolean skipEmptyReports) {
        this.skipEmptyReports = skipEmptyReports;
    }

    public String getMessageTextWhenJobFails() {
        return messageTextWhenJobFails;
    }

    public void setMessageTextWhenJobFails(String messageTextWhenJobFails) {
        this.messageTextWhenJobFails = messageTextWhenJobFails;
    }

    public Boolean getIncludingStackTraceWhenJobFails() {
        return includingStackTraceWhenJobFails;
    }

    public void setIncludingStackTraceWhenJobFails(Boolean includingStackTraceWhenJobFails) {
        this.includingStackTraceWhenJobFails = includingStackTraceWhenJobFails;
    }

    public Boolean getSkipNotificationWhenJobFails() {
        return skipNotificationWhenJobFails;
    }

    public void setSkipNotificationWhenJobFails(Boolean skipNotificationWhenJobFails) {
        this.skipNotificationWhenJobFails = skipNotificationWhenJobFails;
    }
}
