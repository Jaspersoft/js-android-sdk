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
package com.jaspersoft.android.sdk.service.exception;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class ExecutionFailedException extends ExecutionException {
    private ExecutionFailedException(String message) {
        super(message);
    }

    private ExecutionFailedException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public static ExecutionFailedException forReportExport(String reportUri) {
        return new ExecutionFailedException(String.format("Export for report '%s' failed on server side", reportUri));
    }

    public static ExecutionFailedException forReport(String reportUri) {
        return new ExecutionFailedException(String.format("Report '%s' failed on server side", reportUri));
    }

    public static ExecutionFailedException forReportExport(String reportUri, Throwable throwable) {
        String message = String.format("Export for report '%s' failed. Reason: %s", reportUri, throwable.getMessage());
        return new ExecutionFailedException(message);
    }

    public static ExecutionFailedException forReport(String reportUri, Throwable throwable) {
        String message = String.format("Report '%s' execution failed. Reason: %s", reportUri, throwable.getMessage());
        return new ExecutionFailedException(message);
    }
}
