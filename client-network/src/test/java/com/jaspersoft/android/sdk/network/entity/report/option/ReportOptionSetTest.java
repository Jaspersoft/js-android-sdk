package com.jaspersoft.android.sdk.network.entity.report.option;

import com.google.gson.annotations.Expose;

import org.junit.Test;

import java.lang.reflect.Field;

import static com.jaspersoft.android.sdk.test.matcher.HasAnnotation.hasAnnotation;
import static com.jaspersoft.android.sdk.test.matcher.HasSerializedName.hasSerializedName;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ReportOptionSetTest {
    @Test
    public void shouldHaveExposeAnnotationForFieldOptions() throws NoSuchFieldException {
        Field field = ReportOptionSet.class.getDeclaredField("mOptions");
        assertThat(field, hasAnnotation(Expose.class));
    }

    @Test
    public void optionsFieldShouldHaveSerializedNameAnnotationForField() throws NoSuchFieldException {
        Field field = ReportOptionSet.class.getDeclaredField("mOptions");
        assertThat(field, hasSerializedName("reportOptionsSummary"));
    }
}