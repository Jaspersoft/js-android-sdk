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
package com.jaspersoft.android.sdk.service.report;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Map;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class RunReportCriteria extends ExecutionCriteria {
    private final Map<String, Set<String>> mParams;

    private RunReportCriteria(boolean freshData,
                                boolean interactive,
                                boolean saveSnapshot,
                                Format format,
                                String pages,
                                String attachmentPrefix,
                                Map<String, Set<String>> params
                                ) {
        super(freshData, interactive, saveSnapshot, format, pages, attachmentPrefix);
        mParams = params;
    }

    @NonNull
    public static Builder builder() {
        return new Builder();
    }

    @Nullable
    public Map<String, Set<String>> getParams() {
        return mParams;
    }

    public static class Builder {
        private boolean freshData;
        private boolean interactive;
        private boolean saveSnapshot;
        private Format format;
        private String pages;
        public Map<String, Set<String>> params;
        public String attachmentPrefix;

        public Builder() {
            interactive = true;
        }

        public Builder freshData(boolean freshData) {
            this.freshData = freshData;
            return this;
        }

        public Builder interactive(boolean interactive) {
            this.interactive = interactive;
            return this;
        }

        public Builder saveSnapshot(boolean saveSnapshot) {
            this.saveSnapshot = saveSnapshot;
            return this;
        }

        public Builder format(Format format) {
            this.format = format;
            return this;
        }

        public Builder pages(@Nullable String pages) {
            this.pages = pages;
            return this;
        }

        public Builder params(Map<String, Set<String>> params) {
            this.params = params;
            return this;
        }

        public Builder attachmentPrefix(String prefix) {
            this.attachmentPrefix = prefix;
            return this;
        }

        public RunReportCriteria create() {
            return new RunReportCriteria(freshData, interactive, saveSnapshot, format, pages, attachmentPrefix, params);
        }
    }
}
