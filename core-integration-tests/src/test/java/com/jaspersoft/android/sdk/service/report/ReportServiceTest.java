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

import com.jaspersoft.android.sdk.env.JrsEnvironmentRule;
import com.jaspersoft.android.sdk.env.ReportTestBundle;
import com.jaspersoft.android.sdk.service.data.report.PageRange;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.3
 */
@RunWith(JUnitParamsRunner.class)
public class ReportServiceTest {
    @ClassRule
    public static JrsEnvironmentRule sEnv = new JrsEnvironmentRule();

    @Test
    @Parameters(method = "reports")
    public void report_service_should_export(ReportTestBundle bundle) throws Exception {
        ReportExecution execution = runReport(bundle);

        ReportExportOptions exportOptions = ReportExportOptions.builder()
                .withPageRange(PageRange.parse("1"))
                .withFormat(ReportFormat.HTML)
                .build();

        ReportExport export = execution.export(exportOptions);
        List<ReportAttachment> attachments = export.getAttachments();
        for (ReportAttachment reportAttachment : attachments) {
            reportAttachment.download();
        }
        export.download();
    }

    @Test
    @Parameters(method = "reports")
    public void report_service_should_await_complete_event(ReportTestBundle bundle) throws Exception {
        ReportExecution execution = runReport(bundle);
        execution.waitForReportCompletion();
    }

    @Test
    @Parameters(method = "reports")
    public void report_service_should_apply_parameters(ReportTestBundle bundle) throws Exception {
        if (bundle.hasParams()) {
            ReportExecution execution = runReport(bundle);
            execution.updateExecution(bundle.getParams());
        }
    }

    @Test
    @Parameters(method = "reports")
    public void report_service_should_refresh(ReportTestBundle bundle) throws Exception {
        if (bundle.hasParams()) {
            ReportExecution execution = runReport(bundle);
            execution.refresh();
        }
    }

    private ReportExecution runReport(ReportTestBundle bundle) throws ServiceException {
        ReportService reportService = ReportService.newService(bundle.getClient());

        ReportExecutionOptions executionOptions = ReportExecutionOptions.builder()
                .withFormat(ReportFormat.HTML)
                .withFreshData(true)
                .withInteractive(true)
                .build();
        return reportService.run(bundle.getUri(), executionOptions);
    }

    private Object[] reports() {
        return sEnv.listReports();
    }
}
