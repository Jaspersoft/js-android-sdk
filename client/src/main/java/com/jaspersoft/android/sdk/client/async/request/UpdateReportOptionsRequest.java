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

package com.jaspersoft.android.sdk.client.async.request;

import com.jaspersoft.android.sdk.client.JsRestClient;

import java.util.Map;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class UpdateReportOptionsRequest extends BaseRequest<Void> {
    private final String mReportUri;
    private final String mOptionId;
    private final Map<String, Set<String>> mControlsValue;

    public UpdateReportOptionsRequest(JsRestClient jsRestClient, String reportUri,
                                      String optionId, Map<String, Set<String>> controlsValues) {
        super(jsRestClient, Void.class);
        mReportUri = reportUri;
        mOptionId = optionId;
        mControlsValue = controlsValues;
    }

    @Override
    public Void loadDataFromNetwork() throws Exception {
        getJsRestClient().updateReportOption(mReportUri, mOptionId, mControlsValue);
        // We do not care about response
        return null;
    }
}
