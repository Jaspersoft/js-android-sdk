package com.jaspersoft.android.sdk.service.auth;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class JrsAuthenticator {
    private final AuthPolicy mAuthPolicy;

    @TestOnly
    JrsAuthenticator(AuthPolicy authPolicy) {
        mAuthPolicy = authPolicy;
    }

    public static JrsAuthenticator create(String baseUrl) {
        AuthPolicy policyImpl = AuthPolicy.Default.create(baseUrl);
        return new JrsAuthenticator(policyImpl);
    }

    @NotNull
    public String authenticate(Credentials credentials) {
        return credentials.applyPolicy(mAuthPolicy);
    }
}
