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
 * Corresponding class encapsulates error codes that SDK could encounter
 *
 * @author Tom Koptel
 * @since 2.3
 */
public final class StatusCodes {
    public static final int UNDEFINED_ERROR = 100;
    /**
     * Describes issue related to missing network connection
     */
    public static final int NETWORK_ERROR = 102;
    /**
     * Used to describe issue related to client
     */
    public static final int CLIENT_ERROR = 103;
    /**
     * Internal Server error
     */
    public static final int INTERNAL_ERROR = 104;
    /**
     * Requested resources not available for current session
     */
    public static final int PERMISSION_DENIED_ERROR = 105;
    /**
     * Request failed with authorization error
     */
    public static final int AUTHORIZATION_ERROR = 106;

    // EXPORT
    /**
     * Requested export page is out of range
     */
    public static final int EXPORT_PAGE_OUT_OF_RANGE = 200;
    /**
     * Requested export execution was cancelled either by client or JRS
     */
    public static final int EXPORT_EXECUTION_CANCELLED = 201;
    /**
     * Requested export execution failed on the JRS side
     */
    public static final int EXPORT_EXECUTION_FAILED = 202;

    // REPORT
    /**
     * Requested report execution was cancelled either by client or JRS
     */
    public static final int REPORT_EXECUTION_CANCELLED = 301;
    /**
     * Requested report execution failed on the JRS side
     */
    public static final int REPORT_EXECUTION_FAILED = 302;
    /**
     * Session has been deprecated therefore associated execution invalid
     */
    public static final int REPORT_EXECUTION_INVALID = 303;

    // RESOURCE
    /**
     * API responds with corresponding error in the case when resource not found.
     */
    public static final int RESOURCE_NOT_FOUND = 400;

    // SCHEDULE
    /**
     * Job was created/updated with output file name duplicated
     */
    public static final int JOB_DUPLICATE_OUTPUT_FILE_NAME = 500;
    /**
     * Job was created/updated with invalid start data
     */
    public static final int JOB_START_DATE_IN_THE_PAST = 501;
    /**
     * Job was created/updated with invalid chars set for output filename
     */
    public static final int JOB_OUTPUT_FILENAME_INVALID_CHARS = 502;
    /**
     * Job was created/updated with missing output folder
     */
    public static final int JOB_OUTPUT_FOLDER_DOES_NOT_EXIST = 503;
    /**
     * Job was not saved because output folder locked
     */
    public static final int JOB_OUTPUT_FOLDER_IS_NOT_WRITABLE = 504;
    /**
     * Job was created/updated with label that exceed 100 characters
     */
    public static final int JOB_LABEL_TOO_LONG = 505;
    /**
     * Job was created/updated with output filename that exceed 100 characters
     */
    public static final int JOB_OUTPUT_FILENAME_TOO_LONG = 506;
    /**
     * Job was created/updated with calendar trigger and empty months
     */
    public static final int JOB_TRIGGER_MONTHS_EMPTY = 507;
   /**
     * Job was created/updated with calendar trigger and empty week days
     */
    public static final int JOB_TRIGGER_WEEK_DAYS_EMPTY = 508;
   /**
     * Job trigger configuration incorrect we can not create schedule
     */
    public static final int JOB_CREATION_INTERNAL_ERROR = 509;

    // FILTERS
    /**
     * Report option already exists in repository
     */
    public static final int SAVED_VALUES_EXIST_IN_FOLDER = 600;
    /**
     * Report option label too long
     */
    public static final int SAVED_VALUES_LABEL_TOO_LONG = 601;

    private StatusCodes() {}
}
