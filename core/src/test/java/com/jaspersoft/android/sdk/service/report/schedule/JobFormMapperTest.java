package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.network.entity.schedule.JobFormEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.JobSimpleTriggerEntity;
import com.jaspersoft.android.sdk.service.data.schedule.*;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static junitparams.JUnitParamsRunner.$;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

@RunWith(JUnitParamsRunner.class)
public class JobFormMapperTest {

    private static final String FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat(FORMAT_PATTERN, Locale.getDefault());
    private static final TimeZone TIME_ZONE = TimeZone.getDefault();

    private static Date START_DATE;
    private static Date END_DATE;
    static {
        try {
            END_DATE = DATE_FORMAT.parse("2013-11-03 16:32:05");
            START_DATE = DATE_FORMAT.parse("2013-10-03 16:32:05");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private JobFormMapper mJobFormMapper;
    private JobSimpleTrigger.Builder mTriggerBuilder;
    private JobForm.Builder mJobBuilder;

    @Before
    public void setUp() throws Exception {
        mJobFormMapper = new JobFormMapper();
        mTriggerBuilder = new JobSimpleTrigger.Builder()
                .withCalendarName("calendar name")
                .withTimeZone(TIME_ZONE)
                .withOccurrenceCount(1)
                .withRecurrenceInterval(10)
                .withRecurrenceIntervalUnit(RecurrenceIntervalUnit.DAY)
                .withStartType(new ImmediateStartType())
                .withStopDate(END_DATE);
        mJobBuilder = new JobForm.Builder()
                .withLabel("my label")
                .withDescription("Description")
                .addRepositoryDestination().withFolderUri("/temp").done()
                .addSource().withUri("/my/uri").done()
                .addOutputFormat(JobOutputFormat.HTML)
                .addOutputFormats(Collections.singletonList(JobOutputFormat.CSV))
                .withBaseOutputFilename("output");
    }

    @Test
    public void testTransform() throws Exception {
        JobTrigger trigger = mTriggerBuilder.build();
        JobForm form = mJobBuilder
                .withTrigger(trigger)
                .build();

        JobFormEntity entity = mJobFormMapper.transform(form);
        assertThat(entity.getLabel(), is("my label"));
        assertThat(entity.getDescription(), is("Description"));
        assertThat(entity.getRepositoryDestination(), is("/temp"));
        assertThat(entity.getSourceUri(), is("/my/uri"));
        assertThat(entity.getOutputFormats(), hasItems("HTML", "CSV"));
        assertThat(entity.getBaseOutputFilename(), is("output"));

        JobSimpleTriggerEntity simpleTrigger = entity.getSimpleTrigger();
        assertThat(simpleTrigger.getTimezone(), is(TIME_ZONE.getID()));
        assertThat(simpleTrigger.getCalendarName(), is("calendar name"));
        assertThat(simpleTrigger.getStartType(), is(1));
        assertThat(simpleTrigger.getEndDate(), is("2013-11-03 16:32"));
        assertThat(simpleTrigger.getOccurrenceCount(), is(1));
        assertThat(simpleTrigger.getRecurrenceInterval(), is(10));
        assertThat(simpleTrigger.getRecurrenceIntervalUnit(), is("DAY"));
    }

    @Test
    public void should_map_source_param_values() throws Exception {
        List<ReportParameter> parameters = Collections.singletonList(
                new ReportParameter("key", Collections.singleton("value")));

        JobForm form = mJobBuilder
                .addSource()
                .withParameters(parameters)
                .done()
                .withTrigger(mTriggerBuilder.build())
                .build();
        JobFormEntity entity = mJobFormMapper.transform(form);


        Map<String, Set<String>> params = entity.getSourceParameters();
        Collection<String> values = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : params.entrySet()) {
            values.addAll(entry.getValue());
        }

        assertThat(params.keySet(), hasItem("key"));
        assertThat(values, hasItem("value"));
    }

    @Test
    public void should_transform_deffered_trigger() throws Exception {
        JobTrigger trigger = mTriggerBuilder
                .withRecurrenceIntervalUnit(RecurrenceIntervalUnit.DAY)
                .withStartType(new DeferredStartType(START_DATE))
                .build();

        JobForm form = mJobBuilder
                .withTrigger(trigger)
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