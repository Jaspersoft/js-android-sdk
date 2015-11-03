/*
 * Copyright � 2015 TIBCO Software, Inc. All rights reserved.
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

package com.jaspersoft.android.sdk.network.entity.report.option;

import com.google.gson.annotations.Expose;

import org.jetbrains.annotations.NotNull;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class ReportOption {

    @Expose
    private String uri;
    @Expose
    private String id;
    @Expose
    private String label;

    @NotNull
    public String getId() {
        return id;
    }

    @NotNull
    public String getLabel() {
        return label;
    }

    @NotNull
    public String getUri() {
        return uri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReportOption that = (ReportOption) o;

        if (!uri.equals(that.uri)) return false;
        if (!id.equals(that.id)) return false;
        return label.equals(that.label);

    }

    @Override
    public int hashCode() {
        int result = uri.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + label.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ReportOption{" +
                "id='" + id + '\'' +
                ", uri='" + uri + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}

