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

import com.jaspersoft.android.sdk.service.internal.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public final class JobOwner {
    private static final String SEPARATOR = "|";

    @NotNull
    private final String mUsername;
    @Nullable
    private final String mOrganization;

    @TestOnly
    JobOwner(@NotNull String username, String organization) {
        mUsername = username;
        mOrganization = organization;
    }

    public static JobOwner newOwner(@NotNull String username, @Nullable String organization) {
        Preconditions.checkNotNull(username, "Username should not be null");
        return new JobOwner(username, organization);
    }

    public static JobOwner newOwner(@NotNull String rawOwner) {
        Preconditions.checkNotNull(rawOwner, "Raw owner should not be null");
        if (rawOwner.contains("|")) {
            String[] split = rawOwner.split("\\|");
            return newOwner(split[0], split[1]);
        }
        return newOwner(rawOwner, null);
    }

    @NotNull
    public String getUsername() {
        return mUsername;
    }

    @Nullable
    public String getOrganization() {
        return mOrganization;
    }

    @Override
    public String toString() {
        if (mOrganization == null) {
            return mUsername;
        }
        return mUsername + SEPARATOR + mOrganization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JobOwner jobOwner = (JobOwner) o;

        if (mOrganization != null ? !mOrganization.equals(jobOwner.mOrganization) : jobOwner.mOrganization != null)
            return false;
        if (!mUsername.equals(jobOwner.mUsername)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mUsername.hashCode();
        result = 31 * result + (mOrganization != null ? mOrganization.hashCode() : 0);
        return result;
    }
}
