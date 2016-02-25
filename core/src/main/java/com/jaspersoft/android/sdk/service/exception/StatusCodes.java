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

package com.jaspersoft.android.sdk.service.exception;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class StatusCodes {
    public static final int UNDEFINED_ERROR = 100;
    public static final int NETWORK_ERROR = 102;
    public static final int CLIENT_ERROR = 103;
    public static final int INTERNAL_ERROR = 104;
    public static final int PERMISSION_DENIED_ERROR = 105;
    public static final int AUTHORIZATION_ERROR = 106;

    // EXPORT
    public static final int EXPORT_PAGE_OUT_OF_RANGE = 200;
    public static final int EXPORT_EXECUTION_CANCELLED = 201;
    public static final int EXPORT_EXECUTION_FAILED = 202;

    // REPORT
    public static final int REPORT_EXECUTION_CANCELLED = 301;
    public static final int REPORT_EXECUTION_FAILED = 302;
    public static final int REPORT_EXECUTION_INVALID = 303;

    // RESOURCE
    public static final int RESOURCE_NOT_FOUND = 400;

    // SCHEDULE
    public static final int JOB_DUPLICATE_OUTPUT_FILE_NAME = 500;
    public static final int JOB_START_DATE_IN_THE_PAST = 501;
    public static final int JOB_OUTPUT_FILENAME_INVALID_CHARS = 502;
    public static final int JOB_OUTPUT_FOLDER_DOES_NOT_EXIST = 503;
    public static final int JOB_OUTPUT_FOLDER_IS_NOT_WRITABLE = 504;
    public static final int JOB_LABEL_TOO_LONG = 505;
    public static final int JOB_OUTPUT_FILENAME_TOO_LONG = 506;

    private StatusCodes() {}
}
