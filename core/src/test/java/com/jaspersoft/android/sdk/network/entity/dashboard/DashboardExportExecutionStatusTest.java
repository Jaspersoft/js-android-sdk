package com.jaspersoft.android.sdk.network.entity.dashboard;

import com.google.gson.annotations.Expose;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static com.jaspersoft.android.sdk.test.matcher.HasAnnotation.hasAnnotation;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
@RunWith(JUnitParamsRunner.class)
public class DashboardExportExecutionStatusTest {
    @Test
    @Parameters({
            "id",
            "progress",
            "status",
    })
    public void shouldHaveExposeAnnotationForField(String fieldName) throws NoSuchFieldException {
        Field field = DashboardExportExecutionStatus.class.getDeclaredField(fieldName);
        assertThat(field, hasAnnotation(Expose.class));
    }
}
