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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class RunExportCriteria extends ExecutionCriteria {
    private RunExportCriteria(boolean freshData, boolean interactive, boolean saveSnapshot, Format format, String pages, String attachmentPrefix) {
        super(freshData, interactive, saveSnapshot, format, pages, attachmentPrefix);
    }

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean freshData;
        private boolean interactive;
        private boolean saveSnapshot;
        private Format format;
        private String pages;
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


        public Builder attachmentPrefix(String prefix) {
            this.attachmentPrefix = prefix;
            return this;
        }

        public RunExportCriteria create() {
            return new RunExportCriteria(freshData, interactive, saveSnapshot, format, pages, attachmentPrefix);
        }
    }
}
