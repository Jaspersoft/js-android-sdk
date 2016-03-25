package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.schedule.JobStateEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.JobUnitEntity;
import com.jaspersoft.android.sdk.service.data.schedule.JobOwner;
import com.jaspersoft.android.sdk.service.data.schedule.JobState;
import com.jaspersoft.android.sdk.service.data.schedule.JobUnit;
import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class JobUnitMapper {

    private static final SimpleDateFormat FORMAT_WITH_ZONE =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");

    private static final SimpleDateFormat FORMAT_WITH_MILLISECONDS_ZONE =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");

    private static final SimpleDateFormat FORMAT_WITHOUT_MILLISECONDS_ZONE =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

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
                Date nextFireDate = parseDate(nextFireTime);
                jobUnitBuilder.withNextFireTime(nextFireDate);
            }

            String previousFireTime = entityState.getPreviousFireTime();
            if (previousFireTime != null) {
                Date previousFireDate = parseDate(previousFireTime);
                jobUnitBuilder.withPreviousFireTime(previousFireDate);
            }
        }

        return jobUnitBuilder.build();
    }

    @Nullable
    private Date parseDate(String rawDate) {
        Date date = parseSilently(FORMAT_WITH_ZONE, rawDate);
        if (date == null) {
            date = parseSilently(FORMAT_WITH_MILLISECONDS_ZONE, rawDate);
        }
        if (date == null) {
            date = parseSilently(FORMAT_WITHOUT_MILLISECONDS_ZONE, rawDate);
        }
        return date;
    }

    @Nullable
    private Date parseSilently(SimpleDateFormat format, String raw) {
        try {
            return format.parse(raw);
        } catch (ParseException e) {
            return null;
        }
    }
}
