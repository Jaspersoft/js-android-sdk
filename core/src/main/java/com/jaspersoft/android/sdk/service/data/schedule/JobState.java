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

/**
 * @author Tom Koptel
 * @since 2.3
 */
public enum JobState {
    /**
     * The job is scheduled.
     */
    NORMAL,
    /**
     * The server is generating the report output.
     */
    EXECUTING,
    /**
     * The server has finished running the job and placed output to the repository.
     */
    COMPLETE,
    /**
     * The job has been disabled. Click Enabled to resume the schedule.
     */
    PAUSED,
    /**
     * The scheduler encountered an error while scheduling or triggering the job. This doesn’t include
     * cases where the job is successfully triggered, but an error occurs while it runs.
     */
    ERROR,
    /**
     * The scheduler encountered an error with the job trigger.
     */
    UNKNOWN
}
