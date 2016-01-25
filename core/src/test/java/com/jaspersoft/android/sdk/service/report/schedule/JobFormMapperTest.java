package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.schedule.JobFormEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.JobSimpleTriggerEntity;
import com.jaspersoft.android.sdk.service.data.schedule.*;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static junitparams.JUnitParamsRunner.$;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

@RunWith(JUnitParamsRunner.class)
public class JobFormMapperTest {

    public static final String FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat(FORMAT_PATTERN, Locale.getDefault());

    private JobFormMapper mJobFormMapper;

    @Before
    public void setUp() throws Exception {
        mJobFormMapper = new JobFormMapper();
    }

    @Test
    public void testTransform() throws Exception {
        Date startDate = DATE_FORMAT.parse("2013-10-03 16:32:05");
        Date endDate = DATE_FORMAT.parse("2013-11-03 16:32:05");

        TimeZone timezone = TimeZone.getDefault();
        JobTrigger trigger = new JobSimpleTrigger.Builder()
                .withCalendarName("calendar name")
                .withTimeZone(timezone)
                .withOccurrenceCount(1)
                .withRecurrenceInterval(10)
                .withRecurrenceIntervalUnit(RecurrenceIntervalUnit.DAY)
                .withStartType(TriggerStartType.IMMEDIATE)
                .withStartDate(startDate)
                .withStopDate(endDate)
                .build();

        JobForm form = new JobForm.Builder()
                .withLabel("my label")
                .withDescription("Description")
                .withRepositoryDestination("/temp")
                .withSource("/my/uri")
                .addOutputFormat(JobOutputFormat.HTML)
                .addOutputFormats(Collections.singletonList(JobOutputFormat.CSV))
                .withBaseOutputFilename("output")
                .withTrigger(trigger)
                .build();

        JobFormEntity entity = mJobFormMapper.transform(form);
        assertThat(entity.getLabel(), is("my label"));
        assertThat(entity.getDescription(), is("Description"));
        assertThat(entity.getRepositoryDestination(), is("/temp"));
        assertThat(entity.getSource(), is("/my/uri"));
        assertThat(entity.getOutputFormats(), hasItems("HTML", "CSV"));
        assertThat(entity.getBaseOutputFilename(), is("output"));

        JobSimpleTriggerEntity simpleTrigger = entity.getSimpleTrigger();
        assertThat(simpleTrigger.getTimezone(), is(timezone.getID()));
        assertThat(simpleTrigger.getCalendarName(), is("calendar name"));
        assertThat(simpleTrigger.getStartType(), is(1));
        assertThat(simpleTrigger.getStartDate(), is("2013-10-03 16:32"));
        assertThat(simpleTrigger.getStopDate(), is("2013-11-03 16:32"));
        assertThat(simpleTrigger.getOccurrenceCount(), is(1));
        assertThat(simpleTrigger.getRecurrenceInterval(), is(10));
        assertThat(simpleTrigger.getRecurrenceIntervalUnit(), is("DAY"));
    }

    @Test
    public void should_map_empty_form_without_issues() throws Exception {
        JobForm form = new JobForm.Builder().build();
        mJobFormMapper.transform(form);
    }

    @Test
    @Parameters(method = "intervals")
    public void should_map_interval_to_primitive(String type) throws Exception {
        String expected = mJobFormMapper.mapInterval(RecurrenceIntervalUnit.valueOf(type));
        assertThat(expected, is(type));
    }

    @Test
    @Parameters(method = "types")
    public void should_map_start_type(String type, int value) throws Exception {
        int expected = mJobFormMapper.mapStartType(TriggerStartType.valueOf(type));
        assertThat(expected, is(value));
    }

    private Object[] intervals() {
        return $(
                $("MINUTE"),
                $("HOUR"),
                $("DAY"),
                $("WEEK")
        );
    }

    private Object[] types() {
        return $(
                $("IMMEDIATE", 1),
                $("DEFERRED", 2)
        );
    }
}