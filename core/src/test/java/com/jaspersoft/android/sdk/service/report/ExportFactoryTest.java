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

import com.jaspersoft.android.sdk.network.entity.execution.ExportDescriptor;
import com.jaspersoft.android.sdk.network.entity.execution.ReportExecutionDescriptor;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.exception.StatusCodes;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static java.util.Collections.singleton;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        ReportExecutionDescriptor.class,
        ExportDescriptor.class,
})
public class ExportFactoryTest {

    private static final String EXEC_ID = "exec_id";
    private static final String EXPORT_ID = "export_id";

    @Mock
    ExportExecutionApi mExportExecutionApi;
    @Mock
    AttachmentsFactory mAttachmentsFactory;

    @Mock
    ReportExecutionDescriptor mReportExecutionDescriptor;
    @Mock
    ExportDescriptor mExportDescriptor;

    private ExportFactory exportFactory;

    @Before
    public void setUp() throws Exception {
        exportFactory = new ExportFactory(mExportExecutionApi, mAttachmentsFactory);

        when(mExportDescriptor.getId()).thenReturn(EXPORT_ID);
        when(mReportExecutionDescriptor.getExports()).thenReturn(singleton(mExportDescriptor));
    }

    @Test
    public void should_create_export_if_details_fulfilled() throws Exception {
        ReportExport result = exportFactory.create(mReportExecutionDescriptor, EXEC_ID, EXPORT_ID);
        assertThat(result, is(notNullValue()));
        assertThat(result, is(instanceOf(ReportExportImpl.class)));

        verify(mAttachmentsFactory).create(mExportDescriptor, EXEC_ID);
    }

    @Test
    public void should_throw_if_details_missing() throws Exception {
        when(mExportDescriptor.getId()).thenReturn("123122");

        try {
            exportFactory.create(mReportExecutionDescriptor, EXEC_ID, EXPORT_ID);
            fail("Should throw ServiceException");
        } catch (ServiceException ex) {
            assertThat(ex.getMessage(), is("Server returned malformed export details"));
            assertThat(ex.code(), is(StatusCodes.EXPORT_EXECUTION_FAILED));
        }
    }
}