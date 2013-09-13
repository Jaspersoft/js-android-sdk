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
import com.jaspersoft.android.sdk.client.oxm.control.InputControl;
import com.jaspersoft.android.sdk.client.oxm.control.InputControlStatesList;
import com.jaspersoft.android.sdk.client.oxm.report.ReportParameter;

import java.util.List;

/**
 * Request that validates input controls values on the server side
 * and returns states only for invalid controls.
 *
 * @author Ivan Gadzhega
 * @since 1.6
 */
public class ValidateInputControlsValuesRequest extends BaseInputControlsRequest<InputControlStatesList> {

    /**
     * Creates a new instance of {@link ValidateInputControlsValuesRequest}.
     *
     * @param reportUri repository URI of the report
     * @param inputControls list of input controls that should be validated
     */
    public ValidateInputControlsValuesRequest(JsRestClient jsRestClient, String reportUri, List<InputControl> inputControls) {
        super(jsRestClient, reportUri, inputControls, InputControlStatesList.class);
    }

    /**
     * Creates a new instance of {@link ValidateInputControlsValuesRequest}.
     *
     * @param reportUri repository URI of the report
     * @param controlsIds list of input controls IDs that should be validated
     * @param selectedValues list of selected values for validation
     */
    public ValidateInputControlsValuesRequest(JsRestClient jsRestClient, String reportUri, List<String> controlsIds,
                                              List<ReportParameter> selectedValues) {
        super(jsRestClient, reportUri, controlsIds, selectedValues, InputControlStatesList.class);
    }


    @Override
    public InputControlStatesList loadDataFromNetwork() throws Exception {
        return getJsRestClient().validateInputControlsValuesList(getReportUri(), getControlsIds(), getSelectedValues());
    }

    @Override
    protected String createCacheKeyTag() {
        return TAG_IC_VALUES;
    }

}
