/*
 * Copyright (C) 2015 TIBCO Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/mobile-sdk-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile SDK for Android.
 *
 * TIBCO Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile SDK for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.network.entity.type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jaspersoft.android.sdk.network.entity.control.InputControlCollection;

/**
 * @author Tom Koptel
 * @since 2.3
 */
final class InputControlTypeAdapterFactory extends CustomizedTypeAdapterFactory<InputControlCollection> {
    private static final String[] RULE_TYPES = {"dateTimeFormatValidationRule", "mandatoryValidationRule"};

    public InputControlTypeAdapterFactory() {
        super(InputControlCollection.class);
    }

    @Override
    protected JsonElement afterRead(JsonElement deserialized) {
        JsonObject jsonObject = deserialized.getAsJsonObject();
        JsonArray controls = jsonObject.getAsJsonArray("inputControl");
        if (controls != null) {
            int count = controls.size();
            JsonObject jsonControl;

            for (int i = 0; i < count; i++) {
                jsonControl = controls.get(i).getAsJsonObject();
                unwrapValidationRule(jsonControl);
            }
        }
        return jsonObject;
    }

    private void unwrapValidationRule(JsonObject jsonControl) {
        JsonArray rules = jsonControl.getAsJsonArray("validationRules");
        if (rules != null) {
            int count = rules.size();

            JsonObject rule, newRule;
            JsonArray tmpArray = new JsonArray();
            for (int i = 0; i < count; i++) {
                rule = rules.get(i).getAsJsonObject();
                newRule = unwrapRule(rule);
                tmpArray.add(newRule);
            }
            jsonControl.remove("validationRules");
            jsonControl.add("validationRules", tmpArray);
        }
    }

    private JsonObject unwrapRule(JsonObject rule) {
        return defineRuleType(rule);
    }

    private JsonObject defineRuleType(JsonObject rule) {
        JsonObject tmp = null;
        for (String type : RULE_TYPES) {
            tmp = rule.getAsJsonObject(type);
            if (tmp != null) {
                tmp.addProperty("type", type);
                defineRuleValue(tmp);
                break;
            }
        }

        if (tmp == null) {
            throw new IllegalStateException("Failed to define type for validation rule: " + rule);
        }

        return tmp;
    }

    private void defineRuleValue(JsonObject tmp) {
        String type = tmp.get("type").getAsString();
        if (type.equals("dateTimeFormatValidationRule")) {
            String format = tmp.get("format").getAsString();
            tmp.remove("format");
            tmp.addProperty("value", format);
        }
    }


}
