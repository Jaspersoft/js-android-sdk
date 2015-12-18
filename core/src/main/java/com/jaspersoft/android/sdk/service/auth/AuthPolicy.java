package com.jaspersoft.android.sdk.service.auth;

import com.jaspersoft.android.sdk.network.AuthenticationRestApi;
import com.jaspersoft.android.sdk.network.Cookies;
import com.jaspersoft.android.sdk.network.HttpException;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.0
 */
interface AuthPolicy {
    Cookies applyCredentials(SpringCredentials credentials) throws IOException, HttpException;

    class Default implements AuthPolicy {
        private final SpringAuthService mSpringService;

        private Default(SpringAuthService springService) {
            mSpringService = springService;
        }

        @NotNull
        public static AuthPolicy create(@NotNull AuthenticationRestApi restApi) {
            SpringAuthService springService = SpringAuthService.create(restApi);
            return new Default(springService);
        }

        @NotNull
        public static Default create(@NotNull String baseUrl) {
            SpringAuthService springService = SpringAuthService.create(baseUrl);
            return new Default(springService);
        }

        @Override
        public Cookies applyCredentials(SpringCredentials credentials) throws IOException, HttpException {
            return mSpringService.authenticate(credentials);
        }
    }
}
