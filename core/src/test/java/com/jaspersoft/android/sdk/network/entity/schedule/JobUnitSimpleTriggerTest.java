package com.jaspersoft.android.sdk.network.entity.schedule;

import com.google.gson.annotations.Expose;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import static com.jaspersoft.android.sdk.test.matcher.HasAnnotation.hasAnnotation;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnitParamsRunner.class)
public class JobUnitSimpleTriggerTest {
    @Test
    @Parameters({
            "timezone",
            "calendarName",
            "startType",
            "startDate",
            "endDate",
            "misfireInstruction",
    })
    public void shouldHaveExposeAnnotationForSubclassFields(String fieldName) throws NoSuchFieldException {
        Field field = JobTriggerEntity.class.getDeclaredField(fieldName);
        assertThat(field, hasAnnotation(Expose.class));
    }

    @Test
    @Parameters({
            "occurrenceCount",
            "recurrenceInterval",
            "recurrenceIntervalUnit",
    })
    public void shouldHaveExposeAnnotationForFields(String fieldName) throws NoSuchFieldException {
        Field field = JobSimpleTriggerEntity.class.getDeclaredField(fieldName);
        assertThat(field, hasAnnotation(Expose.class));
    }
}