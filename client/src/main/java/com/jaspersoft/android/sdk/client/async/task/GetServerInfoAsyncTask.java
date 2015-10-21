/*
 * Copyright (C) 2015 TIBCO Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/mobile-sdk-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile SDK for Android.
 *
 * TIBCO Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile SDK for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.client.async.task;

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.oxm.server.ServerInfo;

/**
 * @author Ivan Gadzhega
 * @since 1.4
 *
 * @deprecated Use {@link com.jaspersoft.android.sdk.client.async.request.cacheable.GetServerInfoRequest GetResourcesRequest}
 * instead.
 */

@Deprecated
public class GetServerInfoAsyncTask extends JsRestAsyncTask<Object, ServerInfo>{

    /**
     * Creates a new <strong>GetServerInfoAsyncTask</strong> entity with the specified parameters.
     *
     * @param id <strong>GetServerInfoAsyncTask</strong> identifier.
     * @param jsRestClient <strong>JsRestClient</strong>.
     */
    public GetServerInfoAsyncTask(int id, JsRestClient jsRestClient) {
        super(id, jsRestClient);
    }

    /**
     * Creates a new <strong>GetServerInfoAsyncTask</strong> entity with the specified parameters.
     *
     * @param id <strong>GetServerInfoAsyncTask</strong> identifier.
     * @param progressMessage message of <strong>Progress dialog</strong>.
     * @param jsRestClient <strong>JsRestClient</strong>.
     */
    public GetServerInfoAsyncTask(int id, String progressMessage, JsRestClient jsRestClient) {
        super(id, progressMessage, SHOW_DIALOG_DEFAULT_TIMEOUT, jsRestClient);
    }

    /**
     * Creates a new <strong>GetServerInfoAsyncTask</strong> entity with the specified parameters.
     *
     * @param id <strong>GetServerInfoAsyncTask</strong> identifier.
     * @param progressMessage message of <strong>Progress dialog</strong>.
     * @param progressDialogAppearAfter the time interval (in milliseconds) <strong>Progress dialog</strong> should be
     * appear.
     * @param jsRestClient <strong>JsRestClient</strong>.
     */
    public GetServerInfoAsyncTask(int id, String progressMessage, long progressDialogAppearAfter, JsRestClient jsRestClient) {
        super(id, progressMessage, progressDialogAppearAfter, jsRestClient);
    }

    /**
     * Overrides the <code>doInBackground(Object... arg0)</code> method by calling <strong>JsRestClient</strong>
     * <code>getServerInfo()</code> method.
     *
     * @param arg0 the parameters of the <strong>Asynchronous task</strong>. Current implementation does not use this params.
     * @return the ServerInfo value
     */
    @Override
    protected ServerInfo doInBackground(Object... arg0) {
        super.doInBackground(arg0);
        try {
            return getJsRestClient().getServerInfo();
        } catch (Exception e) {
            setTaskException(e);
            return null;
        }
    }

}
