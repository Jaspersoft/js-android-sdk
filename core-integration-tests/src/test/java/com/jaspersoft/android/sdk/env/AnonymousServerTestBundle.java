package com.jaspersoft.android.sdk.env;

import com.jaspersoft.android.sdk.network.AnonymousClient;
import com.jaspersoft.android.sdk.network.SpringCredentials;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;
import com.jaspersoft.android.sdk.testkit.dto.Info;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class AnonymousServerTestBundle {
    private final SpringCredentials mCredentials;
    private final AnonymousClient mClient;
    private final Info mInfo;

    public AnonymousServerTestBundle(SpringCredentials credentials, AnonymousClient client, Info info) {
        mCredentials = credentials;
        mClient = client;
        mInfo = info;
    }

    public SpringCredentials getCredentials() {
        return mCredentials;
    }

    public AnonymousClient getClient() {
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
        return "AnonymousServerTestBundle{" +
                "serverUrl=" + mClient.getBaseUrl() +
                '}';
    }
}
