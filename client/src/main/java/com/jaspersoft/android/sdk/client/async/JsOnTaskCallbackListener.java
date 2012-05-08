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

package com.jaspersoft.android.sdk.client.async;

import com.jaspersoft.android.sdk.client.async.task.JsAsyncTask;

/**
 * <p>Declaration of <strong>Asynchronous task callback listener</strong>,
 * used to notify <strong>Activity</strong> about <strong>Asynchronous task</strong> execution results.</p>
 *
 * @author Volodya Sabadosh (vsabadosh@jaspersoft.com)
 * @author Ivan Gadzhega
 * @version $Id$
 * @since 1.0
 */
public interface JsOnTaskCallbackListener {

    /**
     * Notifies <strong>Activity</strong> about successful completion of an <code>Asynchronous Task</code> and contains
     * the logic which will be executed on the <strong>UI thread</strong>.
     *
     * @param task <strong>Asynchronous Task</strong> which finished with exceptions.
     */
    void onTaskComplete(JsAsyncTask task);

    /**
     * Notifies <strong>Activity</strong> about <strong>Asynchronous Task</strong> execution errors and contains the
     * logic which will be executed on the <strong>UI thread</strong>.
     *
     * @param task <strong>Asynchronous Task</strong> which finished with exceptions.
     */
    void onTaskException(JsAsyncTask task);

}
