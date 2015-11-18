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
 * @since 2.3
 */
public final class StatusCodes {
    public static final int ERROR = 1;
    public static final int NETWORK_ERROR = 2;
    public static final int CLIENT_ERROR = 3;
    public static final int INTERNAL_ERROR = 4;
    public static final int PERMISSION_ERROR = 5;
    public static final int AUTHORIZATION_ERROR = 6;

    // EXPORT
    public static final int EXPORT_PAGE_OUT_OF_RANGE = 100;
    public static final int EXPORT_EXECUTION_CANCELLED = 101;
    public static final int EXPORT_EXECUTION_FAILED = 102;

    // REPORT
    public static final int REPORT_EXECUTION_CANCELLED = 201;
    public static final int REPORT_EXECUTION_FAILED = 202;
}
