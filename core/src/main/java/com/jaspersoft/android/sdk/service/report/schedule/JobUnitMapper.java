package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.schedule.JobStateEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.JobUnitEntity;
import com.jaspersoft.android.sdk.service.data.schedule.JobOwner;
import com.jaspersoft.android.sdk.service.data.schedule.JobState;
import com.jaspersoft.android.sdk.service.data.schedule.JobUnit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.0
 */
class JobUnitMapper {
    public List<JobUnit> transform(List<JobUnitEntity> entities, SimpleDateFormat dateTimeFormat) {
        List<JobUnit> list = new ArrayList<>(entities.size());
        for (JobUnitEntity entity : entities) {
            if (entity != null) {
                JobUnit unit = transform(entity, dateTimeFormat);
                list.add(unit);
            }
        }
        return list;
    }

    public JobUnit transform(JobUnitEntity entity, SimpleDateFormat dateTimeFormat) {
        JobUnit.Builder jobUnitBuilder = new JobUnit.Builder();
        jobUnitBuilder.withId(entity.getId());
        jobUnitBuilder.withVersion(entity.getVersion());
        jobUnitBuilder.withLabel(entity.getLabel());
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
                Date nextFireDate = parseDateSilently(dateTimeFormat, nextFireTime);
                jobUnitBuilder.withNextFireTime(nextFireDate);
            }

            String previousFireTime = entityState.getPreviousFireTime();
            if (previousFireTime != null) {
                Date previousFireDate = parseDateSilently(dateTimeFormat, previousFireTime);
                jobUnitBuilder.withPreviousFireTime(previousFireDate);
            }
        }

        return jobUnitBuilder.build();
    }

    private Date parseDateSilently(SimpleDateFormat dateTimeFormat, String nextFireTime) {
        try {
            return dateTimeFormat.parse(nextFireTime);
        } catch (ParseException ex) {
            return null;
        }
    }
}
