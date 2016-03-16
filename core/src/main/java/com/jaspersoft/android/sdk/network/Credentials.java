package com.jaspersoft.android.sdk.network;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public abstract class Credentials {
    protected abstract void apply(AuthStrategy authStrategy) throws IOException, HttpException;
}
