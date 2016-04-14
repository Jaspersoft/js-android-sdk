package com.jaspersoft.android.sdk.network.entity.schedule;

import com.google.gson.annotations.Expose;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import static com.jaspersoft.android.sdk.test.matcher.HasAnnotation.hasAnnotation;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnitParamsRunner.class)
public class JobUnitTest {
    @Test
    @Parameters({
            "id",
            "version",
            "reportUnitURI",
            "label",
            "reportLabel",
            "owner",
            "state",
    })
    public void shouldHaveExposeAnnotationForField(String fieldName) throws NoSuchFieldException {
        Field field = JobUnitEntity.class.getDeclaredField(fieldName);
        assertThat(field, hasAnnotation(Expose.class));
    }

    @Test
    public void testEquals() throws Exception {
        EqualsVerifier.forClass(JobUnitEntity.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }
}