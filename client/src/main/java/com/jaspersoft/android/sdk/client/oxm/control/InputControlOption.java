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

package com.jaspersoft.android.sdk.client.oxm.control;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * @author Ivan Gadzhega
 * @since 1.4
 */
@Root(name="option", strict=false)
public class InputControlOption implements Parcelable {

    @Expose
    @Element
    private String label;

    @Expose
    @Element
    private String value;

    @Expose
    @Element
    private boolean selected;

    public InputControlOption() { }

    public InputControlOption(String label, String value) {
        this(label, value, false);
    }

    public InputControlOption(String label, String value, boolean selected) {
        this.label = label;
        this.value = value;
        this.selected = selected;
    }

    //---------------------------------------------------------------------
    // Parcelable
    //---------------------------------------------------------------------

    public InputControlOption(Parcel source) {
        this.label = source.readString();
        this.value = source.readString();
        this.selected = source.readByte() != 0;
    }

    public static final Parcelable.Creator<InputControlOption> CREATOR = new Parcelable.Creator<InputControlOption>() {
        public InputControlOption createFromParcel(Parcel source) {
            return new InputControlOption(source);
        }

        public InputControlOption[] newArray(int size) {
            return new InputControlOption[size];
        }
    };

    @Override
    public int describeContents(){
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label);
        dest.writeString(value);
        dest.writeByte((byte) (selected ? 1 : 0));
    }

    //---------------------------------------------------------------------
    // Getters & Setters
    //---------------------------------------------------------------------

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return label;
    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (selected ? 1 : 0);
        return result;
    }
}
