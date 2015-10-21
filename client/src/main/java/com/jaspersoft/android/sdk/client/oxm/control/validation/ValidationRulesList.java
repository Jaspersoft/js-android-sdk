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

package com.jaspersoft.android.sdk.client.oxm.control.validation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Gadzhega
 * @since 1.4
 */
@Root(name="validationRules", strict=false)
public class ValidationRulesList implements Parcelable {

    @Expose
    @ElementListUnion({
            @ElementList(entry="dateTimeFormatValidationRule", inline=true, type=DateTimeFormatValidationRule.class),
            @ElementList(entry="mandatoryValidationRule", inline=true, type=MandatoryValidationRule.class)
    })
    private List<ValidationRule> validationRules = new ArrayList<ValidationRule>();

    public ValidationRulesList() {}

    //---------------------------------------------------------------------
    // Parcelable
    //---------------------------------------------------------------------

    public ValidationRulesList(List<ValidationRule> validationRules) {
        this.validationRules = validationRules;
    }

    public ValidationRulesList(Parcel source) {
        validationRules = source.createTypedArrayList(ValidationRule.CREATOR);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ValidationRulesList createFromParcel(Parcel source) {
            return new ValidationRulesList(source);
        }

        public ValidationRulesList[] newArray(int size) {
            return new ValidationRulesList[size];
        }
    };

    @Override
    public int describeContents(){
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(validationRules);
    }

    //---------------------------------------------------------------------
    // Getters & Setters
    //---------------------------------------------------------------------

    public List<ValidationRule> getValidationRules() {
        return validationRules;
    }

    public void setValidationRules(List<ValidationRule> validationRules) {
        this.validationRules = validationRules;
    }

}
