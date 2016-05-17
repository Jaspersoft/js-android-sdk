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
abstract class BaseTriggerMapper {
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

    public abstract void toTriggerEntity(JobForm form, JobFormEntity entity);

    public abstract void toDataForm(JobForm.Builder form, JobFormEntity entity);
}
