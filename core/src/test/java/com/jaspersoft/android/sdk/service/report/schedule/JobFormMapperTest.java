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

    private static final String FORMAT_PATTERN = "yyyy-MM-dd HH:mm";
    private static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat(FORMAT_PATTERN, Locale.getDefault());
    private static final TimeZone TIME_ZONE = TimeZone.getDefault();

    private static Date START_DATE;
    private static Date END_DATE;

    public static final String END_DATE_SRC = "2013-11-03 16:32";
    public static final String START_DATE_SRC = "2013-10-03 16:32";

    static {
        try {
            END_DATE = DATE_FORMAT.parse(END_DATE_SRC);
            START_DATE = DATE_FORMAT.parse(START_DATE_SRC);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private JobFormMapper mJobFormMapper;
    private JobForm.Builder mJobBuilder;

    @Before
    public void setUp() throws Exception {
        mJobFormMapper = new JobFormMapper();

        RepositoryDestination destination = new RepositoryDestination.Builder()
                .withFolderUri("/temp")
                .build();
        JobSource source = new JobSource.Builder()
                .withUri("/my/uri")
                .build();
        List<JobOutputFormat> jobOutputFormats = new ArrayList<>();
        jobOutputFormats.add(JobOutputFormat.HTML);
        jobOutputFormats.add(JobOutputFormat.CSV);

        mJobBuilder = new JobForm.Builder()
                .withVersion(100)
                .withLabel("my label")
                .withDescription("Description")
                .withRepositoryDestination(destination)
                .withJobSource(source)
                .withOutputFormats(jobOutputFormats)
                .withBaseOutputFilename("output")
                .withStartDate(START_DATE)
                .withTimeZone(TIME_ZONE);
    }

    @Test
    public void should_map_form_to_entity() throws Exception {
        JobForm form = mJobBuilder
                .withStartDate(null) // immediate start type
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
        assertThat(simpleTrigger.getStartType(), is(1));
        assertThat(simpleTrigger.getOccurrenceCount(), is(1));
        assertThat(simpleTrigger.getRecurrenceInterval(), is(1));
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
        assertThat(expected.getStartDate(), is(START_DATE));
        assertThat(expected.getTimeZone(), is(TIME_ZONE));

        JobSource source = expected.getSource();
        assertThat(source.getUri(), is("/my/uri"));
        assertThat(source.getParameters(), hasItem(new ReportParameter("key", Collections.singleton("value"))));
    }

    private JobSimpleTriggerEntity createEntityTrigger() {
        JobSimpleTriggerEntity entity = new JobSimpleTriggerEntity();
        entity.setOccurrenceCount(1);
        entity.setRecurrenceInterval(0);
        entity.setRecurrenceIntervalUnit("DAY");
        entity.setStartDate(START_DATE_SRC);
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