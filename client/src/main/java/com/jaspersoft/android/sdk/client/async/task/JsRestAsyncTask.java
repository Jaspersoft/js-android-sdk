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
public abstract class JsRestAsyncTask<Params, Result> extends JsAsyncTask<Params, Result> {

    private JsRestClient jsRestClient;

    public JsRestAsyncTask(int id, JsRestClient jsRestClient) {
        super(id);
        this.jsRestClient = jsRestClient;
    }

    public JsRestAsyncTask(int id, String progressMessage, JsRestClient jsRestClient) {
        super(id, progressMessage, SHOW_DIALOG_DEFAULT_TIMEOUT);
        this.jsRestClient = jsRestClient;
    }

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
