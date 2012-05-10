/*
 * Copyright (C) 2012 Jaspersoft Corporation. All rights reserved.
 * http://www.jasperforge.org/projects/androidsdk
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

package com.jaspersoft.android.sdk.client.async;

import com.jaspersoft.android.sdk.client.async.task.JsAsyncTask;

/**
 * <p>Declaration of <strong>Progress tracker</strong>, used for reducing of relating between <strong>Activity</strong>
 * and <strong>Asynchronous task manager</strong> and notifies the last one about life cycles result of
 * <strong>Asynchronous task</strong> executing.</p>
 *
 * @author Volodya Sabadosh (vsabadosh@jaspersoft.com)
 * @author Ivan Gadzhega
 * @version $Id$
 * @since 1.0
 */
public interface JsProgressTracker {

    /**
     * Updates progress <strong>message</strong> for target <strong>Asynchronous Task</strong> if showing progress
     * dialog is required.
     *
     * @param asyncTask <strong>Asynchronous task</strong>.
     * @param message <strong>message</strong> which will appear on progress dialog.
     */
    void onProgress(JsAsyncTask asyncTask, String message);

    /**
     * Notifies <strong>Asynchronous Task Manager</strong> about successful <strong>Asynchronous Task</strong>
     * completion and is also responsible for removing the target task from active <strong>Asynchronous Task</strong>
     * list and disconnecting the progress dialog.
     *
     * @param asyncTask <strong>Asynchronous task</strong> which finished successfully.
     */
    void onComplete(JsAsyncTask asyncTask);

    /**
     * Notifies <strong>Asynchronous Task Manager</strong> about <strong>Asynchronous Task</strong> errors and is
     * responsible for removing the target task from active  <strong>Asynchronous Task</strong> list and
     * disconnecting the progress dialog.
     *
     * @param asyncTask <strong>Asynchronous task</strong> which finished with exceptions.
     */
    void onException(JsAsyncTask asyncTask);

}
