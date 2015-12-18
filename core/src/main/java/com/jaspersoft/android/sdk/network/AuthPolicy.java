package com.jaspersoft.android.sdk.network;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.0
 */
abstract class AuthPolicy {
    protected final AnonymousClient mAnonymousClient;

    protected AuthPolicy(AnonymousClient anonymousClient) {
        mAnonymousClient = anonymousClient;
    }

    abstract Cookies applyCredentials(SpringCredentials credentials) throws IOException, HttpException;
}
