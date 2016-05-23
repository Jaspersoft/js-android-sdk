package com.jaspersoft.android.sdk.network;

import com.jaspersoft.android.sdk.network.Server.AuthorizedClientBuilder;

/**
 * Allows to handle auth related error.
 * This event occurs when cookies used for network request expired.
 * Should be used for manual clean up of session related resources.
 *
 * <p>
 * See {@link AuthorizedClientBuilder#withAuthenticationHandler(AuthenticationHandler)}
 * </p>
 *
 * @author Tom Koptel
 * @since 2.5
 */
public interface AuthenticationHandler {
    AuthenticationHandler NULL = new AuthenticationHandler() {
        @Override
        public void onAuthError() {
        }
    };
    void onAuthError();
}
