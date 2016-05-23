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

package com.jaspersoft.android.sdk.service.data.schedule;

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.internal.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class JobSource {
    private final String mUri;
    private final List<ReportParameter> mParameters;

    JobSource(String uri, List<ReportParameter> parameters) {
        mUri = uri;
        mParameters = parameters;
    }

    @NotNull
    public String getUri() {
        return mUri;
    }

    @NotNull
    public List<ReportParameter> getParameters() {
        return mParameters;
    }

    public static class Builder {
        private String mUri;
        private List<ReportParameter> mParameters = Collections.emptyList();

        public Builder() {
            mParameters = new ArrayList<>();
        }

        /**
         * Allows to specify resource uri that will be used to schedule job
         *
         * @param uri unique identifier of resources in JRS domain
         * @return builder for convenient configuration
         */
        public Builder withUri(@NotNull String uri) {
            mUri = Preconditions.checkNotNull(uri, "Source uri should not be null");
            return this;
        }

        /**
         * Allows to specify report params that specify exact data requested by user
         *
         * @param parameters list of key/value pair where key corresponds to control state id and value represented by set of values
         * @return builder for convenient configuration
         */
        public Builder withParameters(@Nullable List<ReportParameter> parameters) {
            if (parameters != null) {
                mParameters = Collections.unmodifiableList(parameters);
            }
            return this;
        }

        public JobSource build() {
            assertState();
            return new JobSource(mUri, mParameters);
        }

        private void assertState() {
            Preconditions.checkNotNull(mUri, "Source can not be created without uri");
        }
    }
}
