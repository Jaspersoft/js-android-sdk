package com.jaspersoft.android.sdk.widget.report.renderer.hyperlink;

import android.net.Uri;

import com.google.gson.Gson;
import com.jaspersoft.android.sdk.widget.report.renderer.Destination;
import com.jaspersoft.android.sdk.widget.report.renderer.RunOptions;

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
                RunOptions reportExecution = new Gson().fromJson(hyperlink, RunOptions.class);
                return new ReportExecutionHyperlink(reportExecution);
            default:
                return null;
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
