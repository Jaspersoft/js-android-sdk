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

/**
 * <p>Declaration of the <strong>DeleteResourceAsyncTask</strong> which is subclass of <strong>JsRestAsyncTask</strong>
 * abstract class and overrides <code>doInBackground(Object... arg0)</code> method from it.</p>
 *
 * @author Volodya Sabadosh (vsabadosh@jaspersoft.com)
 * @author Ivan Gadzhega
 * @version $Id$
 */
public class DeleteResourceAsyncTask extends JsRestAsyncTask<Object, Void> {

    private String resourceUri;

    /**
     * Creates a new <strong>DeleteResourceAsyncTask</strong> entity with the specified parameters.
     *
     * @param id <strong>DeleteResourceAsyncTask</strong> identifier.
     * @param jsRestClient <strong>JsRestClient</strong>.
     * @param resourceUri <strong>Resource</strong> Uri.
     */
    public DeleteResourceAsyncTask(int id, JsRestClient jsRestClient, String resourceUri) {
        super(id, jsRestClient);
        this.resourceUri = resourceUri;
    }

    /**
     * Creates a new <strong>DeleteResourceAsyncTask</strong> entity with the specified parameters.
     *
     * @param id <strong>DeleteResourceAsyncTask</strong> identifier.
     * @param progressMessage message of <strong>Progress dialog</strong>.
     * @param jsRestClient <strong>JsRestClient</strong>.
     * @param resourceUri <strong>Resource</strong> Uri.
     */
    public DeleteResourceAsyncTask(int id, String progressMessage, JsRestClient jsRestClient, String resourceUri) {
        super(id, progressMessage, jsRestClient);
        this.resourceUri = resourceUri;
    }

    /**
     *
     * @param id <strong>DeleteResourceAsyncTask</strong> identifier.
     * @param progressMessage message of <strong>Progress dialog</strong>.
     * @param showDialogTimeout the time interval (in milliseconds) <strong>Progress dialog</strong> should be appear
     * after.
     * @param jsRestClient <strong>JsRestClient</strong>.
     * @param resourceUri <strong>Resource</strong> Uri.
     */
    public DeleteResourceAsyncTask(int id, String progressMessage,
            long showDialogTimeout, JsRestClient jsRestClient, String resourceUri) {
        super(id, progressMessage, showDialogTimeout, jsRestClient);
        this.resourceUri = resourceUri;
    }


    /**
     * Overrides the <code>doInBackground(Object... arg0)</code> method by calling <strong>JsRestClient</strong>
     * <code>deleteResource(String resourceUri)</code> method.
     *
     * @param arg0 the parameters of the <strong>Asynchronous Task</strong>. Current implementation does not use these params.
     * @return <strong>Resource descriptor</strong> by resource URI.
     */
    @Override
    protected Void doInBackground(Object... arg0) {
        super.doInBackground(arg0);
        try {
            getJsRestClient().deleteResource(this.resourceUri);
        } catch (Exception e) {
            setTaskException(e);
        }
        return null;
    }

    // Getters

    public String getResourceUri() {
        return resourceUri;
    }
}
