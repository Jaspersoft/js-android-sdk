package com.jaspersoft.android.sdk.service.auth;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public abstract class Credentials {
    protected abstract String applyPolicy(AuthPolicy authPolicy);
}
