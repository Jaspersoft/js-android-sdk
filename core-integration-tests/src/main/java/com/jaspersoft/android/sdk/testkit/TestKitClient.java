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

package com.jaspersoft.android.sdk.testkit;

import com.jaspersoft.android.sdk.testkit.exception.HttpException;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.net.Proxy;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public final class TestKitClient {
    private final GetResourcesUrisUseCase mGetResourcesUrisUseCase;
    private final GetReportParametersUseCase mGetReportParametersUseCase;

    TestKitClient(
            GetResourcesUrisUseCase getResourcesUrisUseCase,
            GetReportParametersUseCase getReportParametersUseCase) {
        mGetResourcesUrisUseCase = getResourcesUrisUseCase;
        mGetReportParametersUseCase = getReportParametersUseCase;
    }

    public List<String> getResourcesUris(ListResourcesUrisCommand command) throws IOException, HttpException {
        return mGetResourcesUrisUseCase.execute(command);
    }

    public Map<String, Set<String>> resourceParameter(ListReportParamsCommand command) throws IOException, HttpException {
        return mGetReportParametersUseCase.execute(command);
    }

    public static TestKitClient newClient(String baseUrl, Proxy proxy) {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setCookieHandler(LocalCookieManager.get());
        if (proxy != null) {
            okHttpClient.setProxy(proxy);
        }

        GetResourcesUrisUseCase getResourcesUrisUseCase = new GetResourcesUrisUseCase(okHttpClient, baseUrl);
        GetReportParametersUseCase getReportParametersUseCase = new GetReportParametersUseCase(okHttpClient, baseUrl);

        return new TestKitClient(getResourcesUrisUseCase, getReportParametersUseCase);
    }
}
