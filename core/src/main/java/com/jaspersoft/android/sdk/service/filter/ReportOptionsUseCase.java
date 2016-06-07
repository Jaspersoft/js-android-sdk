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

package com.jaspersoft.android.sdk.service.filter;

import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.network.ReportOptionRestApi;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.network.entity.report.option.ReportOptionEntity;
import com.jaspersoft.android.sdk.service.data.report.option.ReportOption;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.ServiceExceptionMapper;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class ReportOptionsUseCase {
    private final ServiceExceptionMapper mExceptionMapper;
    private final ReportOptionRestApi mReportOptionRestApi;
    private final ReportOptionMapper mReportOptionMapper;

    ReportOptionsUseCase(ServiceExceptionMapper exceptionMapper, ReportOptionRestApi reportOptionRestApi, ReportOptionMapper reportOptionMapper) {
        mExceptionMapper = exceptionMapper;
        mReportOptionRestApi = reportOptionRestApi;
        mReportOptionMapper = reportOptionMapper;
    }

    public Set<ReportOption> requestReportOptionsList(String reportUnitUri) throws ServiceException {
        try {
            Set<ReportOptionEntity> entities = mReportOptionRestApi.requestReportOptionsList(reportUnitUri);
            return mReportOptionMapper.transform(entities);
        } catch (HttpException e) {
            throw mExceptionMapper.transform(e);
        } catch (IOException e) {
            throw mExceptionMapper.transform(e);
        }
    }

    public ReportOption createReportOption(String reportUri,
                                           String optionLabel,
                                           List<ReportParameter> parameters,
                                           boolean overwrite) throws ServiceException {
        try {
            ReportOptionEntity entity = mReportOptionRestApi.createReportOption(reportUri, optionLabel, parameters, overwrite);
            return mReportOptionMapper.transform(entity);
        } catch (HttpException e) {
            throw mExceptionMapper.transform(e);
        } catch (IOException e) {
            throw mExceptionMapper.transform(e);
        }
    }

    public void updateReportOption(String reportUri, String optionId, List<ReportParameter> parameters) throws ServiceException {
        try {
            mReportOptionRestApi.updateReportOption(reportUri, optionId, parameters);
        } catch (HttpException e) {
            throw mExceptionMapper.transform(e);
        } catch (IOException e) {
            throw mExceptionMapper.transform(e);
        }
    }

    public void deleteReportOption(String reportUri, String optionId) throws ServiceException {
        try {
            mReportOptionRestApi.deleteReportOption(reportUri, optionId);
        } catch (HttpException e) {
            throw mExceptionMapper.transform(e);
        } catch (IOException e) {
            throw mExceptionMapper.transform(e);
        }
    }
}
