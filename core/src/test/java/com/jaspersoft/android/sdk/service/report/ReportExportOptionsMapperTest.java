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

package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.service.data.server.ServerVersion;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

@RunWith(JUnitParamsRunner.class)
public class ReportExportOptionsMapperTest {
    private static final String BASE_URL = "http://localhost";

    @Test
    public void should_create_6_2_mapper() throws Exception {
        ExportOptionsMapper mapper = ExportOptionsMapper.create(ServerVersion.v6_2, BASE_URL);
        assertThat(mapper, is(instanceOf(ExportOptionsMapper6_2.class)));
    }

    @Test
    public void should_create_5_5_mapper() throws Exception {
        ExportOptionsMapper mapper = ExportOptionsMapper.create(ServerVersion.v5_5, BASE_URL);
        assertThat(mapper, is(instanceOf(ExportOptionsMapper5_5.class)));
    }

    @Test
    @Parameters({
            "v5_6",
            "v5_6_1",
            "v6",
            "v6_0_1",
            "v6_1",
            "v6_1_1",
    })
    public void should_create_default_mapper_for(String rawVersion) throws Exception {
        ServerVersion version = (ServerVersion) ServerVersion.class.getField(rawVersion).get(null);
        ExportOptionsMapper mapper = ExportOptionsMapper.create(version, BASE_URL);
        assertThat(mapper, is(instanceOf(ExportOptionsMapper5_6and6_1.class)));
    }
}