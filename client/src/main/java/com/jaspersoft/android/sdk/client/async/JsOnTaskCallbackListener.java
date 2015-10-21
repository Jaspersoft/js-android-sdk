/*
 * Copyright (C) 2015 TIBCO Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/mobile-sdk-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile SDK for Android.
 *
 * TIBCO Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile SDK for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.client.async;

import com.jaspersoft.android.sdk.client.async.task.JsAsyncTask;

/**
 * <p>Declaration of <strong>Asynchronous task callback listener</strong>,
 * used to notify <strong>Activity</strong> about <strong>Asynchronous task</strong> execution results.</p>
 *
 * @author Volodya Sabadosh (vsabadosh@jaspersoft.com)
 * @author Ivan Gadzhega
 * @since 1.0
 */

@Deprecated
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
