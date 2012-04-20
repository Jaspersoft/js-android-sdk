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
 * <p>Declaration of <strong>Progress tracker</strong>, used for reducing of relating between <strong>Activity</strong>
 * and <strong>Asynchronous task manager</strong> and notifies the last one about life cycles result of
 * <strong>Asynchronous task</strong> executing</p>.
 *
 * @author Volodya Sabadosh (vsabadosh@jaspersoft.com)
 * @author Ivan Gadzhega
 * @version $Id$
 * @since 1.0
 */
public interface JsProgressTracker {

    /**
     * Updates progress <strong>message</strong> for target <strong>Asynchronous task</strong> if progress dialog
     * showing is required for it.
     *
     * @param asyncTask <strong>Asynchronous task</strong>.
     * @param message <strong>message</strong> witch will be appeared on progress dialogue.
     */
    void onProgress(JsAsyncTask asyncTask, String message);

    /**
     * Notifies <strong>Asynchronous task manager</strong> about <strong>Asynchronous task</strong> success completeness
     * and is responsible for removing target task from active  <strong>Asynchronous task</strong> list and progress
     * dialog disconnecting.
     *
     * @param asyncTask <strong>Asynchronous task</strong> witch finished successfully.
     */
    void onComplete(JsAsyncTask asyncTask);

    /**
     * Notifies <strong>Asynchronous task manager</strong> about <strong>Asynchronous task</strong> errors and is
     * responsible for removing target task from active  <strong>Asynchronous task</strong> list and progress dialog
     * disconnecting.
     *
     * @param asyncTask <strong>Asynchronous task</strong> witch finished with exceptions.
     */
    void onException(JsAsyncTask asyncTask);

}
