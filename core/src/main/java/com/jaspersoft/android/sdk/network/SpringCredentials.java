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

package com.jaspersoft.android.sdk.network;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.io.IOException;
import java.util.Locale;
import java.util.TimeZone;

import static com.jaspersoft.android.sdk.service.internal.Preconditions.checkNotNull;

/**
 * Concrete implementation of abstract {@link Credentials}.
 * Corresponding class wrapper introduces fields required to initiate
 * Spring authorization on JRS side.
 *
 * @author Tom Koptel
 * @since 2.3
 */
public final class SpringCredentials extends Credentials {
    private final String mUsername;
    private final String mPassword;
    private final String mOrganization;
    private final Locale mLocale;
    private final TimeZone mTimeZone;

    @TestOnly
    SpringCredentials(
            @NotNull String username,
            @NotNull String password,
            @Nullable String organization,
            @NotNull Locale locale,
            @NotNull TimeZone timeZone) {
        mUsername = username;
        mPassword = password;
        mOrganization = organization;
        mLocale = locale;
        mTimeZone = timeZone;
    }

    public static Builder builder() {
        return new Builder();
    }

    @NotNull
    public String getUsername() {
        return mUsername;
    }

    @NotNull
    public String getPassword() {
        return mPassword;
    }

    @Nullable
    public String getOrganization() {
        return mOrganization;
    }

    @NotNull
    public TimeZone getTimeZone() {
        return mTimeZone;
    }

    @NotNull
    public Locale getLocale() {
        return mLocale;
    }

    @Override
    protected void apply(AuthStrategy policy) throws IOException, HttpException {
        policy.apply(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpringCredentials that = (SpringCredentials) o;

        if (mUsername != null ? !mUsername.equals(that.mUsername) : that.mUsername != null)
            return false;
        if (mPassword != null ? !mPassword.equals(that.mPassword) : that.mPassword != null)
            return false;
        if (mOrganization != null ? !mOrganization.equals(that.mOrganization) : that.mOrganization != null)
            return false;
        if (mLocale != null ? !mLocale.equals(that.mLocale) : that.mLocale != null) return false;
        return !(mTimeZone != null ? !mTimeZone.equals(that.mTimeZone) : that.mTimeZone != null);
    }

    @Override
    public int hashCode() {
        int result = mUsername != null ? mUsername.hashCode() : 0;
        result = 31 * result + (mPassword != null ? mPassword.hashCode() : 0);
        result = 31 * result + (mOrganization != null ? mOrganization.hashCode() : 0);
        result = 31 * result + (mLocale != null ? mLocale.hashCode() : 0);
        result = 31 * result + (mTimeZone != null ? mTimeZone.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SpringCredentials{" +
                "mLocale=" + mLocale +
                ", mUsername='" + mUsername + '\'' +
                ", mOrganization='" + mOrganization + '\'' +
                ", mTimeZone=" + mTimeZone +
                '}';
    }

    public static class Builder {
        private String mUsername;
        private String mPassword;
        private String mOrganization;

        // Optional
        private Locale mLocale;
        private TimeZone mTimeZone;

        private Builder() {}

        /**
         * Setter to provide username
         *
         * @param username used for authentication on JRS
         * @return builder for convenient configuration
         */
        public Builder withUsername(@NotNull String username) {
            mUsername = checkNotNull(username, "username == null");
            return this;
        }

        /**
         * Setter to provide password
         *
         * @param password used for authentication on JRS
         * @return builder for convenient configuration
         */
        public Builder withPassword(@NotNull String password) {
            mPassword = checkNotNull(password, "password == null");
            return this;
        }

        /**
         * Setter to provide organization. May be null value
         *
         * @param organization used for authentication on JRS
         * @return builder for convenient configuration
         */
        public Builder withOrganization(@Nullable String organization) {
            mOrganization = organization;
            return this;
        }

        /**
         * Setter to provide time zone. May be null value.
         * You can provide time zone, so that JRS would respect it while generating appropriate dates.
         * Used system time zone by default.
         *
         * @param timeZone your timezone
         * @return builder for convenient configuration
         */
        public Builder withTimeZone(@NotNull TimeZone timeZone) {
            mTimeZone = checkNotNull(timeZone, "timeZone == null");
            return this;
        }

        /**
         * Setter to provide locale. May be null value.
         * Used system locale by default.
         *
         * @param locale you locale
         * @return builder for convenient configuration
         */
        public Builder withLocale(@NotNull Locale locale) {
            mLocale = checkNotNull(locale, "locale == null");
            return this;
        }

        @NotNull
        public SpringCredentials build() {
            ensureValidState();
            ensureDefaults();
            return new SpringCredentials(
                    mUsername,
                    mPassword,
                    mOrganization,
                    mLocale,
                    mTimeZone);
        }

        private void ensureDefaults() {
            if (mTimeZone == null) {
                mTimeZone = TimeZone.getDefault();
            }
            if (mLocale == null) {
                mLocale = Locale.getDefault();
            }
        }

        private void ensureValidState() {
            if (mUsername == null) {
                throw new IllegalStateException("Username should not be null");
            }
            if (mPassword == null) {
                throw new IllegalStateException("Password should not be null");
            }
        }
    }
}
