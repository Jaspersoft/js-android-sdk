package com.jaspersoft.android.sdk.service.auth;

import com.jaspersoft.android.sdk.env.AnonymousServerTestBundle;
import com.jaspersoft.android.sdk.env.JrsEnvironmentRule;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Tom Koptel
 * @since 2.3
 */
@RunWith(JUnitParamsRunner.class)
public class AuthorizationServiceTest {
    @ClassRule
    public static JrsEnvironmentRule sEnv = new JrsEnvironmentRule();

    @Test
    @Parameters(method = "clients")
    public void authorization_service_should_authorize(AnonymousServerTestBundle bundle) throws Exception {
        AuthorizationService service = AuthorizationService.newService(bundle.getClient());
        service.authorize(bundle.getCredentials());
    }

    private Object[] clients() {
        return sEnv.listAnonymousClients();
    }
}
