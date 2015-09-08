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

package com.jaspersoft.android.sdk.network.api;

import android.support.annotation.NonNull;

import com.jaspersoft.android.sdk.network.entity.report.option.ReportOption;
import com.jaspersoft.android.sdk.network.entity.report.option.ReportOptionResponse;

import java.util.Map;
import java.util.Set;

import rx.Observable;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public interface ReportOptionRestApi {

    @NonNull
    Observable<ReportOptionResponse> requestReportOptionsList(@NonNull String reportUnitUri);

    @NonNull
    Observable<ReportOption> createReportOption(@NonNull String optionLabel,
                                                @NonNull Map<String, Set<String>> controlsValues,
                                                boolean overwrite);

    @NonNull
    Observable<Void> updateReportOption(@NonNull String reportUnitUri,
                                        @NonNull String optionId,
                                        @NonNull Map<String, Set<String>> controlsValues);

    @NonNull
    Observable<Void> deleteReportOption(@NonNull String reportUnitUri,
                                        @NonNull String optionId);

    final class Builder extends AuthBaseBuilder<ReportOptionRestApi, Builder> {
        public Builder(String baseUrl, String cookie) {
            super(baseUrl, cookie);
        }

        @Override
        ReportOptionRestApi createApi() {
            return new ReportOptionRestApiImpl(getDefaultBuilder().build());
        }
    }
}
