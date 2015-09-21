package com.jaspersoft.android.sdk.network.entity.server;

import com.google.gson.annotations.Expose;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static com.jaspersoft.android.sdk.test.matcher.HasAnnotation.hasAnnotation;
import static com.jaspersoft.android.sdk.test.matcher.HasSerializedName.hasSerializedName;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(JUnitParamsRunner.class)
public class EncryptionKeyTest {
    @Test
    @Parameters({
            "error",
            "exponent",
            "maxdigits",
            "modulus",
    })
    public void shouldHaveExposeAnnotationForField(String fieldName) throws NoSuchFieldException {
        Field field = EncryptionKey.class.getDeclaredField(fieldName);
        assertThat(field, hasAnnotation(Expose.class));
    }

    @Test
    @Parameters({
            "error|Error",
            "exponent|e",
            "modulus|n",
    })
    public void optionsFieldShouldHaveSerializedNameAnnotationForField(String fieldName, String serializeName) throws NoSuchFieldException {
        Field field = EncryptionKey.class.getDeclaredField(fieldName);
        assertThat(field, hasSerializedName(serializeName));
    }
}