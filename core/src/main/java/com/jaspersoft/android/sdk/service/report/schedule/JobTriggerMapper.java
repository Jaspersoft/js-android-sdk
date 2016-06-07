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
import com.jaspersoft.android.sdk.network.entity.schedule.JobSimpleTriggerEntity;
import com.jaspersoft.android.sdk.service.data.schedule.CalendarRecurrence;
import com.jaspersoft.android.sdk.service.data.schedule.JobForm;
import com.jaspersoft.android.sdk.service.data.schedule.Recurrence;
import com.jaspersoft.android.sdk.service.data.schedule.Trigger;

/**
 * @author Tom Koptel
 * @since 2.5
 */
class JobTriggerMapper extends JobMapper {
    final static JobTriggerMapper INSTANCE = new JobTriggerMapper(
            JobCalendarTriggerMapper.INSTANCE,
            JobSimpleTriggerMapper.INSTANCE,
            JobNoneTriggerMapper.INSTANCE);

    private final JobCalendarTriggerMapper mCalendarTriggerMapper;
    private final JobSimpleTriggerMapper mSimpleTriggerMapper;
    private final JobNoneTriggerMapper mNoneTriggerMapper;

    JobTriggerMapper(
            JobCalendarTriggerMapper calendarTriggerMapper,
            JobSimpleTriggerMapper simpleTriggerMapper,
            JobNoneTriggerMapper noneTriggerMapper
    ) {
        mCalendarTriggerMapper = calendarTriggerMapper;
        mSimpleTriggerMapper = simpleTriggerMapper;
        mNoneTriggerMapper = noneTriggerMapper;
    }

    @Override
    public void mapFormOnEntity(JobForm form, JobFormEntity entity) {
        Trigger trigger = form.getTrigger();

        if (trigger == null) {
            mNoneTriggerMapper.mapFormOnEntity(form, entity);
        } else {
            Recurrence recurrence = trigger.getRecurrence();

            if (recurrence instanceof CalendarRecurrence) {
                mCalendarTriggerMapper.mapFormOnEntity(form, entity);
            } else {
                mSimpleTriggerMapper.mapFormOnEntity(form, entity);
            }
        }
    }

    @Override
    public void mapEntityOnForm(JobForm.Builder form, JobFormEntity entity) {
        JobSimpleTriggerEntity simpleTrigger = entity.getSimpleTrigger();

        if (simpleTrigger == null) {
            mCalendarTriggerMapper.mapEntityOnForm(form, entity);
        } else {
            Integer recurrenceInterval = simpleTrigger.getRecurrenceInterval();
            String recurrenceIntervalUnit = simpleTrigger.getRecurrenceIntervalUnit();

            boolean triggerIsSimpleType =
                    (recurrenceInterval != null && recurrenceIntervalUnit != null);

            if (triggerIsSimpleType) {
                mSimpleTriggerMapper.mapEntityOnForm(form, entity);
            } else {
                mNoneTriggerMapper.mapEntityOnForm(form, entity);
            }
        }
    }
}
