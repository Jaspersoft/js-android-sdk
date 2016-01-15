package com.jaspersoft.android.sdk.env;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.SpringCredentials;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ServerTestBundle {
    private final SpringCredentials mCredentials;
    private final AuthorizedClient mClient;

    public ServerTestBundle(SpringCredentials credentials, AuthorizedClient client) {
        mCredentials = credentials;
        mClient = client;
    }

    public SpringCredentials getCredentials() {
        return mCredentials;
    }

    public AuthorizedClient getClient() {
        return mClient;
    }

    @Override
    public String toString() {
        String credentials = "Credentials{" +
                "username='" + mCredentials.getUsername() + '\'' +
                ", organization='" + mCredentials.getOrganization() + '\'' +
                ", password=" + mCredentials.getPassword() +
                '}';
        return "ServerTestBundle{" +
                "url=" + mClient.getBaseUrl() +
                ", credentials=" + credentials +
                '}';
    }
}
