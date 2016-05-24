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

/**
 * @author Tom Koptel
 * @since 2.5
 */
class JobMailNotificationMapper extends JobMapper {
    static final JobMailNotificationMapper INSTANCE = new JobMailNotificationMapper();

    @Override
    public void mapFormOnEntity(JobForm form, JobFormEntity entity) {
        JobMailNotification formMailNotification = form.getMailNotification();

        if (formMailNotification != null) {
            JobMailNotificationEntity notification = new JobMailNotificationEntity();

            notification.setVersion(formMailNotification.getVersion());
            notification.setToAddresses(formMailNotification.getRecipients());
            notification.setCcAddresses(formMailNotification.getCcRecipients());
            notification.setBccAddresses(formMailNotification.getBccRecipients());
            notification.setSubject(formMailNotification.getSubject());
            notification.setMessageText(formMailNotification.getMessageText());

            JobMailNotification.Type sendType = formMailNotification.getResultSendType();
            if (sendType != null) {
                notification.setResultSendType(sendType.name());
            }

            notification.setSkipNotificationWhenJobFails(formMailNotification.getSkipNotificationWhenJobFails());
            notification.setIncludingStackTraceWhenJobFails(formMailNotification.getIncludeStackTraceWhenJobFails());
            notification.setSkipEmptyReports(formMailNotification.getSkipEmptyReports());
            notification.setMessageTextWhenJobFails(formMailNotification.getMessageTextWhenJobFails());

            entity.setMailNotification(notification);
        }
    }

    @Override
    public void mapEntityOnForm(JobForm.Builder form, JobFormEntity entity) {
        JobMailNotificationEntity notification = entity.getMailNotification();

        if (notification != null) {
            JobMailNotification.Builder notificationBuilder = new JobMailNotification.Builder();

            notificationBuilder.withVersion(notification.getVersion());
            notificationBuilder.withRecipients(notification.getToAddresses());
            notificationBuilder.withCcRecipients(notification.getCcAddresses());
            notificationBuilder.withBccRecipients(notification.getBccAddresses());
            notificationBuilder.withSubject(notification.getSubject());
            notificationBuilder.withMessageText(notification.getMessageText());

            String type = notification.getResultSendType();
            if (type != null) {
                JobMailNotification.Type sendType = JobMailNotification.Type.valueOf(type);
                notificationBuilder.withResultSendType(sendType);
            }

            notificationBuilder.withSkipNotificationWhenJobFails(notification.getSkipNotificationWhenJobFails());
            notificationBuilder.withIncludeStackTraceWhenJobFails(notification.getIncludingStackTraceWhenJobFails());
            notificationBuilder.withSkipEmptyReports(notification.getSkipEmptyReports());
            notificationBuilder.withMessageTextWhenJobFails(notification.getMessageTextWhenJobFails());

            form.withMailNotification(notificationBuilder.build());
        }
    }
}
