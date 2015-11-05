package com.jaspersoft.android.sdk.service.auth;

import com.jaspersoft.android.sdk.network.AuthenticationRestApi;

import org.jetbrains.annotations.NotNull;

/**
 * @author Tom Koptel
 * @since 2.0
 */
interface AuthPolicy {
    String applyCredentials(SpringCredentials credentials);

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
        public String applyCredentials(SpringCredentials credentials) {
            return mSpringService.authenticate(credentials);
        }
    }
}
