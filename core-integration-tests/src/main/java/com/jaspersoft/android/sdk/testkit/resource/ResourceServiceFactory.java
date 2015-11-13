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

package com.jaspersoft.android.sdk.testkit.resource;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public final class ResourceServiceFactory {
    private final String mToken;
    private final String mBaseUrl;

    ResourceServiceFactory(String token, String baseUrl) {
        mToken = token;
        mBaseUrl = baseUrl;
    }

    public static Builder builder() {
        return new Builder();
    }

    public ResourceRepository resourceRepository(int count, String type) {
        return new ResourceRepository(mToken, mBaseUrl, count, type);
    }

    public ResourceParameter resourceParameter(String resourceUri) {
        return new ResourceParameter(mToken, mBaseUrl, resourceUri);
    }

    public static class Builder {
        private String mToken;
        private String mBaseUrl;

        private Builder() {
        }

        public Builder token(String token) {
            mToken = token;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            mBaseUrl = baseUrl;
            return this;
        }

        public ResourceServiceFactory create() {
            return new ResourceServiceFactory(mToken, mBaseUrl);
        }
    }
}
