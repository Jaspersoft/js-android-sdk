package com.jaspersoft.android.sdk.env;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.SpringCredentials;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;
import com.jaspersoft.android.sdk.testkit.dto.Info;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class ServerTestBundle {
    private final SpringCredentials mCredentials;
    private final AuthorizedClient mClient;
    private final Info mInfo;

    public ServerTestBundle(SpringCredentials credentials, AuthorizedClient client, Info info) {
        mCredentials = credentials;
        mClient = client;
        mInfo = info;
    }

    public SpringCredentials getCredentials() {
        return mCredentials;
    }

    public AuthorizedClient getClient() {
        return mClient;
    }

    public ServerVersion getVersion() {
        if (mInfo == null) {
            return ServerVersion.valueOf("0");
        }
        return ServerVersion.valueOf(mInfo.getVersion());
    }

    public boolean isPro() {
        if (mInfo == null) {
            return false;
        }
        return "PRO".equals(mInfo.getVersion());
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
