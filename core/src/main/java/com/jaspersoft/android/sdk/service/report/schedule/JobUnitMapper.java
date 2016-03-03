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
import java.util.Locale;

/**
 * @author Tom Koptel
 * @since 2.0
 */
class JobUnitMapper {

    private static final String FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    private static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat(FORMAT_PATTERN, Locale.getDefault());

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
                Date nextFireDate = parseDateSilently(nextFireTime);
                jobUnitBuilder.withNextFireTime(nextFireDate);
            }

            String previousFireTime = entityState.getPreviousFireTime();
            if (previousFireTime != null) {
                Date previousFireDate = parseDateSilently(previousFireTime);
                jobUnitBuilder.withPreviousFireTime(previousFireDate);
            }
        }

        return jobUnitBuilder.build();
    }

    private Date parseDateSilently(String nextFireTime) {
        try {
            return DATE_FORMAT.parse(nextFireTime);
        } catch (ParseException ex) {
            return null;
        }
    }
}
