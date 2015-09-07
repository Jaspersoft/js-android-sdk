package com.jaspersoft.android.sdk.network.entity.control;

import com.google.gson.annotations.Expose;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static com.jaspersoft.android.sdk.test.matcher.HasAnnotation.hasAnnotation;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(JUnitParamsRunner.class)
public class InputControlTest {
    @Test
    @Parameters({
            "id",
            "label",
            "mandatory",
            "readOnly",
            "type",
            "uri",
            "visible",
            "masterDependencies",
            "slaveDependencies",
            "validationRules",
            "state",
    })
    public void shouldHaveExposeAnnotationForField(String fieldName) throws NoSuchFieldException {
        Field field = InputControl.class.getDeclaredField(fieldName);
        assertThat(field, hasAnnotation(Expose.class));
    }
}