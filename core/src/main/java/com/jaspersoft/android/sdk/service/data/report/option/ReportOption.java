/*
 * Copyright (C) 2016 TIBCO Jaspersoft Corporation. All rights reserved.
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

package com.jaspersoft.android.sdk.service.data.report.option;

import com.jaspersoft.android.sdk.service.internal.Preconditions;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class ReportOption {
    @NotNull
    private final String id;
    @NotNull
    private final String uri;
    @NotNull
    private final String label;

    private ReportOption(@NotNull String id, @NotNull String uri, @NotNull String label) {
        this.id = id;
        this.uri = uri;
        this.label = label;
    }

    @NotNull
    public String getId() {
        return id;
    }

    @NotNull
    public String getUri() {
        return uri;
    }

    @NotNull
    public String getLabel() {
        return label;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReportOption)) return false;

        ReportOption that = (ReportOption) o;

        if (!id.equals(that.id)) return false;
        if (!label.equals(that.label)) return false;
        if (!uri.equals(that.uri)) return false;

        return true;
    }

    @Override
    public final int hashCode() {
        int result = id.hashCode();
        result = 31 * result + uri.hashCode();
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

    public static class Builder {
        private String mId;
        private String mUri;
        private String mLabel;

        public Builder withId(String id) {
            mId = Preconditions.checkNotNull(id, "Id should not be null");
            return this;
        }

        public Builder withUri(String uri) {
            mUri = Preconditions.checkNotNull(uri, "Uri should not be null");
            return this;
        }

        public Builder withLabel(String label) {
            mLabel = Preconditions.checkNotNull(label, "Label should not be null");
            return this;
        }

        public ReportOption build() {
            Preconditions.checkNotNull(mId, "Report option can not be created without id");
            Preconditions.checkNotNull(mUri, "Report option can not be created without uri");
            Preconditions.checkNotNull(mLabel, "Report option can not be created without label");
            return new ReportOption(mId, mUri, mLabel);
        }
    }
}
