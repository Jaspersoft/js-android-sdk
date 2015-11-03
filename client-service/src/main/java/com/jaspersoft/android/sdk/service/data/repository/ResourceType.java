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

package com.jaspersoft.android.sdk.service.data.repository;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public enum ResourceType {
    folder,
    reportUnit,
    dashboard,
    legacyDashboard,
    file,
    semanticLayerDataSource,
    jndiJdbcDataSource,
    unknown {
        private String rawValue;

        @Override
        void setRawValue(String value) {
            rawValue = value;
        }

        @Override
        public String getRawValue() {
            return rawValue;
        }
    };

    public static Parser defaultParser() {
        return DefaultTypeParser.INSTANCE;
    }

    void setRawValue(String value) {
        throw new UnsupportedOperationException();
    }

    public String getRawValue() {
        return String.valueOf(this);
    }

    public interface Parser {
        ResourceType parse(String rawVersion);
    }
}
