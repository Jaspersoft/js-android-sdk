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
        Date endDate = DATE_FORMAT.parse("2013-11-03 16:32:05");

        TimeZone timezone = TimeZone.getDefault();
        JobTrigger trigger = new JobSimpleTrigger.Builder()
                .withCalendarName("calendar name")
                .withTimeZone(timezone)
                .withOccurrenceCount(1)
                .withRecurrenceInterval(10)
                .withRecurrenceIntervalUnit(RecurrenceIntervalUnit.DAY)
                .withStartType(new ImmediateStartType())
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
        assertThat(simpleTrigger.getEndDate(), is("2013-11-03 16:32"));
        assertThat(simpleTrigger.getOccurrenceCount(), is(1));
        assertThat(simpleTrigger.getRecurrenceInterval(), is(10));
        assertThat(simpleTrigger.getRecurrenceIntervalUnit(), is("DAY"));
    }

    @Test
    public void should_transform_deffered_trigger() throws Exception {
        Date startDate = DATE_FORMAT.parse("2013-10-03 16:32:05");

        JobTrigger trigger = new JobSimpleTrigger.Builder()
                .withOccurrenceCount(1)
                .withRecurrenceInterval(10)
                .withRecurrenceIntervalUnit(RecurrenceIntervalUnit.DAY)
                .withStartType(new DeferredStartType(startDate))
                .build();

        JobForm form = new JobForm.Builder()
                .withLabel("my label")
                .withDescription("Description")
                .withTrigger(trigger)
                .addOutputFormat(JobOutputFormat.CSV)
                .withBaseOutputFilename("output")
                .withRepositoryDestination("/temp")
                .withSource("/my/uri")
                .build();

        JobFormEntity entity = mJobFormMapper.transform(form);
        JobSimpleTriggerEntity simpleTrigger = entity.getSimpleTrigger();
        assertThat(simpleTrigger.getStartType(), is(2));
        assertThat(simpleTrigger.getStartDate(), is("2013-10-03 16:32"));
    }

    @Test
    @Parameters(method = "intervals")
    public void should_map_interval_to_primitive(String type) throws Exception {
        String expected = mJobFormMapper.mapInterval(RecurrenceIntervalUnit.valueOf(type));
        assertThat(expected, is(type));
    }

    private Object[] intervals() {
        return $(
                $("MINUTE"),
                $("HOUR"),
                $("DAY"),
                $("WEEK")
        );
    }

}