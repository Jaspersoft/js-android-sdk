/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile for Android.
 *
 * TIBCO Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.service.filter;

import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.network.InputControlRestApi;
import com.jaspersoft.android.sdk.network.entity.control.InputControl;
import com.jaspersoft.android.sdk.network.entity.control.InputControlState;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.0
 */
class ReportControlsUseCase {

    private final ServiceExceptionMapper mExceptionMapper;
    private final InputControlRestApi mRestApi;

    public ReportControlsUseCase(ServiceExceptionMapper exceptionMapper, InputControlRestApi restApi) {
        mExceptionMapper = exceptionMapper;
        mRestApi = restApi;
    }

    public List<InputControl> requestControls(String reportUri, Set<String> ids, boolean excludeState) throws ServiceException {
        try {
            return mRestApi.requestInputControls(reportUri, ids, excludeState);
        } catch (HttpException e) {
            throw mExceptionMapper.transform(e);
        } catch (IOException e) {
            throw mExceptionMapper.transform(e);
        }
    }

    public List<InputControlState> requestControlsValues(String reportUri,
                                                         List<ReportParameter> parameters,
                                                         boolean fresh) throws ServiceException {
        try {
            return mRestApi.requestInputControlsStates(reportUri, parameters, fresh);
        } catch (HttpException e) {
            throw mExceptionMapper.transform(e);
        } catch (IOException e) {
            throw mExceptionMapper.transform(e);
        }
    }

    public List<InputControlState> requestResourceValues(String resourceUri, boolean freshData) throws ServiceException {
        try {
            return mRestApi.requestInputControlsInitialStates(resourceUri, freshData);
        } catch (HttpException e) {
            throw mExceptionMapper.transform(e);
        } catch (IOException e) {
            throw mExceptionMapper.transform(e);
        }
    }
}
