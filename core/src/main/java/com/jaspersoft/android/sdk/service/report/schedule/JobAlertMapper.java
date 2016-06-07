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

/**
 * @author Tom Koptel
 * @since 2.3
 */
class JobAlertMapper extends JobMapper {
    static JobAlertMapper INSTANCE = new JobAlertMapper();


    JobAlertMapper() {
    }

    @Override
    public void mapFormOnEntity(JobForm form, JobFormEntity entity) {
        JobAlert jobAlert = form.getJobAlert();
        if (jobAlert != null) {
            JobAlertEntity alertEntity = new JobAlertEntity();

            JobAlert.RecipientType type = jobAlert.getRecipientType();
            if (type != null) {
                alertEntity.setRecipientType(type.name());
            }

            JobAlert.JobState jobState = jobAlert.getJobState();
            if (jobState != null) {
                alertEntity.setJobState(jobState.name());
            }

            alertEntity.setVersion(jobAlert.getVersion());
            alertEntity.setMessageText(jobAlert.getMessageText());
            alertEntity.setRecipients(jobAlert.getRecipients());
            alertEntity.setSubject(jobAlert.getSubject());
            alertEntity.setMessageText(jobAlert.getMessageText());
            alertEntity.setMessageTextWhenJobFails(jobAlert.getMessageTextWhenJobFails());
            alertEntity.setIncludeReportJobInfo(jobAlert.getIncludeReportJobInfo());
            alertEntity.setIncludeStackTrace(jobAlert.getIncludeStackTrace());

            entity.setAlert(alertEntity);
        }
    }

    @Override
    public void mapEntityOnForm(JobForm.Builder form, JobFormEntity entity) {
        JobAlertEntity alert = entity.getAlert();

        if (alert != null) {
            JobAlert.Builder alertBuilder = new JobAlert.Builder();

            String recipientType = alert.getRecipientType();
            if (recipientType != null) {
                alertBuilder.withRecipientType(JobAlert.RecipientType.valueOf(recipientType));
            }

            String jobState = alert.getJobState();
            if (jobState != null) {
                alertBuilder.withJobState(JobAlert.JobState.valueOf(jobState));
            }

            alertBuilder.withVersion(alert.getVersion());
            alertBuilder.withSubject(alert.getSubject());
            alertBuilder.withMessageText(alert.getMessageText());
            alertBuilder.withMessageTextWhenJobFails(alert.getMessageTextWhenJobFails());
            alertBuilder.withRecipients(alert.getRecipients());
            alertBuilder.withIncludeReportJobInfo(alert.getIncludeReportJobInfo());
            alertBuilder.withIncludeStackTrace(alert.getIncludeStackTrace());

            form.withJobAlert(alertBuilder.build());
        }
    }
}
