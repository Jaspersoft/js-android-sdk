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

import com.jaspersoft.android.sdk.network.entity.execution.AttachmentDescriptor;
import com.jaspersoft.android.sdk.network.entity.execution.ExportDescriptor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        ExportDescriptor.class,
        AttachmentDescriptor.class,
})
public class AttachmentsFactoryTest {

    private static final String EXEC_ID = "exec_id";
    private static final String EXPORT_ID = "export_id";
    private static final String ATTACHMENT_ID = "img.png";

    @Mock
    ExportExecutionApi mExportExecutionApi;
    @Mock
    ExportDescriptor mExportDescriptor;
    @Mock
    AttachmentDescriptor mAttachmentDescriptor;

    private AttachmentsFactory attachmentsFactory;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        attachmentsFactory = new AttachmentsFactory(mExportExecutionApi);
    }

    @Test
    public void testCreate() throws Exception {
        when(mExportDescriptor.getId()).thenReturn(EXPORT_ID);
        when(mAttachmentDescriptor.getFileName()).thenReturn(ATTACHMENT_ID);
        when(mExportDescriptor.getAttachments()).thenReturn(Collections.singleton(mAttachmentDescriptor));

        List<ReportAttachment> result = attachmentsFactory.create(mExportDescriptor, EXEC_ID);
        assertThat(result.size(), is(1));

        verify(mExportDescriptor).getId();
        verify(mExportDescriptor).getAttachments();
        verify(mAttachmentDescriptor).getFileName();
    }
}