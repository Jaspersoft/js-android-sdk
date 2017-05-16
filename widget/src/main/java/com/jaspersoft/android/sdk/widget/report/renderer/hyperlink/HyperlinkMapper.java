package com.jaspersoft.android.sdk.widget.report.renderer.hyperlink;

import android.net.Uri;

import com.google.gson.Gson;
import com.jaspersoft.android.sdk.network.entity.report.ReportParameter;
import com.jaspersoft.android.sdk.widget.report.renderer.Destination;
import com.jaspersoft.android.sdk.widget.report.renderer.RunOptions;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class HyperlinkMapper {
    public Hyperlink map(String type, String hyperlink) {
        switch (type) {
            case "Reference":
                Uri referenceUri = Uri.parse(hyperlink);
                return new ReferenceHyperlink(referenceUri);
            case "LocalPage":
                Integer page = new Gson().fromJson(hyperlink, Integer.class);
                return new LocalHyperlink(page == null ? null : new Destination(page));
            case "LocalAnchor":
                return new LocalHyperlink(hyperlink == null ? null : new Destination(hyperlink));
            case "RemotePage":
            case "RemoteAnchor":
                RemoteResource remoteHyperlink = new Gson().fromJson(hyperlink, RemoteResource.class);
                return new RemoteHyperlink(Uri.parse(remoteHyperlink.getResourceUri()), remoteHyperlink.getDestination());
            case "ReportExecution":
                ReportExecution reportExecution = new Gson().fromJson(hyperlink, ReportExecution.class);
                RunOptions runOptions = new RunOptions.Builder()
                        .reportUri(reportExecution.getReportUri())
                        .destination(reportExecution.getDestination())
                        .parameters(reportExecution.parameters)
                        .build();
                return new ReportExecutionHyperlink(runOptions, reportExecution.getReportFormat());
            default:
                return null;
        }
    }

    private static class ReportExecution {
        private final String reportUri;
        private final List<ReportParameter> parameters;
        private final Destination destination;
        private final String reportFormat;

        public ReportExecution(String reportUri, List<ReportParameter> parameters, Destination destination, String reportFormat) {
            this.reportUri = reportUri;
            this.parameters = parameters;
            this.destination = destination;
            this.reportFormat = reportFormat;
        }

        public String getReportUri() {
            return reportUri;
        }

        public List<ReportParameter> getParameters() {
            return parameters;
        }

        public Destination getDestination() {
            return destination;
        }

        public String getReportFormat() {
            if (reportFormat == null) return "html";
            return reportFormat;
        }
    }

    private static class RemoteResource {
        private final String resourceUri;
        private final Destination destination;

        public RemoteResource(String resourceUri, Destination destination) {
            this.resourceUri = resourceUri;
            this.destination = destination;
        }

        public String getResourceUri() {
            return resourceUri;
        }

        public Destination getDestination() {
            if (destination.getAnchor() == null && destination.getPage() == null) return null;
            return destination;
        }
    }
}
