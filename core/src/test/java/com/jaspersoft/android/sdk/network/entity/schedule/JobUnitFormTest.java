package com.jaspersoft.android.sdk.network.entity.schedule;

import com.google.gson.annotations.Expose;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import static com.jaspersoft.android.sdk.test.matcher.HasAnnotation.hasAnnotation;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnitParamsRunner.class)
public class JobUnitFormTest {
    @Test
    @Parameters({
            "label",
            "description",
            "baseOutputFilename",
            "source",
            "repositoryDestination",
            "outputFormats",
            "trigger",
    })
    public void shouldHaveExposeAnnotationForField(String fieldName) throws NoSuchFieldException {
        Field field = JobForm.class.getDeclaredField(fieldName);
        assertThat(field, hasAnnotation(Expose.class));
    }

    @Test
    public void testEquals() throws Exception {
        EqualsVerifier.forClass(JobForm.class).verify();
    }
}