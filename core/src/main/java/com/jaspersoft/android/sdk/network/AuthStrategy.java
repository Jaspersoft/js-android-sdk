package com.jaspersoft.android.sdk.network;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.0
 */
abstract class AuthStrategy {
    protected final AnonymousClient mAnonymousClient;

    protected AuthStrategy(AnonymousClient anonymousClient) {
        mAnonymousClient = anonymousClient;
    }

    abstract Cookies applyCredentials(SpringCredentials credentials) throws IOException, HttpException;
}
