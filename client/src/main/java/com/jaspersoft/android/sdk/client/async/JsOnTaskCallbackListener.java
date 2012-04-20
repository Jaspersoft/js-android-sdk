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
 * used to notification <strong>Activity</strong> about <strong>Asynchronous task</strong> executing results.</p>
 *
 * @author Volodya Sabadosh (vsabadosh@jaspersoft.com)
 * @author Ivan Gadzhega
 * @version $Id$
 * @since 1.0
 */
public interface JsOnTaskCallbackListener {

    /**
     * Notifies <strong>Activity</strong> about <strong>Asynchronous task</strong> success completeness and contains
     * the logic witch will be executed on <strong>UI tread</strong>.
     *
     * @param task <strong>Asynchronous task</strong> witch finished with exceptions.
     */
    void onTaskComplete(JsAsyncTask task);

    /**
     * Notifies <strong>Activity</strong> about <strong>Asynchronous task</strong> executing errors and contains the
     * logic witch will be executed on <strong>UI tread</strong>.
     *
     * @param task <strong>Asynchronous task</strong> witch finished with exceptions.
     */
    void onTaskException(JsAsyncTask task);

}
