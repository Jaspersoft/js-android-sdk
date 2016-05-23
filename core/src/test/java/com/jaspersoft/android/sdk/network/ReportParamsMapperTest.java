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

package com.jaspersoft.android.sdk.network;

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;


public class ReportParamsMapperTest {
    @Test
    public void testToMap() throws Exception {
        ReportParameter reportParameter1 = new ReportParameter("a", Collections.singleton("b"));
        ReportParameter reportParameter2 = new ReportParameter("c", Collections.singleton("d"));
        Map<String, Set<String>> params = ReportParamsMapper.INSTANCE.toMap(
                Arrays.asList(reportParameter1, reportParameter2));

        Set<String> values = new HashSet<>();
        for (Set<String> sets : params.values()) {
            values.addAll(sets);
        }
        assertThat(params.keySet(), hasItems("a", "c"));
        assertThat(values, hasItems("b", "d"));
    }
}