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

import com.jaspersoft.android.sdk.service.data.report.PageRange;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ReportExportOptionsTest {

    private ReportExportOptions.Builder criteriaBuilder;

    @Before
    public void setUp() throws Exception {
        criteriaBuilder = ReportExportOptions.builder().withFormat(ReportFormat.PDF);
    }

    @Test(expected = IllegalStateException.class)
    public void should_not_build_with_null_format() throws Exception {
        ReportExportOptions.builder().build();
    }

    @Test(expected = NullPointerException.class)
    public void should_not_accept_null_format() throws Exception {
        criteriaBuilder.withFormat(null).build();
    }


    @Test
    public void testGetFormat() throws Exception {
        ReportExportOptions criteria = criteriaBuilder.withFormat(ReportFormat.PDF).build();
        assertThat(criteria.getFormat(), is(ReportFormat.PDF));
    }

    @Test
    public void testGetPages() throws Exception {
        PageRange pageRange = PageRange.parse("1-10");
        ReportExportOptions criteria = criteriaBuilder.withPageRange(pageRange).build();
        assertThat(criteria.getPageRange(), is(pageRange));
    }

    @Test
    public void testGetAttachmentPrefix() throws Exception {
        ReportExportOptions criteria = criteriaBuilder.withAttachmentPrefix("./").build();
        assertThat(criteria.getAttachmentPrefix(), is("./"));
    }

    @Test
    public void testGetAnchor() throws Exception {
        ReportExportOptions criteria = criteriaBuilder.withAnchor("anchor").build();
        assertThat(criteria.getAnchor(), is("anchor"));
    }

    @Test
    public void testGetIgnorePagination() throws Exception {
        ReportExportOptions criteria = criteriaBuilder.withIgnorePagination(true).build();
        assertThat(criteria.getIgnorePagination(), is(true));
    }

    @Test
    public void testGetAllowInlineScripts() throws Exception {
        ReportExportOptions criteria = criteriaBuilder.withAllowInlineScripts(true).build();
        assertThat(criteria.getAllowInlineScripts(), is(true));
    }

    @Test
    public void testEquals() throws Exception {
        EqualsVerifier.forClass(ReportExportOptions.class).verify();
    }
}