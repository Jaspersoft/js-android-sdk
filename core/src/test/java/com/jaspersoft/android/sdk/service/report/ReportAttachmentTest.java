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

import com.jaspersoft.android.sdk.network.ReportExportRestApi;
import com.jaspersoft.android.sdk.network.entity.export.OutputResource;
import com.jaspersoft.android.sdk.service.data.report.ResourceOutput;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author Tom Koptel
 * @since 2.3
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({OutputResource.class})
public class ReportAttachmentTest {

    private static final String EXEC_ID = "exec_id";
    private static final String EXPORT_ID = "export_id";
    private static final String ATTACHMENT_ID = "img.png";

    @Mock
    ReportExportRestApi mExportRestApi;
    @Mock
    ResourceOutput attachment;
    @Mock
    ExportExecutionApi mExportExecutionApi;

    private ReportAttachment objectUnderTest;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        objectUnderTest = new ReportAttachment(mExportExecutionApi, EXEC_ID, EXPORT_ID, ATTACHMENT_ID);
    }

    @Test
    public void testDownload() throws Exception {
        when(mExportExecutionApi.downloadAttachment(anyString(), anyString(), anyString())).thenReturn(attachment);

        ResourceOutput result = objectUnderTest.download();
        assertThat(result, is(notNullValue()));

        verify(mExportExecutionApi).downloadAttachment(EXEC_ID, EXPORT_ID, ATTACHMENT_ID);
        verifyNoMoreInteractions(mExportExecutionApi);
    }
}