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
import com.jaspersoft.android.sdk.network.entity.schedule.JobTriggerEntity;
import com.jaspersoft.android.sdk.service.data.schedule.JobForm;
import com.jaspersoft.android.sdk.service.data.schedule.UntilEndDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author Tom Koptel
 * @since 2.5
 */
abstract class BaseTriggerMapper extends JobMapper {
    private static final String FORMAT_PATTERN = "yyyy-MM-dd HH:mm";
    private static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat(FORMAT_PATTERN, Locale.getDefault());

    protected void mapCommonTriggerFieldsOnEntity(JobForm form, JobTriggerEntity triggerEntity) {
        Date startDate = form.getStartDate();
        if (startDate == null) {
            triggerEntity.setStartType(1);
        } else {
            triggerEntity.setStartDate(getCommandDateFormat().format(startDate));
            triggerEntity.setStartType(2);
        }
        TimeZone timeZone = form.getTimeZone();
        if (timeZone == null) {
            timeZone = TimeZone.getDefault();
        }
        triggerEntity.setTimezone(timeZone.getID());
    }

    protected void mapCommonEntityTriggerFieldsOnEntity(JobForm.Builder form, JobFormEntity entity) {
        JobTriggerEntity triggerEntity = entity.getTrigger();
        String startDate = triggerEntity.getStartDate();
        if (startDate != null) {
            Date date = parseDate(startDate);
            if (date != null) {
                form.withStartDate(date);
            }
        }

        String timezone = triggerEntity.getTimezone();
        if (timezone != null) {
            TimeZone timeZone = TimeZone.getTimeZone(timezone);
            form.withTimeZone(timeZone);
        }
    }

    protected void mapEndDate(UntilEndDate endDate, JobTriggerEntity triggerEntity) {
        UntilEndDate date = endDate;

        Date specifiedDate = date.getSpecifiedDate();
        String untilDate = getCommandDateFormat().format(specifiedDate);

        triggerEntity.setEndDate(untilDate);
    }

    protected Date parseDate(String target) {
        try {
            return DATE_FORMAT.parse(target);
        } catch (ParseException e) {
            return null;
        }
    }

    protected SimpleDateFormat getCommandDateFormat() {
        return DATE_FORMAT;
    }
}
