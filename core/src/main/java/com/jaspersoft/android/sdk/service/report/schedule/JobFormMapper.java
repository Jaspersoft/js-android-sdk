package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.schedule.JobFormEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.JobSimpleTriggerEntity;
import com.jaspersoft.android.sdk.service.data.schedule.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Tom Koptel
 * @since 2.0
 */
class JobFormMapper {
    private static final String FORMAT_PATTERN = "yyyy-MM-dd HH:mm";
    private static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat(FORMAT_PATTERN, Locale.getDefault());

    @NotNull
    public JobFormEntity transform(@NotNull JobForm form) {
        JobFormEntity entity = new JobFormEntity();
        entity.setLabel(form.getLabel());
        entity.setDescription(form.getDescription());
        entity.setBaseOutputFilename(form.getBaseOutputFilename());
        entity.setRepositoryDestination(form.getRepositoryDestination());
        entity.setSource(form.getSource());

        Set<JobOutputFormat> outputFormats = form.getOutputFormats();
        Collection<String> formats = new ArrayList<>(outputFormats.size());
        for (JobOutputFormat outputFormat : outputFormats) {
            formats.add(outputFormat.toString());
        }
        entity.addOutputFormats(formats);


        JobTrigger trigger = form.getTrigger();
        if (trigger instanceof JobSimpleTrigger) {
            JobSimpleTrigger jobSimpleTrigger = (JobSimpleTrigger) trigger;
            JobSimpleTriggerEntity simpleTriggerEntity = new JobSimpleTriggerEntity();

            simpleTriggerEntity.setOccurrenceCount(jobSimpleTrigger.getOccurrenceCount());
            simpleTriggerEntity.setRecurrenceInterval(jobSimpleTrigger.getRecurrenceInterval());
            simpleTriggerEntity.setCalendarName(jobSimpleTrigger.getCalendarName());

            RecurrenceIntervalUnit recurrenceIntervalUnit = jobSimpleTrigger.getRecurrenceIntervalUnit();
            if (recurrenceIntervalUnit != null) {
                simpleTriggerEntity.setRecurrenceIntervalUnit(mapInterval(recurrenceIntervalUnit));
            }

            Date startDate = jobSimpleTrigger.getStartDate();
            if (startDate != null) {
                simpleTriggerEntity.setStartDate(DATE_FORMAT.format(startDate));
            }

            Date stopDate = jobSimpleTrigger.getStopDate();
            if (stopDate != null) {
                simpleTriggerEntity.setStopDate(DATE_FORMAT.format(stopDate));
            }

            TimeZone timeZone = jobSimpleTrigger.getTimeZone();
            if (timeZone != null) {
                simpleTriggerEntity.setTimezone(timeZone.getID());
            }

            TriggerStartType startType = jobSimpleTrigger.getStartType();
            if (startType != null) {
                simpleTriggerEntity.setStartType(mapStartType(startType));
            }

            entity.setSimpleTrigger(simpleTriggerEntity);
        }

        return entity;
    }

    @TestOnly
    String mapInterval(RecurrenceIntervalUnit recurrenceIntervalUnit) {
        return recurrenceIntervalUnit.toString();
    }

    @TestOnly
    int mapStartType(TriggerStartType triggerStartType) {
        return triggerStartType.getValue();
    }
}
