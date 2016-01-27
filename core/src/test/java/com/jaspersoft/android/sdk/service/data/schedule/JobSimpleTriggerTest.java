package com.jaspersoft.android.sdk.service.data.schedule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.rules.ExpectedException.none;

public class JobSimpleTriggerTest {

    @Rule
    public ExpectedException expected = none();

    @Test
    public void occurrence_count_should_be_defined() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Occurrence count should be specified");

        new JobSimpleTrigger.Builder()
                .withRecurrenceInterval(2)
                .withRecurrenceIntervalUnit(RecurrenceIntervalUnit.DAY)
                .build();
    }

    @Test
    public void recurrence_count_should_be_defined() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Recurrence interval should be specified");

        new JobSimpleTrigger.Builder()
                .withOccurrenceCount(2)
                .withRecurrenceIntervalUnit(RecurrenceIntervalUnit.DAY)
                .build();
    }

    @Test
    public void interval_unit_should_be_defined() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Recurrent interval unit should be specified");

        new JobSimpleTrigger.Builder()
                .withOccurrenceCount(2)
                .withRecurrenceInterval(2)
                .build();
    }

    @Test
    public void null_interval_unit_will_be_rejected() throws Exception {
        expected.expect(NullPointerException.class);
        expected.expectMessage("Recurrent interval should not be null");

        new JobSimpleTrigger.Builder()
                .withOccurrenceCount(2)
                .withRecurrenceInterval(2)
                .withRecurrenceIntervalUnit(null)
                .build();
    }
}