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

package com.jaspersoft.android.sdk.network.entity.execution;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class ReportParameter {
    @Expose
    private final String name;

    @Expose
    @SerializedName("value")
    private final Set<String> values;

    private ReportParameter(String name, Set<String> values) {
        this.name = name;
        this.values = values;
    }

    @SuppressWarnings("unchecked")
    public static ReportParameter emptyParameter(String name) {
        return create(name, Collections.EMPTY_SET);
    }

    public static ReportParameter create(String name, Set<String> values) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("Name should not be null");
        }
        if (values == null) {
            throw new IllegalArgumentException("Values should not be null. Otherwise use ReportParameter.emptyParameter()");
        }
        return new ReportParameter(name, values);
    }

    public String getName() {
        return name;
    }

    public Set<String> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return "ReportParameter{" +
                "name='" + name + '\'' +
                ", values=" + Arrays.toString(values.toArray()) +
                '}';
    }
}
