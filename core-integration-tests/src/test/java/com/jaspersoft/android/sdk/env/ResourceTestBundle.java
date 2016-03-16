package com.jaspersoft.android.sdk.env;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.SpringCredentials;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public final class ResourceTestBundle {
    private final String mUri;
    private final ServerTestBundle serverTestBundle;

    public ResourceTestBundle(String mUri, ServerTestBundle serverTestBundle) {
        this.mUri = mUri;
        this.serverTestBundle = serverTestBundle;
    }

    public String getUri() {
        return mUri;
    }

    public ServerVersion getVersion() {
        return serverTestBundle.getVersion();
    }

    public SpringCredentials getCredentials() {
        return serverTestBundle.getCredentials();
    }

    public AuthorizedClient getClient() {
        return serverTestBundle.getClient();
    }

    @Override
    public String toString() {
        return "ReportTestBundle{" +
                "server=" + serverTestBundle +
                ", uri='" + mUri + '\'' +
                '}';
    }
}
