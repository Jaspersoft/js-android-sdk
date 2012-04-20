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
 * @author Volodya Sabadosh (vsabadosh@jaspersoft.com)
 * @author Ivan Gadzhega
 * @version $Id$
 */
public class DeleteResourceAsyncTask extends JsRestAsyncTask<Object, Void> {

    private String resourceUri;

    public DeleteResourceAsyncTask(int id, JsRestClient jsRestClient, String resourceUri) {
        super(id, jsRestClient);
        this.resourceUri = resourceUri;
    }

    public DeleteResourceAsyncTask(int id, String progressMessage, JsRestClient jsRestClient, String resourceUri) {
        super(id, progressMessage, jsRestClient);
        this.resourceUri = resourceUri;
    }

    public DeleteResourceAsyncTask(int id, String progressMessage,
            long showDialogTimeout, JsRestClient jsRestClient, String resourceUri) {
        super(id, progressMessage, showDialogTimeout, jsRestClient);
        this.resourceUri = resourceUri;
    }


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
