package com.jaspersoft.android.sdk.network.entity.control;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.jaspersoft.android.sdk.network.entity.type.GsonFactory;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static com.jaspersoft.android.sdk.test.matcher.HasAnnotation.hasAnnotation;
import static com.jaspersoft.android.sdk.test.matcher.HasSerializedName.hasSerializedName;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class InputControlCollectionTest {

    Gson mGson = GsonFactory.create();

    @Test
    public void shouldHaveExposeAnnotationForFieldControls() throws NoSuchFieldException {
        Field field = InputControlCollection.class.getDeclaredField("mValues");
        assertThat(field, hasAnnotation(Expose.class));
    }

    @Test
    public void valuesFieldShouldHaveSerializedNameAnnotationForFieldControls() throws NoSuchFieldException {
        Field field = InputControlCollection.class.getDeclaredField("mValues");
        assertThat(field, hasSerializedName("inputControl"));
    }

    @Test
    public void shouldProperlyAdaptMasterDependencies() {
        String json = "{\"inputControl\" : [ {\"masterDependencies\":[\"Country_multi_select\",\"Cascading_state_multi_select\"]}]}";
        InputControl inputControl = deserializeInputControl(json);

        Set<String> deps = inputControl.getMasterDependencies();

        assertThat(deps, hasItem("Cascading_state_multi_select"));
    }

    @Test
    public void shouldProperlyAdaptSlaveDependencies() {
        String json = "{\"inputControl\" : [ {\"slaveDependencies\":[\"Country_multi_select\",\"Cascading_state_multi_select\"]}]}";
        InputControl inputControl = deserializeInputControl(json);

        Set<String> deps = inputControl.getSlaveDependencies();

        assertThat(deps, hasItem("Cascading_state_multi_select"));
    }

    @Test
    public void shouldProperlyAdaptDateTimeValidationRule() {
        String json = "{\"inputControl\" : [ {\"validationRules\" : [ { \"dateTimeFormatValidationRule\" : { \"errorMessage\" : \"This field is mandatory so you must enter data.\", \"format\": \"YYYY-mm-dd\" } }]} ]}";
        InputControl inputControl = deserializeInputControl(json);

        ValidationRule rule = (ValidationRule) inputControl.getValidationRules().toArray()[0];

        assertThat(rule.getErrorMessage(), is("This field is mandatory so you must enter data."));
        assertThat(rule.getType(), is("dateTimeFormatValidationRule"));
        assertThat(rule.getValue(), is("YYYY-mm-dd"));
    }

    @Test
    public void shouldProperlyAdaptMandatoryValidationRule() {
        String json = "{\"inputControl\" : [ {\"validationRules\" : [ { \"mandatoryValidationRule\" : { \"errorMessage\" : \"This field is mandatory so you must enter data.\" } }]} ]}";
        InputControl inputControl = deserializeInputControl(json);

        ValidationRule rule = (ValidationRule) inputControl.getValidationRules().toArray()[0];

        assertThat(rule.getErrorMessage(), is("This field is mandatory so you must enter data."));
        assertThat(rule.getType(), is("mandatoryValidationRule"));
    }
    private InputControl deserializeInputControl(String json) {
        Type type = new TypeToken<Collection<InputControl>>() {
        }.getType();
        Collection<InputControl> response = mGson.fromJson(json, type);
        return new ArrayList<>(response).get(0);
    }
}