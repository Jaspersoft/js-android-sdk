/*
 * Copyright (c) 2014. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.jaspersoft.android.sdk.client.async.request;

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.async.request.cacheable.CacheableRequest;
import com.jaspersoft.android.sdk.client.oxm.report.FolderDataResponse;

public class GetRootFolderDataRequest extends CacheableRequest<FolderDataResponse> {

    public GetRootFolderDataRequest(JsRestClient jsRestClient) {
        super(jsRestClient, FolderDataResponse.class);
    }

    @Override
    public FolderDataResponse loadDataFromNetwork() throws Exception {
        return getJsRestClient().getRootFolderData();
    }

}
