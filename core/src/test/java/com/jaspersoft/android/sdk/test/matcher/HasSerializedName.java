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

package com.jaspersoft.android.sdk.test.matcher;

import com.google.gson.annotations.SerializedName;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.Field;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public final class HasSerializedName extends TypeSafeMatcher<Field> {
    private final String mValue;

    private HasSerializedName(String value) {
        mValue = value;
    }

    @Override
    protected boolean matchesSafely(Field item) {
        SerializedName annotation = item.getAnnotation(SerializedName.class);
        return annotation.value().equals(mValue);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("annotation <" + SerializedName.class + "> has value: " + mValue);
    }

    @Factory
    public static <T> Matcher<Field> hasSerializedName(String value) {
        return new HasSerializedName(value);
    }
}
