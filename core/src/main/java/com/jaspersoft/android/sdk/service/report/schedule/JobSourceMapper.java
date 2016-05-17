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

package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.network.entity.schedule.JobFormEntity;
import com.jaspersoft.android.sdk.service.data.schedule.JobForm;
import com.jaspersoft.android.sdk.service.data.schedule.JobSource;

import java.util.*;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class JobSourceMapper extends JobMapper {
    final static JobSourceMapper INSTANCE = new JobSourceMapper();

    @Override
    public void mapFormOnEntity(JobForm form, JobFormEntity entity) {
        JobSource source = form.getSource();
        List<ReportParameter> params = source.getParameters();
        Map<String, Set<String>> values = mapSourceParamValues(params);
        entity.setSourceUri(source.getUri());
        entity.setSourceParameters(values);
    }

    private Map<String, Set<String>> mapSourceParamValues(List<ReportParameter> params) {
        Map<String, Set<String>> values = new HashMap<>(params.size());
        for (ReportParameter param : params) {
            values.put(param.getName(), param.getValue());
        }
        return values;
    }

    @Override
    public void mapEntityOnForm(JobForm.Builder form, JobFormEntity entity) {
        JobSource.Builder builder = new JobSource.Builder();
        builder.withUri(entity.getSourceUri());

        Map<String, Set<String>> parameters = entity.getSourceParameters();
        if (parameters != null) {
            List<ReportParameter> params = mapParams(parameters);
            builder.withParameters(params);
        }

        JobSource source = builder.build();
        form.withJobSource(source);
    }

    private List<ReportParameter> mapParams(Map<String, Set<String>> parameters) {
        List<ReportParameter> params = new ArrayList<>(parameters.size());
        for (Map.Entry<String, Set<String>> entry : parameters.entrySet()) {
            params.add(new ReportParameter(entry.getKey(), entry.getValue()));
        }
        return params;
    }
}
