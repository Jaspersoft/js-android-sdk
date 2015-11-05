package com.jaspersoft.android.sdk.service.auth;

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

        public static Default create(String baseUrl) {
            SpringAuthService springService = SpringAuthService.create(baseUrl);
            return new Default(springService);
        }

        @Override
        public String applyCredentials(SpringCredentials credentials) {
            return mSpringService.authenticate(credentials);
        }
    }
}
