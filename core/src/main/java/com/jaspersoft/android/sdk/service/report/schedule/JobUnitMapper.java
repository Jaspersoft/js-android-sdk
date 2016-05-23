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

import com.jaspersoft.android.sdk.network.entity.schedule.JobStateEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.JobUnitEntity;
import com.jaspersoft.android.sdk.service.data.schedule.JobOwner;
import com.jaspersoft.android.sdk.service.data.schedule.JobState;
import com.jaspersoft.android.sdk.service.data.schedule.JobUnit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class JobUnitMapper {

    private final JobUnitDateParser mJobUnitDateParser;

    JobUnitMapper(JobUnitDateParser jobUnitDateParser) {
        mJobUnitDateParser = jobUnitDateParser;
    }

    public List<JobUnit> transform(List<JobUnitEntity> entities) {
        List<JobUnit> list = new ArrayList<>(entities.size());
        for (JobUnitEntity entity : entities) {
            if (entity != null) {
                JobUnit unit = transform(entity);
                list.add(unit);
            }
        }
        return list;
    }

    public JobUnit transform(JobUnitEntity entity) {
        JobUnit.Builder jobUnitBuilder = new JobUnit.Builder();
        jobUnitBuilder.withId(entity.getId());
        jobUnitBuilder.withVersion(entity.getVersion());
        jobUnitBuilder.withLabel(entity.getLabel());
        jobUnitBuilder.withReportLabel(entity.getReportLabel());
        jobUnitBuilder.withReportUri(entity.getReportUnitURI());
        jobUnitBuilder.withDescription(entity.getDescription());

        JobOwner owner = JobOwner.newOwner(entity.getOwner());
        jobUnitBuilder.withOwner(owner);

        JobStateEntity entityState = entity.getState();
        if (entityState != null) {
            String value = entityState.getValue();
            JobState state = JobState.valueOf(value);
            jobUnitBuilder.withState(state);

            String nextFireTime = entityState.getNextFireTime();
            if (nextFireTime != null) {
                Date nextFireDate = mJobUnitDateParser.parseDate(nextFireTime);
                jobUnitBuilder.withNextFireTime(nextFireDate);
            }

            String previousFireTime = entityState.getPreviousFireTime();
            if (previousFireTime != null) {
                Date previousFireDate = mJobUnitDateParser.parseDate(previousFireTime);
                jobUnitBuilder.withPreviousFireTime(previousFireDate);
            }
        }

        return jobUnitBuilder.build();
    }
}
