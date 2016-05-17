package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.network.entity.schedule.*;
import com.jaspersoft.android.sdk.service.data.schedule.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class JobFormMapper {
    private static final Integer[] ALL_WEEK_DAYS = {
            Calendar.SUNDAY,
            Calendar.MONDAY,
            Calendar.TUESDAY,
            Calendar.WEDNESDAY,
            Calendar.THURSDAY,
            Calendar.FRIDAY,
            Calendar.SATURDAY
    };
    private static final String FORMAT_PATTERN = "yyyy-MM-dd HH:mm";
    private static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat(FORMAT_PATTERN, Locale.getDefault());

    private final JobTriggerMapper mTriggerMapper;

    JobFormMapper(JobTriggerMapper triggerMapper) {
        mTriggerMapper = triggerMapper;
    }

    private static class InstanceHolder {
        private final static JobFormMapper INSTANCE = new JobFormMapper(
                JobTriggerMapper.getInstance());
    }

    public static JobFormMapper getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @NotNull
    public JobFormEntity toFormEntity(@NotNull JobForm form) {
        JobFormEntity entity = new JobFormEntity();
        mapFormToCommonEntityFields(form, entity);
        mapFormDestinationOnEntity(form, entity);
        mapFormSourceOnEntity(form, entity);
        mapFormFormatsOnEntity(form, entity);
        mTriggerMapper.toTriggerEntity(form, entity);
        return entity;
    }

    @TestOnly
    void mapFormToCommonEntityFields(JobForm form, JobFormEntity entity) {
        entity.setLabel(form.getLabel());
        entity.setDescription(form.getDescription());
        entity.setBaseOutputFilename(form.getBaseOutputFilename());

        Integer version = form.getVersion();
        if (version != null) {
            entity.setVersion(version);
        }
    }

    @TestOnly
    void mapFormDestinationOnEntity(JobForm form, JobFormEntity entity) {
        RepositoryDestination repositoryDestination = form.getRepositoryDestination();
        entity.setRepositoryDestination(repositoryDestination.getFolderUri());
    }

    @TestOnly
    void mapFormSourceOnEntity(JobForm form, JobFormEntity entity) {
        JobSource source = form.getSource();
        List<ReportParameter> params = source.getParameters();
        Map<String, Set<String>> values = mapSourceParamValues(params);
        entity.setSourceUri(source.getUri());
        entity.setSourceParameters(values);
    }

    @TestOnly
    void mapFormFormatsOnEntity(JobForm form, JobFormEntity entity) {
        Set<JobOutputFormat> outputFormats = form.getOutputFormats();
        Collection<String> formats = new ArrayList<>(outputFormats.size());
        for (JobOutputFormat outputFormat : outputFormats) {
            formats.add(outputFormat.toString());
        }
        entity.addOutputFormats(formats);
    }

    private Map<String, Set<String>> mapSourceParamValues(List<ReportParameter> params) {
        Map<String, Set<String>> values = new HashMap<>(params.size());
        for (ReportParameter param : params) {
            values.put(param.getName(), param.getValue());
        }
        return values;
    }

    @NotNull
    public JobForm toDataForm(@NotNull JobFormEntity entity) {
        JobForm.Builder form = new JobForm.Builder();
        mapEntityCommonFieldsToForm(form, entity);
        mapEntitySourceToForm(form, entity);
        mapEntityDestinationToForm(form, entity);
        mapEntityFormatsToForm(form, entity);
        mapEntityTriggerToForm(form, entity);
        return form.build();
    }

    @TestOnly
    void mapEntityCommonFieldsToForm(JobForm.Builder form, JobFormEntity entity) {
        form.withVersion(entity.getVersion());
        form.withLabel(entity.getLabel());
        form.withDescription(entity.getDescription());
        form.withBaseOutputFilename(entity.getBaseOutputFilename());
    }

    @TestOnly
    void mapEntityFormatsToForm(JobForm.Builder form, JobFormEntity entity) {
        Collection<String> formats = entity.getOutputFormats();
        List<JobOutputFormat> formatList = new ArrayList<>();
        for (String format : formats) {
            JobOutputFormat out = JobOutputFormat.valueOf(format);
            formatList.add(out);
        }
        form.withOutputFormats(formatList);
    }

    @TestOnly
    void mapEntitySourceToForm(JobForm.Builder form, JobFormEntity entity) {
        JobSource.Builder builder = new JobSource.Builder();
        builder.withUri(entity.getSourceUri());

        Map<String, Set<String>> parameters = entity.getSourceParameters();
        if (parameters != null) {
            List<ReportParameter> params = mapParams(parameters);
            builder.withParameters(params);
        }

        JobSource source = builder.build();
        form.withJobSource(source);
    }

    @TestOnly
    void mapEntityDestinationToForm(JobForm.Builder form, JobFormEntity entity) {
        RepositoryDestination.Builder builder = new RepositoryDestination.Builder();
        builder.withFolderUri(entity.getRepositoryDestination());
        RepositoryDestination destination = builder.build();
        form.withRepositoryDestination(destination);
    }

    @TestOnly
    void mapEntityTriggerToForm(JobForm.Builder form, JobFormEntity entity) {
        JobSimpleTriggerEntity simpleTrigger = entity.getSimpleTrigger();

        mapEntityCommonTriggerFields(form, entity);
        if (simpleTrigger == null) {
            mapEntityCalendarTrigger(form, entity);
        } else {
            mapEntitySimpleTrigger(form, entity);
        }
    }

    private void mapEntityCommonTriggerFields(JobForm.Builder form, JobFormEntity entity) {
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

    private void mapEntitySimpleTrigger(JobForm.Builder form, JobFormEntity entity) {
        JobSimpleTriggerEntity simpleTrigger = entity.getSimpleTrigger();

        int occurrenceCount = simpleTrigger.getOccurrenceCount();
        Integer recurrenceInterval = simpleTrigger.getRecurrenceInterval();
        String recurrenceIntervalUnit = simpleTrigger.getRecurrenceIntervalUnit();

        boolean triggerIsSimpleType =
                (recurrenceInterval != null && recurrenceIntervalUnit != null);

        if (triggerIsSimpleType) {
            Date endDate = null;
            String endDateString = simpleTrigger.getEndDate();
            if (endDateString != null) {
                endDate = parseDate(endDateString);
            }

            IntervalRecurrence recurrence = new IntervalRecurrence.Builder()
                    .withInterval(recurrenceInterval)
                    .withUnit(RecurrenceIntervalUnit.valueOf(recurrenceIntervalUnit))
                    .build();
            Trigger.SimpleTriggerBuilder triggerBuilder = new Trigger.Builder()
                    .withCalendarName(simpleTrigger.getCalendarName())
                    .withRecurrence(recurrence);

            if (occurrenceCount < 0 && endDate != null) {
                triggerBuilder.withEndDate(new UntilEndDate(endDate));
            } else {
                triggerBuilder.withEndDate(new RepeatedEndDate(occurrenceCount));
            }

            Trigger trigger = triggerBuilder.build();
            form.withTrigger(trigger);
        }
    }

    private void mapEntityCalendarTrigger(JobForm.Builder form, JobFormEntity entity) {
        JobCalendarTriggerEntity calendarTrigger = entity.getCalendarTrigger();

        CalendarRecurrence.Builder recurrenceBuilder = new CalendarRecurrence.Builder()
                .withMinutes(MinutesTimeFormat.parse(calendarTrigger.getMinutes()))
                .withHours(HoursTimeFormat.parse(calendarTrigger.getHours()))
                .withMonths(mapMonthsToData(calendarTrigger.getMonths()));

        String daysType = calendarTrigger.getDaysType();
        if ("ALL".equals(daysType)) {
            recurrenceBuilder.withDaysInWeek(ALL_WEEK_DAYS);
        } else if ("WEEK".equals(daysType)) {
            recurrenceBuilder.withDaysInWeek(calendarTrigger.getWeekDays());
        } else if ("MONTH".equals(daysType)) {
            recurrenceBuilder.withDaysInMonth(DaysInMonth.valueOf(calendarTrigger.getMonthDays()));
        }

        CalendarRecurrence calendarRecurrence = recurrenceBuilder.build();
        Trigger.CalendarTriggerBuilder triggerBuilder = new Trigger.Builder()
                .withCalendarName(calendarTrigger.getCalendarName())
                .withRecurrence(calendarRecurrence);

        Date endDate = null;
        String endDateString = calendarTrigger.getEndDate();
        if (endDateString != null) {
            endDate = parseDate(endDateString);
        }

        if (endDate != null) {
            triggerBuilder.withEndDate(new UntilEndDate(endDate));
        }

        Trigger trigger = triggerBuilder.build();
        form.withTrigger(trigger);
    }

    private Integer[] mapMonthsToData(Set<Integer> monthSet) {
        List<Integer> monthList = new ArrayList<>(monthSet);
        int size = monthSet.size();
        Integer[] result = new Integer[size];

        for (int i = 0; i < size; i++) {
            result[i] = monthList.get(i) - 1;
        }

        return result;
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
