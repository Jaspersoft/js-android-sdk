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

package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.network.entity.schedule.JobFormEntity;
import com.jaspersoft.android.sdk.service.data.schedule.JobForm;
import com.jaspersoft.android.sdk.service.data.schedule.JobSource;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class JobSourceMapperTest {

    private JobSourceMapper mapperUnderTest;
    private final JobFormFactory formFactory = new JobFormFactory();

    @Before
    public void setUp() throws Exception {
        mapperUnderTest = new JobSourceMapper();
    }

    @Test
    public void should_map_form_to_entity() throws Exception {
        List<ReportParameter> parameters = Collections.singletonList(
                new ReportParameter("key", Collections.singleton("value")));

        JobSource source = new JobSource.Builder()
                .withUri("/my/uri")
                .withParameters(parameters)
                .build();

        JobFormEntity mappedEntity = formFactory.givenNewJobFormEntity();
        JobForm preparedForm = formFactory.givenJobFormBuilderWithValues()
                .withJobSource(source)
                .build();

        mapperUnderTest.mapFormOnEntity(preparedForm, mappedEntity);

        assertThat(mappedEntity.getSourceUri(), is("/my/uri"));

        Map<String, Set<String>> params = mappedEntity.getSourceParameters();
        Collection<String> values = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : params.entrySet()) {
            values.addAll(entry.getValue());
        }

        assertThat(params.keySet(), hasItem("key"));
        assertThat(values, hasItem("value"));
    }

    @Test
    public void should_map_entity_to_form() throws Exception {
        JobFormEntity preparedEntity = formFactory.givenJobFormEntityWithValues();
        JobForm.Builder form = formFactory.givenJobFormBuilderWithValues();
        mapperUnderTest.mapEntityOnForm(form, preparedEntity);

        JobSource source = form.build().getSource();

        assertThat(source.getUri(), is("/my/uri"));
        assertThat(source.getParameters(), hasItem(new ReportParameter("key", Collections.singleton("value"))));
    }
}