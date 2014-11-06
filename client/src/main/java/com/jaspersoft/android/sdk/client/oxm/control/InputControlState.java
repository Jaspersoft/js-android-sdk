/*
 * Copyright (C) 2012-2014 Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/mobile-sdk-android
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of Jaspersoft Mobile SDK for Android.
 *
 * Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Jaspersoft Mobile SDK for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.client.oxm.control;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * @author Ivan Gadzhega
 * @since 1.4
 */
@Root(name="state", strict=false)
public class InputControlState implements Parcelable {

    @Element
    private String id;

    @Element
    private String uri;

    @Element(required=false)
    private String value;

    @Element(required=false)
    private String error;

    @ElementList(required=false, empty=false)
    private List<InputControlOption> options;

    public InputControlState() {}

    //---------------------------------------------------------------------
    // Parcelable
    //---------------------------------------------------------------------

    public InputControlState(Parcel source) {
        this.id = source.readString();
        this.uri = source.readString();
        this.value = source.readString();
        this.error = source.readString();
        this.options = source.createTypedArrayList(InputControlOption.CREATOR);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public InputControlState createFromParcel(Parcel source) {
            return new InputControlState(source);
        }

        public InputControlState[] newArray(int size) {
            return new InputControlState[size];
        }
    };

    @Override
    public int describeContents(){
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(uri);
        dest.writeString(value);
        dest.writeString(error);
        dest.writeTypedList(options);
    }

    //---------------------------------------------------------------------
    // Getters & Setters
    //---------------------------------------------------------------------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<InputControlOption> getOptions() {
        return options;
    }

    public void setOptions(List<InputControlOption> options) {
        this.options = options;
    }

}
