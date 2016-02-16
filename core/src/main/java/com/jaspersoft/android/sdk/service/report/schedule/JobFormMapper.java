package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.network.entity.schedule.JobFormEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.JobSimpleTriggerEntity;
import com.jaspersoft.android.sdk.service.data.schedule.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Tom Koptel
 * @since 2.0
 */
class JobFormMapper {
    private static final String FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
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


        JobSimpleTrigger jobSimpleTrigger = form.getSimpleTrigger();
        if (jobSimpleTrigger != null) {
            JobSimpleTriggerEntity simpleTriggerEntity = new JobSimpleTriggerEntity();

            simpleTriggerEntity.setOccurrenceCount(jobSimpleTrigger.getOccurrenceCount());
            simpleTriggerEntity.setRecurrenceInterval(jobSimpleTrigger.getRecurrenceInterval());
            simpleTriggerEntity.setCalendarName(jobSimpleTrigger.getCalendarName());

            RecurrenceIntervalUnit recurrenceIntervalUnit = jobSimpleTrigger.getRecurrenceIntervalUnit();
            simpleTriggerEntity.setRecurrenceIntervalUnit(mapInterval(recurrenceIntervalUnit));

            Date startDate = jobSimpleTrigger.getStartDate();
            if (startDate == null) {
                simpleTriggerEntity.setStartType(1);
            } else {
                simpleTriggerEntity.setStartDate(DATE_FORMAT.format(startDate));
                simpleTriggerEntity.setStartType(2);
            }

            Date stopDate = jobSimpleTrigger.getEndDate();
            if (stopDate != null) {
                simpleTriggerEntity.setEndDate(DATE_FORMAT.format(stopDate));
            }

            TimeZone timeZone = jobSimpleTrigger.getTimeZone();
            if (timeZone != null) {
                simpleTriggerEntity.setTimezone(timeZone.getID());
            }

            entity.setSimpleTrigger(simpleTriggerEntity);
        }

        Integer version = form.getVersion();
        if (version != null) {
            entity.setVersion(version);
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

    @NotNull
    public JobForm transform(@NotNull JobFormEntity entity) {
        JobForm.Builder builder = new JobForm.Builder();
        builder.withVersion(entity.getVersion());
        builder.withLabel(entity.getLabel());
        builder.withDescription(entity.getDescription());
        builder.withBaseOutputFilename(entity.getBaseOutputFilename());

        Collection<String> formats = entity.getOutputFormats();
        for (String format : formats) {
            JobOutputFormat out = JobOutputFormat.valueOf(format);
            builder.addOutputFormat(out);
        }

        JobSimpleTriggerEntity triggerEntity = entity.getSimpleTrigger();
        JobSimpleTrigger trigger = transformTrigger(triggerEntity);
        builder.withSimpleTrigger(trigger);

        JobSource source = transformSource(entity);
        builder.withJobSource(source);

        RepositoryDestination destination = transformDestination(entity);
        builder.withRepositoryDestination(destination);

        return builder.build();
    }

    private RepositoryDestination transformDestination(JobFormEntity entity) {
        RepositoryDestination.Builder builder = new RepositoryDestination.Builder();
        builder.withFolderUri(entity.getRepositoryDestination());
        return builder.build();
    }

    private JobSource transformSource(JobFormEntity entity) {
        JobSource.Builder builder = new JobSource.Builder();
        builder.withUri(entity.getSourceUri());

        Map<String, Set<String>> parameters = entity.getSourceParameters();
        if (parameters != null) {
            List<ReportParameter> params = mapParams(parameters);
            builder.withParameters(params);
        }

        return builder.build();
    }

    @TestOnly
    JobSimpleTrigger transformTrigger(JobSimpleTriggerEntity entity) {
        JobSimpleTrigger.Builder triggerBuilder = new JobSimpleTrigger.Builder();

        Integer occurrenceCount = entity.getOccurrenceCount();
        if (occurrenceCount != null) {
            triggerBuilder.withOccurrenceCount(occurrenceCount);
        }

        Integer recurrenceInterval = entity.getRecurrenceInterval();
        if (recurrenceInterval != null) {
            triggerBuilder.withRecurrenceInterval(recurrenceInterval);
        }

        String unit = entity.getRecurrenceIntervalUnit();
        if (unit != null) {
            RecurrenceIntervalUnit intervalUnit = RecurrenceIntervalUnit.valueOf(unit);
            triggerBuilder.withRecurrenceIntervalUnit(intervalUnit);
        }

        String startDate = entity.getStartDate();
        if (startDate != null) {
            Date date = parseDate(startDate);
            if (date != null) {
                triggerBuilder.withStartDate(date);
            }
        }

        String endDate = entity.getEndDate();
        if (endDate != null) {
            Date date = parseDate(endDate);
            if (date != null) {
                triggerBuilder.withEndDate(date);
            }
        }

        triggerBuilder.withCalendarName(entity.getCalendarName());
        String timezone = entity.getTimezone();
        if (timezone != null) {
            TimeZone timeZone = TimeZone.getTimeZone(timezone);
            triggerBuilder.withTimeZone(timeZone);
        }

        return triggerBuilder.build();
    }

    public List<ReportParameter> mapParams(Map<String, Set<String>> parameters) {
        List<ReportParameter> params = new ArrayList<>(parameters.size());
        for (Map.Entry<String, Set<String>> entry : parameters.entrySet()) {
            params.add(new ReportParameter(entry.getKey(), entry.getValue()));
        }
        return params;
    }

    private Date parseDate(String target) {
        try {
            return DATE_FORMAT.parse(target);
        } catch (ParseException e) {
            return null;
        }
    }
}
