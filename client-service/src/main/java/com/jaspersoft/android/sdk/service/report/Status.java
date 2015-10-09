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

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class Status {
    private final String mStatus;

    private Status(String status) {
        mStatus = status;
    }

    public static Status wrap(String status) {
        return new Status(status);
    }

    public boolean isQueued() {
        return mStatus.equals("queued");
    }

    public boolean isExecution() {
        return mStatus.equals("execution");
    }

    public boolean isCancelled() {
        return mStatus.equals("cancelled");
    }

    public boolean isFailed() {
        return mStatus.equals("failed");
    }

    public boolean isReady() {
        return mStatus.equals("ready");
    }
}
