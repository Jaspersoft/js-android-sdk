/*
 * Copyright (C) 2012 Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/mobile-sdk-android
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of Jaspersoft Mobile SDK for Android.
 *
 * Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Jaspersoft Mobile SDK for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.client.async.task;

import com.jaspersoft.android.sdk.client.JsRestClient;

/**
 * <p>Declaration of the <strong>JsRestAsyncTask</strong> which is subclass of <strong>JsAsyncTask</strong>
 * abstract class and encapsulates <strong>JsRestClient</strong>.</p>
 *
 * @author Volodya Sabadosh (vsabadosh@jaspersoft.com)
 * @author Ivan Gadzhega
 * @version $Id$
 */
public abstract class JsRestAsyncTask<Params, Result> extends JsAsyncTask<Params, Result> {

    private JsRestClient jsRestClient;

    /**
     * Creates a new <strong>JsRestAsyncTask</strong> entity with the specified parameters.
     *
     * @param id <strong>JsRestAsyncTask</strong> identifier.
     * @param jsRestClient <strong>JsRestClient</strong>.
     */
    public JsRestAsyncTask(int id, JsRestClient jsRestClient) {
        super(id);
        this.jsRestClient = jsRestClient;
    }

    /**
     * Creates a new <strong>JsRestAsyncTask</strong> entity with the specified parameters.
     *
     * @param id <strong>JsRestAsyncTask</strong> identifier.
     * @param progressMessage message of <strong>Progress dialog</strong>.
     * @param jsRestClient <strong>JsRestClient</strong>.
     */
    public JsRestAsyncTask(int id, String progressMessage, JsRestClient jsRestClient) {
        super(id, progressMessage, SHOW_DIALOG_DEFAULT_TIMEOUT);
        this.jsRestClient = jsRestClient;
    }

    /**
     * Creates a new <strong>JsRestAsyncTask</strong> entity with the specified parameters.
     *
     * @param id <strong>JsRestAsyncTask</strong> identifier.
     * @param progressMessage message of <strong>Progress dialog</strong>.
     * @param progressDialogAppearAfter the time interval (in milliseconds) <strong>Progress dialog</strong> should be
     * appear.
     * @param jsRestClient <strong>JsRestClient</strong>.
     */
    public JsRestAsyncTask(int id, String progressMessage, long progressDialogAppearAfter, JsRestClient jsRestClient) {
        super(id, progressMessage, progressDialogAppearAfter);
        this.jsRestClient = jsRestClient;
    }


    public JsRestClient getJsRestClient() {
        return jsRestClient;
    }

    public void setJsRestClient(JsRestClient jsRestClient) {
        this.jsRestClient = jsRestClient;
    }
}
