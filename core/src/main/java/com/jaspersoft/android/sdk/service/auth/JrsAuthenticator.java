package com.jaspersoft.android.sdk.service.auth;

import com.jaspersoft.android.sdk.network.AuthenticationRestApi;
import com.jaspersoft.android.sdk.service.exception.JSException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import static com.jaspersoft.android.sdk.service.internal.Preconditions.checkNotNull;

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

    @NotNull
    public static JrsAuthenticator create(@NotNull String baseUrl) {
        checkNotNull(baseUrl, "Base url should not be null");
        AuthPolicy policyImpl = AuthPolicy.Default.create(baseUrl);
        return new JrsAuthenticator(policyImpl);
    }

    @NotNull
    public static JrsAuthenticator create(@NotNull AuthenticationRestApi restApi) {
        checkNotNull(restApi, "Authentication API should not be null");
        AuthPolicy policyImpl = AuthPolicy.Default.create(restApi);
        return new JrsAuthenticator(policyImpl);
    }

    @NotNull
    public String authenticate(@NotNull Credentials credentials) throws JSException {
        checkNotNull(credentials, "Credentials should not be null");
        return credentials.applyPolicy(mAuthPolicy);
    }
}
