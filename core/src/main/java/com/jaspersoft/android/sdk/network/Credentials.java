package com.jaspersoft.android.sdk.network;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public abstract class Credentials {
    protected abstract Cookies applyPolicy(AuthPolicy authPolicy) throws IOException, HttpException;
}
