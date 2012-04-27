/*
 * Copyright (C) 2005 - 2012 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.jaspersoft.android.sdk.client.async.task;

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.oxm.ResourceDescriptor;

/**
 * <p>Declaration of the <strong>GetResourceAsyncTask</strong> which is subclass of <strong>JsRestAsyncTask</strong>
 * abstract class and overrides <code>doInBackground(Object... arg0)</code> method from it.</p>
 *
 * @author Volodya Sabadosh (vsabadosh@jaspersoft.com)
 * @author Ivan Gadzhega
 * @version $Id$
 * @since 1.0
 */
public class GetResourceAsyncTask extends JsRestAsyncTask<Object, ResourceDescriptor> {

    private String resourceUri;

    /**
     * Creates a new <strong>GetResourceAsyncTask</strong> entity with the specified parameters.
     *
     * @param id <strong>GetResourceAsyncTask</strong> identifier.
     * @param jsRestClient <strong>JsRestClient</strong>.
     * @param resourceUri <strong>Resource</strong> Uri.
     */
    public GetResourceAsyncTask(int id, JsRestClient jsRestClient, String resourceUri) {
        super(id, jsRestClient);
        this.resourceUri = resourceUri;
    }

    /**
     * Creates a new <strong>GetResourceAsyncTask</strong> entity with the specified parameters.
     *
     * @param id <strong>GetResourceAsyncTask</strong> identifier.
     * @param progressMessage message of <strong>Progress dialog</strong>.
     * @param jsRestClient <strong>JsRestClient</strong>.
     * @param resourceUri <strong>Resource</strong> Uri.
     */
    public GetResourceAsyncTask(int id, String progressMessage, JsRestClient jsRestClient, String resourceUri) {
        super(id, progressMessage, jsRestClient);
        this.resourceUri = resourceUri;
    }

    /**
     * Creates a new <strong>GetResourceAsyncTask</strong> entity with the specified parameters.
     *
     * @param id <strong>GetResourceAsyncTask</strong> identifier.
     * @param progressMessage message of <strong>Progress dialog</strong>.
     * @param showDialogTimeout the time interval (in milliseconds) <strong>Progress dialog</strong> should be appear
     * after.
     * @param jsRestClient <strong>JsRestClient</strong>.
     * @param resourceUri <strong>Resource</strong> Uri.
     */
    public GetResourceAsyncTask(int id, String progressMessage,
            long showDialogTimeout, JsRestClient jsRestClient, String resourceUri) {
        super(id, progressMessage, showDialogTimeout, jsRestClient);
        this.resourceUri = resourceUri;
    }

    /**
     * Overrides the <code>doInBackground(Object... arg0)</code> method by calling <strong>JsRestClient</strong>
     * <code>getResource(String resourceUri)</code> method.
     *
     * @param arg0 the parameters of the <strong>Asynchronous task</strong>. Current implementation does not use this params.
     * @return <strong>Resource descriptor</strong> by resource URI.
     */
    @Override
    protected ResourceDescriptor doInBackground(Object... arg0) {
        super.doInBackground(arg0);
        try {
            return getJsRestClient().getResource(resourceUri);
        } catch (Exception e) {
            setTaskException(e);
            return null;
        }
    }

    // Getters

    public String getResourceUri() {
        return resourceUri;
    }
}
