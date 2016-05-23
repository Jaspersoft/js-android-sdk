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

package com.jaspersoft.android.sdk.service.report;

import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.data.report.PageRange;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ReportExecutionOptionsTest {
    @Test
    public void testNewBuilder() throws Exception {
        boolean freshData = true;
        boolean saveSnapshot = true;
        boolean interactive = true;
        boolean ignorePagination = true;
        boolean allowInlineScripts = true;
        String key = "key";
        String prefix = "./";
        String anchor = "anchor";
        ReportMarkup embeddable = ReportMarkup.EMBEDDABLE;
        ReportFormat format = ReportFormat.PDF;
        PageRange range = PageRange.parse("1-10");
        List<ReportParameter> params = Collections.<ReportParameter>emptyList();

        ReportExecutionOptions criteria = ReportExecutionOptions.builder()
                .withFreshData(freshData)
                .withSaveSnapshot(saveSnapshot)
                .withInteractive(interactive)
                .withIgnorePagination(ignorePagination)
                .withAllowInlineScripts(allowInlineScripts)
                .withTransformerKey(key)
                .withAttachmentPrefix(prefix)
                .withAnchor(anchor)
                .withMarkupType(embeddable)
                .withFormat(format)
                .withPageRange(range)
                .withParams(params)
                .build();
        ReportExecutionOptions newCriteria = criteria.newBuilder().build();
        assertThat("Failed to map 'freshData'", newCriteria.getFreshData(), is(freshData));
        assertThat("Failed to map 'saveSnapshot'", newCriteria.getSaveSnapshot(), is(saveSnapshot));
        assertThat("Failed to map 'interactive'", newCriteria.getInteractive(), is(interactive));
        assertThat("Failed to map 'ignorePagination'", newCriteria.getIgnorePagination(), is(ignorePagination));
        assertThat("Failed to map 'allowInlineScripts'", newCriteria.getAllowInlineScripts(), is(allowInlineScripts));
        assertThat("Failed to map 'key'", newCriteria.getTransformerKey(), is(key));
        assertThat("Failed to map 'prefix'", newCriteria.getAttachmentPrefix(), is(prefix));
        assertThat("Failed to map 'anchor'", newCriteria.getAnchor(), is(anchor));
        assertThat("Failed to map 'embeddable'", newCriteria.getMarkupType(), is(embeddable));
        assertThat("Failed to map 'format'", newCriteria.getFormat(), is(format));
        assertThat("Failed to map 'range'", newCriteria.getPageRange(), is(range));
        assertThat("Failed to map 'params'", newCriteria.getParams(), is(params));
    }

    @Test
    public void testEquals() throws Exception {
        EqualsVerifier.forClass(ReportExecutionOptions.class).verify();
    }
}