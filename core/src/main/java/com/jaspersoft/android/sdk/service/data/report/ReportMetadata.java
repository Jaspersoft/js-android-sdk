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

package com.jaspersoft.android.sdk.service.data.report;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class ReportMetadata {
    private final String uri;
    private final int totalPages;

    public ReportMetadata(String uri, int totalPages) {
        this.uri = uri;
        this.totalPages = totalPages;
    }

    public String getUri() {
        return uri;
    }

    public int getTotalPages() {
        return totalPages;
    }

    @Override
    public String toString() {
        return "ReportMetadata{" +
                "uri='" + uri + '\'' +
                ", totalPages=" + totalPages +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReportMetadata that = (ReportMetadata) o;

        if (totalPages != that.totalPages) return false;
        if (uri != null ? !uri.equals(that.uri) : that.uri != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = uri != null ? uri.hashCode() : 0;
        result = 31 * result + totalPages;
        return result;
    }
}
