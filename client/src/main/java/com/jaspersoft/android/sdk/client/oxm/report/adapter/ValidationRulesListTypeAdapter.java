/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of Jaspersoft Mobile for Android.
 *
 * Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.client.oxm.report.adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.jaspersoft.android.sdk.client.oxm.control.validation.DateTimeFormatValidationRule;
import com.jaspersoft.android.sdk.client.oxm.control.validation.MandatoryValidationRule;
import com.jaspersoft.android.sdk.client.oxm.control.validation.ValidationRule;
import com.jaspersoft.android.sdk.client.oxm.control.validation.ValidationRulesList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 1.10
 */
public class ValidationRulesListTypeAdapter extends TypeAdapter<ValidationRulesList> {
    private final Gson gson;

    public ValidationRulesListTypeAdapter() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();
    }

    @Override
    public ValidationRulesList read(JsonReader in) throws IOException {
        ValidationRulesList ruleList = new ValidationRulesList();
        List<ValidationRule> rules = new ArrayList<ValidationRule>();

        in.beginArray();
        while (in.hasNext()) {
            in.beginObject();
            while (in.hasNext()) {
                ValidationRule rule;
                if (in.nextName().equals("dateTimeFormatValidationRule")) {
                    rule = gson.fromJson(in, DateTimeFormatValidationRule.class);
                } else {
                    rule = gson.fromJson(in, MandatoryValidationRule.class);
                }
                rules.add(rule);
            }
            in.endObject();
        }
        in.endArray();

        ruleList.setValidationRules(rules);
        return ruleList;
    }

    @Override
    public void write(JsonWriter out, ValidationRulesList value) throws IOException {
    }
}
