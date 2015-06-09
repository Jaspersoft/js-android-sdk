/*
 * Copyright (c) 2014. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.jaspersoft.android.sdk.client.integration;

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.async.request.GetRootFolderDataRequest;
import com.jaspersoft.android.sdk.client.oxm.report.FolderDataResponse;
import com.jaspersoft.android.sdk.client.util.FactoryGirl;
import com.jaspersoft.android.sdk.util.StaticCacheHelper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.FakeHttp;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Tom Koptel
 * @since 1.10
 */
@RunWith(ParameterizedRobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class GetRootFolderDataRequestTest extends ParametrizedTest {

    @ParameterizedRobolectricTestRunner.Parameters(name = "Data type = {2} Server version = {0} url = {1}")
    public static Collection<Object[]> data() {
        return ParametrizedTest.data();
    }

    public GetRootFolderDataRequestTest(String versionCode, String url, String dataType) {
        super(versionCode, url, dataType);
    }

    @Test
    public void shouldGetRootFolderUriMethod() throws Exception {
        StaticCacheHelper.clearCache();
        FakeHttp.getFakeHttpLayer().interceptHttpRequests(false);

        FactoryGirl factoryGirl = FactoryGirl.newInstance();
        JsRestClient jsRestClient = factoryGirl.createJsRestClient(mDataType, mServer);

        GetRootFolderDataRequest request = new GetRootFolderDataRequest(jsRestClient);
        FolderDataResponse response = request.loadDataFromNetwork();
        assertThat(response.getUri(), is("/"));
    }

}
