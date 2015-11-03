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

package com.jaspersoft.android.sdk.test;

import com.jaspersoft.android.sdk.network.api.RestApiLog;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class TestLogger implements RestApiLog {

    private final Logger logger;

    private TestLogger(String logTarget) {
        logger = Logger.getLogger(logTarget);
    }

    public static TestLogger get(Object target) {
        return new TestLogger(target.getClass().getSimpleName());
    }

    @Override
    public void log(String message) {
        logger.log(Level.INFO, message);
    }
}
