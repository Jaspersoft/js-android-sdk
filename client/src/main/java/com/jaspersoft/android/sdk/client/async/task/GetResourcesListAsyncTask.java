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

import java.util.List;

/**
 * @author Volodya Sabadosh (vsabadosh@jaspersoft.com)
 * @author Ivan Gadzhega
 * @version $Id$
 * @since 1.0
 */
public class GetResourcesListAsyncTask extends JsRestAsyncTask<Object, List<ResourceDescriptor>> {

    private String resourceUri;

    public GetResourcesListAsyncTask(int id, JsRestClient jsRestClient, String resourceUri) {
        super(id, jsRestClient);
        this.resourceUri = resourceUri;
    }

    public GetResourcesListAsyncTask(int id, String progressMessage, JsRestClient jsRestClient, String resourceUri) {
        super(id, progressMessage, jsRestClient);
        this.resourceUri = resourceUri;
    }

    public GetResourcesListAsyncTask(int id, String progressMessage,
                                     long showDialogTimeout, JsRestClient jsRestClient, String resourceUri) {
        super(id, progressMessage, showDialogTimeout, jsRestClient);
        this.resourceUri = resourceUri;
    }


    @Override
    protected List<ResourceDescriptor> doInBackground(Object... arg0) {
        super.doInBackground(arg0);
        try {
            return getJsRestClient().getResourcesList(resourceUri);
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
