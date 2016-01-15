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
public class RepositoryDestinationTest {
    @Test
    @Parameters({
            "reportUnitURI",
    })
    public void shouldHaveExposeAnnotationForField(String fieldName) throws NoSuchFieldException {
        Field field = JobSource.class.getDeclaredField(fieldName);
        assertThat(field, hasAnnotation(Expose.class));
    }
}