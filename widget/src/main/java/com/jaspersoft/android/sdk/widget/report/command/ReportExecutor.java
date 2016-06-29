package com.jaspersoft.android.sdk.widget.report.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.service.data.report.PageRange;
import com.jaspersoft.android.sdk.service.data.report.ReportExportOutput;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.report.ReportExecution;
import com.jaspersoft.android.sdk.service.report.ReportExecutionOptions;
import com.jaspersoft.android.sdk.service.report.ReportExport;
import com.jaspersoft.android.sdk.service.report.ReportExportOptions;
import com.jaspersoft.android.sdk.service.report.ReportFormat;
import com.jaspersoft.android.sdk.service.report.ReportService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class ReportExecutor {
    private final ReportService reportService;
    private ReportExecution reportExecution;

    public ReportExecutor(ReportService reportService) {
        this.reportService = reportService;
    }

    public void execute(String reportUri, List<ReportParameter> parameters) throws ServiceException {
        ReportExecutionOptions reportExecutionOptions = defineRunOptions(parameters);
        reportExecution = reportService.run(reportUri, reportExecutionOptions);
    }

    public String export(int page) throws ServiceException, IOException {
        ReportExportOptions exportOptions = ReportExportOptions.builder()
                .withPageRange(PageRange.parse(String.valueOf(page)))
                .withFormat(ReportFormat.HTML)
                .build();

        ReportExport export = reportExecution.export(exportOptions);
        ReportExportOutput output = export.download();
        String htmlPage = parseReportDocument(output.getStream());
        return toJson(htmlPage);
    }

    public void updateParams(List<ReportParameter> params) throws ServiceException {
        reportExecution.updateExecution(params);
    }

    private ReportExecutionOptions defineRunOptions(List<ReportParameter> parameters) {
        return ReportExecutionOptions.builder()
                .withFormat(ReportFormat.HTML)
                .withFreshData(false)
                .withInteractive(true)
                .withSaveSnapshot(true)
                .withParams(parameters)
                .build();
    }

    private String parseReportDocument(InputStream is) throws IOException {
        StringBuilder htmlBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

        String line = bufferedReader.readLine();
        while (line != null) {
            htmlBuilder.append(line);
            htmlBuilder.append('\n');
            line = bufferedReader.readLine();
        }

        return htmlBuilder.toString();
    }

    private String toJson(Object object) {
        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .create();
        return gson.toJson(object);
    }
}
