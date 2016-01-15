package com.jaspersoft.android.sdk.service.info;

import com.jaspersoft.android.sdk.env.AnonymousServerTestBundle;
import com.jaspersoft.android.sdk.env.JrsEnvironmentRule;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(JUnitParamsRunner.class)
public class ServerInfoServiceTest {
    @ClassRule
    public static JrsEnvironmentRule sEnv = new JrsEnvironmentRule();

    @Test
    @Parameters(method = "clients")
    public void info_service_should_provide_info(AnonymousServerTestBundle bundle) throws Exception {
        ServerInfoService service = ServerInfoService.newService(bundle.getClient());
        assertThat(service.requestServerInfo(), is(notNullValue()));
    }

    private Object[] clients() {
        return sEnv.listAnonymousClients();
    }
}
