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

/**
 * Incapsulates data related to SSH keys that are used for FTP authentication purpose
 *
 * @author Tom Koptel
 * @since 2.5
 */
public class JobFtpAuthKey {
    private final String keyPath;
    private final String passPhrase;

    private JobFtpAuthKey(@NotNull String keyPath, @Nullable String passPhrase) {
        this.keyPath = keyPath;
        this.passPhrase = passPhrase;
    }

    /**
     * Creates new pair
     *
     * @param path to the keys stored on JRS
     * @param passPhrase required to unlock secure connection to FTP server
     * @return newly created pair
     * @throws NullPointerException if path is null
     */
    public static JobFtpAuthKey newPair(@NotNull String path, @Nullable String passPhrase) {
        Preconditions.checkNotNull(path, "keyPath == null");
        Preconditions.checkNotNull(path, "passPhrase == null");
        return new JobFtpAuthKey(path, passPhrase);
    }

    /**
     * Path to the SSH keys stored on behalf of JRS instance
     *
     * @return path to the keys stored on JRS
     */
    @NotNull
    public String getKeyPath() {
        return keyPath;
    }

    /**
     * Pass phrase required to unlock secure connection to FTP server
     *
     * @return pass phrase can be anything
     */
    @Nullable
    public String getPassPhrase() {
        return passPhrase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JobFtpAuthKey that = (JobFtpAuthKey) o;

        if (passPhrase != null ? !passPhrase.equals(that.passPhrase) : that.passPhrase != null) return false;
        if (keyPath != null ? !keyPath.equals(that.keyPath) : that.keyPath != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = keyPath != null ? keyPath.hashCode() : 0;
        result = 31 * result + (passPhrase != null ? passPhrase.hashCode() : 0);
        return result;
    }
}
