package com.jaspersoft.android.sdk.client.integration;

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.async.request.GetReportResourceRequest;
import com.jaspersoft.android.sdk.client.oxm.resource.ResourceLookup;
import com.jaspersoft.android.sdk.client.util.RealHttpRule;
import com.jaspersoft.android.sdk.client.util.TargetDataType;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Collection;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Tom Koptel
 * @since 1.10
 */
@RunWith(ParameterizedRobolectricTestRunner.class)
@Config(manifest = Config.NONE)
@TargetDataType(values = {"XML", "JSON"})
public class GetReportResourceRequestTest extends ParametrizedTest {
    @Rule
    public RealHttpRule realHttpRule = new RealHttpRule();

    @ParameterizedRobolectricTestRunner.Parameters(name = "Data type = {2} Server version = {0} url = {1}")
    public static Collection<Object[]> data() {
        return ParametrizedTest.data(GetReportResourceRequestTest.class);
    }

    public GetReportResourceRequestTest(String versionCode, String url, String dataType) {
        super(versionCode, url, dataType);
    }

    @Test
    public void requestShouldReportStatus() throws Exception {
        JsRestClient jsRestClient = getJsRestClient();
        GetReportResourceRequest resourcesRequest = new GetReportResourceRequest(jsRestClient,
                getFactoryGirl().getResourceUri(jsRestClient));
        ResourceLookup response = resourcesRequest.loadDataFromNetwork();
        assertThat(response.getResourceType(), is(ResourceLookup.ResourceType.reportUnit));
        assertThat(response.getLabel(), notNullValue());
        assertThat(response.getDescription(), notNullValue());
        assertThat(response.getUri(), notNullValue());
    }
}
