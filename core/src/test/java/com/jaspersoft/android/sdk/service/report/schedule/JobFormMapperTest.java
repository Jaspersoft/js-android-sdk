package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.network.entity.schedule.JobCalendarTriggerEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.JobFormEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.JobSimpleTriggerEntity;
import com.jaspersoft.android.sdk.service.data.schedule.*;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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
    private JobForm mForm;
    private JobFormEntity mEntity;
    private JobFormEntity mPreparedEntity;

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

        mForm = mJobBuilder.build();
        mEntity = new JobFormEntity();

        mPreparedEntity = new JobFormEntity();
        mPreparedEntity.setVersion(100);
        mPreparedEntity.setDescription("description");
        mPreparedEntity.setLabel("label");
        mPreparedEntity.setSourceUri("/my/uri");
        mPreparedEntity.setSourceParameters(Collections.singletonMap("key", Collections.singleton("value")));
        mPreparedEntity.addOutputFormats(Arrays.asList("PDF"));
        mPreparedEntity.setRepositoryDestination("/folder/uri");
        mPreparedEntity.setBaseOutputFilename("file.txt");
    }

    @Test
    public void should_map_form_to_common_entity_fields() throws Exception {
        mJobFormMapper.mapFormToCommonEntityFields(mForm, mEntity);

        assertThat(mEntity.getVersion(), is(100));
        assertThat(mEntity.getLabel(), is("my label"));
        assertThat(mEntity.getDescription(), is("Description"));
        assertThat(mEntity.getBaseOutputFilename(), is("output"));
    }

    @Test
    public void should_map_form_destination_on_entity() throws Exception {
        mJobFormMapper.mapFormDestinationOnEntity(mForm, mEntity);

        assertThat(mEntity.getRepositoryDestination(), is("/temp"));
    }

    @Test
    public void should_map_form_source_on_entity() throws Exception {
        mJobFormMapper.mapFormSourceOnEntity(mForm, mEntity);

        assertThat(mEntity.getSourceUri(), is("/my/uri"));
    }

    @Test
    public void should_map_form_formats_on_entity() throws Exception {
        mJobFormMapper.mapFormFormatsOnEntity(mForm, mEntity);

        assertThat(mEntity.getOutputFormats(), hasItems("HTML", "CSV"));
    }

    @Test
    public void should_map_no_trigger_type_on_entity() throws Exception {
        JobForm form = mJobBuilder
                .withStartDate(null) // immediate start type
                .build();

        mJobFormMapper.mapFormTriggerOnEntity(form, mEntity);

        JobSimpleTriggerEntity simpleTrigger = mEntity.getSimpleTrigger();
        assertThat(simpleTrigger.getOccurrenceCount(), is(1));
        assertThat(simpleTrigger.getRecurrenceInterval(), is(1));
        assertThat(simpleTrigger.getRecurrenceIntervalUnit(), is("DAY"));
        assertThat(simpleTrigger.getTimezone(), is(TIME_ZONE.getID()));
        assertThat(simpleTrigger.getStartType(), is(1));
    }

    @Test
    public void should_map_no_trigger_start_date_on_entity() throws Exception {
        mJobFormMapper.mapFormTriggerOnEntity(mForm, mEntity);

        JobSimpleTriggerEntity simpleTrigger = mEntity.getSimpleTrigger();
        assertThat(simpleTrigger.getStartType(), is(2));
        assertThat(simpleTrigger.getStartDate(), is(START_DATE_SRC));
    }

    @Test
    public void should_map_simple_trigger_with_infinite_value_on_entity() throws Exception {
        IntervalRecurrence recurrence = new IntervalRecurrence.Builder()
                .withInterval(10)
                .withUnit(RecurrenceIntervalUnit.DAY)
                .build();

        Trigger trigger = new Trigger.Builder()
                .withCalendarName("Gregorian")
                .withRecurrence(recurrence)
                .build();

        JobForm form = mForm.newBuilder()
                .withTrigger(trigger)
                .build();

        mJobFormMapper.mapFormTriggerOnEntity(form, mEntity);

        JobSimpleTriggerEntity simpleTrigger = mEntity.getSimpleTrigger();
        assertThat(simpleTrigger.getCalendarName(), is("Gregorian"));
        assertThat(simpleTrigger.getOccurrenceCount(), is(-1));
        assertThat(simpleTrigger.getRecurrenceIntervalUnit(), is("DAY"));
        assertThat(simpleTrigger.getRecurrenceInterval(), is(10));
    }

    @Test
    public void should_map_simple_trigger_with_repeated_value_on_entity() throws Exception {
        IntervalRecurrence recurrence = new IntervalRecurrence.Builder()
                .withInterval(10)
                .withUnit(RecurrenceIntervalUnit.DAY)
                .build();

        Trigger trigger = new Trigger.Builder()
                .withRecurrence(recurrence)
                .withEndDate(new RepeatedEndDate(100))
                .build();

        JobForm form = mForm.newBuilder()
                .withTrigger(trigger)
                .build();

        mJobFormMapper.mapFormTriggerOnEntity(form, mEntity);

        JobSimpleTriggerEntity simpleTrigger = mEntity.getSimpleTrigger();
        assertThat(simpleTrigger.getOccurrenceCount(), is(100));
        assertThat(simpleTrigger.getRecurrenceIntervalUnit(), is("DAY"));
        assertThat(simpleTrigger.getRecurrenceInterval(), is(10));
    }

    @Test
    public void should_map_simple_trigger_with_until_date_value_on_entity() throws Exception {
        IntervalRecurrence recurrence = new IntervalRecurrence.Builder()
                .withInterval(10)
                .withUnit(RecurrenceIntervalUnit.DAY)
                .build();

        Trigger trigger = new Trigger.Builder()
                .withRecurrence(recurrence)
                .withEndDate(new UntilEndDate(END_DATE))
                .build();

        JobForm form = mForm.newBuilder()
                .withTrigger(trigger)
                .build();

        mJobFormMapper.mapFormTriggerOnEntity(form, mEntity);

        JobSimpleTriggerEntity simpleTrigger = mEntity.getSimpleTrigger();
        assertThat(simpleTrigger.getEndDate(), is(END_DATE_SRC));
        assertThat(simpleTrigger.getOccurrenceCount(), is(-1));
        assertThat(simpleTrigger.getRecurrenceIntervalUnit(), is("DAY"));
        assertThat(simpleTrigger.getRecurrenceInterval(), is(10));
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
        JobFormEntity entity = mJobFormMapper.toFormEntity(form);


        Map<String, Set<String>> params = entity.getSourceParameters();
        Collection<String> values = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : params.entrySet()) {
            values.addAll(entry.getValue());
        }

        assertThat(params.keySet(), hasItem("key"));
        assertThat(values, hasItem("value"));
    }

    @Test
    public void should_map_calendar_trigger() throws Exception {
        CalendarRecurrence recurrence = new CalendarRecurrence.Builder()
                .withMonths(Calendar.JANUARY, Calendar.FEBRUARY)
                .withHours(HoursTimeFormat.parse("1"))
                .withMinutes(MinutesTimeFormat.parse("2"))
                .build();

        Trigger trigger = new Trigger.Builder()
                .withCalendarName("Gregorian")
                .withRecurrence(recurrence)
                .withEndDate(new UntilEndDate(END_DATE))
                .build();

        JobForm form = mForm.newBuilder()
                .withTrigger(trigger)
                .build();

        mJobFormMapper.mapFormTriggerOnEntity(form, mEntity);

        JobCalendarTriggerEntity calendarTrigger = mEntity.getCalendarTrigger();
        assertThat(calendarTrigger.getCalendarName(), is("Gregorian"));
        assertThat(calendarTrigger.getEndDate(), is(END_DATE_SRC));
        assertThat(calendarTrigger.getMonths(), hasItems(Calendar.JANUARY + 1, Calendar.FEBRUARY + 1));
        assertThat(calendarTrigger.getHours(), is("1"));
        assertThat(calendarTrigger.getMinutes(), is("2"));
        assertThat(calendarTrigger.getDaysType(), is("ALL"));
        assertThat(calendarTrigger.getWeekDays(), is(empty()));
        assertThat(calendarTrigger.getMonthDays(), is(""));
    }

    @Test
    public void should_map_calendar_trigger_with_day_type_days_in_week() throws Exception {
        CalendarRecurrence recurrence = new CalendarRecurrence.Builder()
                .withMonths(Calendar.JANUARY, Calendar.FEBRUARY)
                .withHours(HoursTimeFormat.parse("1"))
                .withMinutes(MinutesTimeFormat.parse("2"))
                .withDaysInWeek(Calendar.MONDAY, Calendar.THURSDAY)
                .build();

        Trigger trigger = new Trigger.Builder()
                .withRecurrence(recurrence)
                .build();

        JobForm form = mForm.newBuilder()
                .withTrigger(trigger)
                .build();

        mJobFormMapper.mapFormTriggerOnEntity(form, mEntity);

        JobCalendarTriggerEntity calendarTrigger = mEntity.getCalendarTrigger();
        assertThat(calendarTrigger.getMonths(), hasItems(Calendar.JANUARY + 1, Calendar.FEBRUARY + 1));
        assertThat(calendarTrigger.getHours(), is("1"));
        assertThat(calendarTrigger.getMinutes(), is("2"));
        assertThat(calendarTrigger.getDaysType(), is("WEEK"));
        assertThat(calendarTrigger.getWeekDays(), hasItems(Calendar.MONDAY, Calendar.THURSDAY));
        assertThat(calendarTrigger.getMonthDays(), is(""));
    }

    @Test
    public void should_map_calendar_trigger_with_day_type_days_in_month() throws Exception {
        CalendarRecurrence recurrence = new CalendarRecurrence.Builder()
                .withMonths(Calendar.JANUARY, Calendar.FEBRUARY)
                .withHours(HoursTimeFormat.parse("1"))
                .withMinutes(MinutesTimeFormat.parse("2"))
                .withDaysInMonth(DaysInMonth.valueOf("1"))
                .build();

        Trigger trigger = new Trigger.Builder()
                .withRecurrence(recurrence)
                .build();

        JobForm form = mForm.newBuilder()
                .withTrigger(trigger)
                .build();

        mJobFormMapper.mapFormTriggerOnEntity(form, mEntity);

        JobCalendarTriggerEntity calendarTrigger = mEntity.getCalendarTrigger();
        assertThat(calendarTrigger.getMonths(), hasItems(Calendar.JANUARY + 1, Calendar.FEBRUARY + 1));
        assertThat(calendarTrigger.getHours(), is("1"));
        assertThat(calendarTrigger.getMinutes(), is("2"));
        assertThat(calendarTrigger.getDaysType(), is("MONTH"));
        assertThat(calendarTrigger.getWeekDays(), is(empty()));
        assertThat(calendarTrigger.getMonthDays(), is("1"));
    }

    @Test
    public void should_map_entity_common_fields_to_form() throws Exception {
        JobForm.Builder form = new JobForm.Builder();
        mJobFormMapper.mapEntityCommonFieldsToForm(form, mPreparedEntity);
        mJobFormMapper.mapEntitySourceToForm(form, mPreparedEntity);
        mJobFormMapper.mapEntityDestinationToForm(form, mPreparedEntity);
        mJobFormMapper.mapEntityFormatsToForm(form, mPreparedEntity);

        JobForm expected = form.build();
        assertThat(expected.getLabel(), is("label"));
        assertThat(expected.getVersion(), is(100));
        assertThat(expected.getDescription(), is("description"));
        assertThat(expected.getOutputFormats(), hasItem(JobOutputFormat.PDF));
        assertThat(expected.getRepositoryDestination().getFolderUri(), is("/folder/uri"));
        assertThat(expected.getBaseOutputFilename(), is("file.txt"));


        JobSource source = expected.getSource();
        assertThat(source.getUri(), is("/my/uri"));
        assertThat(source.getParameters(), hasItem(new ReportParameter("key", Collections.singleton("value"))));
    }

    @Test
    public void should_map_none_trigger_type_as_simple_one() throws Exception {
        JobSimpleTriggerEntity simpleTrigger = new JobSimpleTriggerEntity();
        simpleTrigger.setOccurrenceCount(1);
        simpleTrigger.setStartDate(START_DATE_SRC);
        simpleTrigger.setTimezone(TIME_ZONE.getID());
        mPreparedEntity.setSimpleTrigger(simpleTrigger);

        JobForm expected = mJobFormMapper.toDataForm(mPreparedEntity);

        assertThat(expected.getStartDate(), is(START_DATE));
        assertThat(expected.getTimeZone(), is(TIME_ZONE));
        assertThat(expected.getTrigger(), is(nullValue()));
    }

    @Test
    public void should_map_simple_trigger_with_occurrence_count() throws Exception {
        JobSimpleTriggerEntity simpleTrigger = new JobSimpleTriggerEntity();
        simpleTrigger.setOccurrenceCount(1);
        simpleTrigger.setRecurrenceIntervalUnit("DAY");
        simpleTrigger.setCalendarName("Gregorian");
        simpleTrigger.setRecurrenceInterval(100);
        mPreparedEntity.setSimpleTrigger(simpleTrigger);

        JobForm expected = mJobFormMapper.toDataForm(mPreparedEntity);
        Trigger trigger = expected.getTrigger();

        IntervalRecurrence recurrence = (IntervalRecurrence) trigger.getRecurrence();
        RepeatedEndDate endDate = (RepeatedEndDate) trigger.getEndDate();

        assertThat(recurrence.getInterval(), is(100));
        assertThat(recurrence.getUnit(), is(RecurrenceIntervalUnit.DAY));
        assertThat(endDate.getOccurrenceCount(), is(1));
    }

    @Test
    public void should_map_simple_trigger_with_end_date() throws Exception {
        JobSimpleTriggerEntity simpleTrigger = new JobSimpleTriggerEntity();
        simpleTrigger.setOccurrenceCount(-1);
        simpleTrigger.setRecurrenceInterval(1);
        simpleTrigger.setRecurrenceIntervalUnit("DAY");
        simpleTrigger.setEndDate(END_DATE_SRC);

        mPreparedEntity.setSimpleTrigger(simpleTrigger);

        JobForm expected = mJobFormMapper.toDataForm(mPreparedEntity);
        Trigger trigger = expected.getTrigger();

        IntervalRecurrence recurrence = (IntervalRecurrence) trigger.getRecurrence();
        UntilEndDate endDate = (UntilEndDate) trigger.getEndDate();

        assertThat(recurrence.getInterval(), is(1));
        assertThat(recurrence.getUnit(), is(RecurrenceIntervalUnit.DAY));
        assertThat(endDate.getSpecifiedDate(), is(END_DATE));
    }

    @Test
    public void should_map_calendar_trigger_with_all_type() throws Exception {
        JobCalendarTriggerEntity calendarTrigger = new JobCalendarTriggerEntity();
        calendarTrigger.setMonths(new HashSet<>(Arrays.asList(Calendar.FEBRUARY + 1)));
        calendarTrigger.setMinutes("30");
        calendarTrigger.setHours("3");
        calendarTrigger.setDaysType("ALL");
        calendarTrigger.setEndDate(END_DATE_SRC);

        mPreparedEntity.setCalendarTrigger(calendarTrigger);

        JobForm expected = mJobFormMapper.toDataForm(mPreparedEntity);
        Trigger trigger = expected.getTrigger();

        CalendarRecurrence calendarRecurrence = (CalendarRecurrence) trigger.getRecurrence();
        UntilEndDate endDate = (UntilEndDate) trigger.getEndDate();

        assertThat(calendarRecurrence.getMinutes().toString(), is("30"));
        assertThat(calendarRecurrence.getHours().toString(), is("3"));
        assertThat(calendarRecurrence.getMonths(), hasItems(Calendar.FEBRUARY));
        DaysInWeek daysType = (DaysInWeek) calendarRecurrence.getDaysType();
        assertThat(daysType.getDays(), hasItems(Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY));
    }

    @Test
    public void should_map_calendar_trigger_with_week_type() throws Exception {
        JobCalendarTriggerEntity calendarTrigger = new JobCalendarTriggerEntity();
        calendarTrigger.setMonths(new HashSet<>(Arrays.asList(Calendar.FEBRUARY)));
        calendarTrigger.setWeekDays(new HashSet<>(Arrays.asList(Calendar.MONDAY)));
        calendarTrigger.setDaysType("WEEK");

        mPreparedEntity.setCalendarTrigger(calendarTrigger);

        JobForm expected = mJobFormMapper.toDataForm(mPreparedEntity);
        Trigger trigger = expected.getTrigger();

        CalendarRecurrence calendarRecurrence = (CalendarRecurrence) trigger.getRecurrence();

        DaysInWeek daysType = (DaysInWeek) calendarRecurrence.getDaysType();
        assertThat(daysType.getDays(), hasItems(Calendar.MONDAY));
    }

    @Test
    public void should_map_calendar_trigger_with_month_type() throws Exception {
        JobCalendarTriggerEntity calendarTrigger = new JobCalendarTriggerEntity();
        calendarTrigger.setMonths(new HashSet<>(Arrays.asList(Calendar.FEBRUARY)));
        calendarTrigger.setDaysType("MONTH");
        calendarTrigger.setMonthDays("1");

        mPreparedEntity.setCalendarTrigger(calendarTrigger);

        JobForm expected = mJobFormMapper.toDataForm(mPreparedEntity);
        Trigger trigger = expected.getTrigger();

        CalendarRecurrence calendarRecurrence = (CalendarRecurrence) trigger.getRecurrence();

        DaysInMonth daysType = (DaysInMonth) calendarRecurrence.getDaysType();
        assertThat(daysType.toString(), is("1"));
    }
}