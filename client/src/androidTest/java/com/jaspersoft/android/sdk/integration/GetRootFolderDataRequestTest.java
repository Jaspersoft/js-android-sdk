/*
 * Copyright (c) 2014. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.jaspersoft.android.sdk.integration;

import com.jaspersoft.android.sdk.client.async.request.GetRootFolderDataRequest;
import com.jaspersoft.android.sdk.client.oxm.report.FolderDataResponse;
import com.jaspersoft.android.sdk.integration.utils.ProtoInstrumentation;

public class GetRootFolderDataRequestTest extends ProtoInstrumentation {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void test_getRootFolderUriMethod() throws Exception {
        GetRootFolderDataRequest request = new GetRootFolderDataRequest(getJsRestClient());
        FolderDataResponse response = request.loadDataFromNetwork();
        assertTrue(response.getUri().equals("/"));
    }
}
