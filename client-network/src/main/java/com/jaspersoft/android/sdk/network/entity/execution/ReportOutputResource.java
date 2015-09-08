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

package com.jaspersoft.android.sdk.network.entity.execution;

import com.google.gson.annotations.Expose;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class ReportOutputResource {
    @Expose
    private String contentType;
    @Expose
    private String fileName;

    public String getContentType() {
        return contentType;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReportOutputResource that = (ReportOutputResource) o;

        if (contentType != null ? !contentType.equals(that.contentType) : that.contentType != null)
            return false;
        return !(fileName != null ? !fileName.equals(that.fileName) : that.fileName != null);

    }

    @Override
    public int hashCode() {
        int result = contentType != null ? contentType.hashCode() : 0;
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ReportOutputResource{" +
                "contentType='" + contentType + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
