package com.jaspersoft.android.sdk.env;

import com.jaspersoft.android.sdk.network.AnonymousClient;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class AnonymousServerTestBundle {
    private final AnonymousClient mClient;

    public AnonymousServerTestBundle(AnonymousClient client) {
        mClient = client;
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
