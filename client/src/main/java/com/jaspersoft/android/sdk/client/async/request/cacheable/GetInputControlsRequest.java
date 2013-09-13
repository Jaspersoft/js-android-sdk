/*
 * Copyright (C) 2012-2013 Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/mobile-sdk-android
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of Jaspersoft Mobile SDK for Android.
 *
 * Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Jaspersoft Mobile SDK for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.client.async.request.cacheable;

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.oxm.control.InputControlsList;
import com.jaspersoft.android.sdk.client.oxm.report.ReportParameter;

import java.util.List;

/**
 * Request that gets the list of input controls for the report with specified URI
 * and according to specified parameters.
 *
 * @author Ivan Gadzhega
 * @since 1.6
 */
public class GetInputControlsRequest extends BaseInputControlsRequest<InputControlsList> {

    /**
     * Creates a new instance of {@link GetInputControlsRequest}.
     *
     * @param reportUri repository URI of the report.
     */
    public GetInputControlsRequest(JsRestClient jsRestClient, String reportUri) {
        super(jsRestClient, reportUri, InputControlsList.class);
    }

    /**
     * Creates a new instance of {@link GetInputControlsRequest}.
     *
     * @param reportUri repository URI of the report.
     * @param controlsIds list of input controls IDs.
     * @param selectedValues list of selected values.
     */
    public GetInputControlsRequest(JsRestClient jsRestClient, String reportUri,
                                   List<String> controlsIds, List<ReportParameter> selectedValues) {
        super(jsRestClient, reportUri, controlsIds, selectedValues, InputControlsList.class);
    }


    @Override
    public InputControlsList loadDataFromNetwork() throws Exception {
        return getJsRestClient().getInputControlsList(getReportUri(), getControlsIds(), getSelectedValues());
    }

}
