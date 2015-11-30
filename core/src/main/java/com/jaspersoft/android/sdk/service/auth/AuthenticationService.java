package com.jaspersoft.android.sdk.service.auth;

import com.jaspersoft.android.sdk.network.AuthenticationRestApi;
import com.jaspersoft.android.sdk.service.RestClient;
import com.jaspersoft.android.sdk.service.exception.ServiceException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.util.concurrent.TimeUnit;

import static com.jaspersoft.android.sdk.service.internal.Preconditions.checkNotNull;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class AuthenticationService {
    private final AuthPolicy mAuthPolicy;

    @TestOnly
    AuthenticationService(AuthPolicy authPolicy) {
        mAuthPolicy = authPolicy;
    }

    @NotNull
    public static AuthenticationService create(@NotNull RestClient mClient) {
        AuthenticationRestApi restApi = new AuthenticationRestApi.Builder()
                .connectionTimeOut(mClient.getConnectionTimeOut(), TimeUnit.MILLISECONDS)
                .readTimeout(mClient.getReadTimeOut(), TimeUnit.MILLISECONDS)
                .baseUrl(mClient.getServerUrl())
                .build();
        AuthPolicy policyImpl = AuthPolicy.Default.create(restApi);
        return new AuthenticationService(policyImpl);
    }

    @NotNull
    public String authenticate(@NotNull Credentials credentials) throws ServiceException {
        checkNotNull(credentials, "Credentials should not be null");
        return credentials.applyPolicy(mAuthPolicy);
    }
}
