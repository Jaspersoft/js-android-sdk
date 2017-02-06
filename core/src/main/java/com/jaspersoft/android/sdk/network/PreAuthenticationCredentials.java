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
import org.jetbrains.annotations.TestOnly;

import java.io.IOException;

import static com.jaspersoft.android.sdk.service.internal.Preconditions.checkNotNull;

/**
 * Concrete implementation of abstract {@link Credentials}.
 * Corresponding class wrapper introduces fields required to initiate
 * Using a Pre-Authentication Token on JRS side.
 *
 * @author Andrew Tivodar
 * @since 2.6
 */
public final class PreAuthenticationCredentials extends Credentials {
    private final String parameterName;
    private final String token;

    @TestOnly
    PreAuthenticationCredentials(String parameterName, String token) {
        this.parameterName = parameterName;
        this.token = token;
    }

    public static Builder builder() {
        return new Builder();
    }

    @NotNull
    public String getParameterName() {
        return parameterName;
    }

    @NotNull
    public String getToken() {
        return token;
    }

    @Override
    protected void apply(AuthStrategy policy) throws IOException, HttpException {
        policy.apply(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PreAuthenticationCredentials that = (PreAuthenticationCredentials) o;

        if (parameterName != null ? !parameterName.equals(that.parameterName) : that.parameterName != null)
            return false;
        return token != null ? token.equals(that.token) : that.token == null;

    }

    @Override
    public int hashCode() {
        int result = parameterName != null ? parameterName.hashCode() : 0;
        result = 31 * result + (token != null ? token.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PreAuthenticationCredentials{" +
                "parameterName='" + parameterName + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    public static class Builder {
        private String parameterName;
        private String token;

        private Builder() {}

        /**
         * Setter to provide parameterName
         *
         * @param parameterName  The default parameter name for a pre authentication token is "pp".
         *                       This parameter name can be changed in the configuration file applicationContext-externalAuth-preAuth.xml.
         * @return builder for convenient configuration
         */
        public Builder withParameterName(@NotNull String parameterName) {
            this.parameterName = checkNotNull(parameterName, "username == null");
            return this;
        }

        /**
         * Setter to provide token
         *
         * @param token The token for your pre-authentication mechanism.
         * @return builder for convenient configuration
         */
        public Builder withToken(@NotNull String token) {
            this.token = checkNotNull(token, "token == null");
            return this;
        }

        @NotNull
        public PreAuthenticationCredentials build() {
            ensureValidState();
            ensureDefaults();
            return new PreAuthenticationCredentials(
                    parameterName,
                    token);
        }

        private void ensureDefaults() {
            if (parameterName == null) {
                parameterName = "pp";
            }
        }

        private void ensureValidState() {
            if (token == null) {
                throw new IllegalStateException("Token should not be null");
            }
        }
    }
}
