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
import static org.hamcrest.Matchers.*;

@RunWith(JUnitParamsRunner.class)
public class JobFormMapperTest {

    private static final String FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat(FORMAT_PATTERN, Locale.getDefault());
    private static final TimeZone TIME_ZONE = TimeZone.getDefault();

    private static Date START_DATE;
    private static Date END_DATE;

    public static final String END_DATE_SRC = "2013-11-03 16:32:05";
    public static final String START_DATE_SRC = "2013-10-03 16:32:05";

    static {
        try {
            END_DATE = DATE_FORMAT.parse(END_DATE_SRC);
            START_DATE = DATE_FORMAT.parse(START_DATE_SRC);
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
                .withStartDate(START_DATE)
                .withEndDate(END_DATE);

        RepositoryDestination destination = new RepositoryDestination.Builder()
                .withFolderUri("/temp")
                .build();
        JobSource source = new JobSource.Builder()
                .withUri("/my/uri")
                .build();

        mJobBuilder = new JobForm.Builder()
                .withVersion(100)
                .withLabel("my label")
                .withDescription("Description")
                .withRepositoryDestination(destination)
                .withJobSource(source)
                .addOutputFormat(JobOutputFormat.HTML)
                .addOutputFormats(Collections.singletonList(JobOutputFormat.CSV))
                .withBaseOutputFilename("output");
    }

    @Test
    public void should_map_form_to_entity() throws Exception {
        JobSimpleTrigger trigger = mTriggerBuilder
                // Force start type immediate
                .withStartDate(null)
                .build();
        JobForm form = mJobBuilder
                .withSimpleTrigger(trigger)
                .build();

        JobFormEntity entity = mJobFormMapper.transform(form);
        assertThat(entity.getVersion(), is(100));
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
        assertThat(simpleTrigger.getEndDate(), is("2013-11-03 16:32:05"));
        assertThat(simpleTrigger.getOccurrenceCount(), is(1));
        assertThat(simpleTrigger.getRecurrenceInterval(), is(10));
        assertThat(simpleTrigger.getRecurrenceIntervalUnit(), is("DAY"));
    }

    @Test
    public void should_map_source_param_values() throws Exception {
        List<ReportParameter> parameters = Collections.singletonList(
                new ReportParameter("key", Collections.singleton("value")));

        JobSource source = new JobSource.Builder()
                .withUri("/my/uri")
                .withParameters(parameters)
                .build();

        JobForm form = mJobBuilder
                .withJobSource(source)
                .withSimpleTrigger(mTriggerBuilder.build())
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
        JobSimpleTrigger trigger = mTriggerBuilder
                .withRecurrenceIntervalUnit(RecurrenceIntervalUnit.DAY)
                .withStartDate(START_DATE)
                .build();

        JobForm form = mJobBuilder
                .withSimpleTrigger(trigger)
                .build();

        JobFormEntity entity = mJobFormMapper.transform(form);
        JobSimpleTriggerEntity simpleTrigger = entity.getSimpleTrigger();
        assertThat(simpleTrigger.getStartType(), is(2));
        assertThat(simpleTrigger.getStartDate(), is("2013-10-03 16:32:05"));
    }

    @Test
    @Parameters(method = "intervals")
    public void should_map_interval_to_primitive(String type) throws Exception {
        String expected = mJobFormMapper.mapInterval(RecurrenceIntervalUnit.valueOf(type));
        assertThat(expected, is(type));
    }

    @Test
    public void should_map_entity_form_to_data_type() throws Exception {
        JobFormEntity entity = new JobFormEntity();
        entity.setVersion(100);
        entity.setSimpleTrigger(createEntityTrigger());
        entity.setDescription("description");
        entity.setLabel("label");
        entity.setSourceUri("/my/uri");
        entity.setSourceParameters(Collections.singletonMap("key", Collections.singleton("value")));
        entity.addOutputFormats(Arrays.asList("PDF"));
        entity.setRepositoryDestination("/folder/uri");
        entity.setBaseOutputFilename("file.txt");

        JobForm expected = mJobFormMapper.transform(entity);

        assertThat(expected.getLabel(), is("label"));
        assertThat(expected.getVersion(), is(100));
        assertThat(expected.getDescription(), is("description"));
        assertThat(expected.getOutputFormats(), hasItem(JobOutputFormat.PDF));
        assertThat(expected.getRepositoryDestination().getFolderUri(), is("/folder/uri"));
        assertThat(expected.getBaseOutputFilename(), is("file.txt"));
        assertThat(expected.getSource().getUri(), is("/my/uri"));
        assertThat(expected.getSource().getParameters(), hasItem(new ReportParameter("key", Collections.singleton("value"))));
    }

    @Test
    public void should_map_entity_trigger_to_data_type() throws Exception {
        JobSimpleTriggerEntity entity = createEntityTrigger();

        JobSimpleTrigger expected = mJobFormMapper.transformTrigger(entity);

        assertThat(expected.getOccurrenceCount(), is(100));
        assertThat(expected.getRecurrenceInterval(), is(200));
        assertThat(expected.getRecurrenceIntervalUnit(), is(RecurrenceIntervalUnit.DAY));

        assertThat(expected.getStartDate(), is(START_DATE));
        assertThat(expected.getEndDate(), is(END_DATE));
        assertThat(expected.getCalendarName(), is("Gregorian"));
        assertThat(expected.getTimeZone(), is(TIME_ZONE));
    }

    private JobSimpleTriggerEntity createEntityTrigger() {
        JobSimpleTriggerEntity entity = new JobSimpleTriggerEntity();
        entity.setOccurrenceCount(100);
        entity.setRecurrenceInterval(200);
        entity.setRecurrenceIntervalUnit("DAY");
        entity.setStartDate(START_DATE_SRC);
        entity.setEndDate(END_DATE_SRC);
        entity.setCalendarName("Gregorian");
        entity.setTimezone(TIME_ZONE.getID());
        return entity;
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