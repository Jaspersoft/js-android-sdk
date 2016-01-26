package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
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

        RepositoryDestination repositoryDestination = form.getRepositoryDestination();
        entity.setRepositoryDestination(repositoryDestination.getFolderUri());

        JobSource source = form.getSource();
        List<ReportParameter> params = source.getParameters();
        Map<String, Set<String>> values = mapSourceParamValues(params);
        entity.setSourceUri(source.getUri());
        entity.setSourceParameters(values);

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
            simpleTriggerEntity.setRecurrenceIntervalUnit(mapInterval(recurrenceIntervalUnit));

            JobStartType startType = jobSimpleTrigger.getStartType();
            if (startType instanceof DeferredStartType) {
                DeferredStartType type = (DeferredStartType) startType;
                Date startDate = type.getStartDate();
                simpleTriggerEntity.setStartDate(DATE_FORMAT.format(startDate));
            }
            simpleTriggerEntity.setStartType(mapStartType(startType));

            Date stopDate = jobSimpleTrigger.getStopDate();
            if (stopDate != null) {
                simpleTriggerEntity.setEndDate(DATE_FORMAT.format(stopDate));
            }

            TimeZone timeZone = jobSimpleTrigger.getTimeZone();
            if (timeZone != null) {
                simpleTriggerEntity.setTimezone(timeZone.getID());
            }

            entity.setSimpleTrigger(simpleTriggerEntity);
        }

        return entity;
    }

    private Map<String, Set<String>> mapSourceParamValues(List<ReportParameter> params) {
        Map<String, Set<String>> values = new HashMap<>(params.size());
        for (ReportParameter param : params) {
            values.put(param.getName(), param.getValue());
        }
        return values;
    }

    @TestOnly
    String mapInterval(RecurrenceIntervalUnit recurrenceIntervalUnit) {
        return recurrenceIntervalUnit.toString();
    }

    @TestOnly
    int mapStartType(JobStartType startType) {
        return startType instanceof DeferredStartType ? 2 : 1;
    }
}
