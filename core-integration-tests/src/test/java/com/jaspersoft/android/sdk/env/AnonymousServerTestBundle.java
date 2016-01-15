package com.jaspersoft.android.sdk.env;

import com.jaspersoft.android.sdk.network.AnonymousClient;
import com.jaspersoft.android.sdk.network.SpringCredentials;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class AnonymousServerTestBundle {
    private final SpringCredentials mCredentials;
    private final AnonymousClient mClient;

    public AnonymousServerTestBundle(SpringCredentials credentials, AnonymousClient client) {
        mCredentials = credentials;
        mClient = client;
    }

    public SpringCredentials getCredentials() {
        return mCredentials;
    }

    public AnonymousClient getClient() {
        return mClient;
    }

    @Override
    public String toString() {
        return "AnonymousServerTestBundle{" +
                "serverUrl=" + mClient.getBaseUrl() +
                '}';
    }
}
