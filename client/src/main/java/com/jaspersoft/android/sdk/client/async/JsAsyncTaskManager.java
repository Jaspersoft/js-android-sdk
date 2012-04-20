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

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.jaspersoft.android.sdk.client.async.task.JsAsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Declaration of <strong>Asynchronous task manager</strong>,
 * used to manage asynchronous task. </p>
 *
 * @author Volodya Sabadosh (vsabadosh@jaspersoft.com)
 * @author Ivan Gadzhega
 * @version $Id$
 * @since 1.0
 */
public class JsAsyncTaskManager implements JsProgressTracker, DialogInterface.OnCancelListener {
    private final JsOnTaskCallbackListener jsTaskCompleteListener;
    private AlertDialog progressDialog;
    private List<JsAsyncTask> jsAsyncTaskList = new ArrayList<JsAsyncTask>();

    /**
     *
     * @param context
     * @param jsTaskCompleteListener
     */
    public JsAsyncTaskManager(Context context, JsOnTaskCallbackListener jsTaskCompleteListener) {
        this(context, jsTaskCompleteListener, null);
    }

    /**
     *
     * @param context
     * @param jsTaskCompleteListener
     * @param progressDialog
     */
    public JsAsyncTaskManager(Context context, JsOnTaskCallbackListener jsTaskCompleteListener, AlertDialog progressDialog) {
        // Save reference to complete listener (activity)
        this.jsTaskCompleteListener = jsTaskCompleteListener;

        if (progressDialog == null) {
            // Setup default progress dialog.
            this.progressDialog = new ProgressDialog(context);
            this.progressDialog.setCancelable(true);
            this.progressDialog.setOnCancelListener(this);
            this.progressDialog.dismiss();
        }  else {
            this.progressDialog = progressDialog;
        }
    }

    /**
     * Executes target <strong>Asynchronous task</>.
     *
     * @param jsAsyncTask <strong>Asynchronous task</>.
     */
    public void executeTask(JsAsyncTask jsAsyncTask) {
        // Keep async tasks
        jsAsyncTaskList.add(jsAsyncTask);
        // Wire task to tracker (this)
        jsAsyncTask.setProgressTracker(this);
        // Start task
        jsAsyncTask.execute();
    }

    /**
     * {@inheritDoc}
     */
    public void onProgress(JsAsyncTask jsAsyncTask, String message) {
        if (jsAsyncTask.isShowProgressDialog()) {
            // Show dialog if it wasn't shown yet or was removed on configuration (rotation) change
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
            // Show current message in progress dialog
            progressDialog.setMessage(message);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void onCancel(DialogInterface dialog) {
        if(!this.jsAsyncTaskList.isEmpty()) {
            for (JsAsyncTask task : jsAsyncTaskList) {
                task.cancel(true);
                this.jsTaskCompleteListener.onTaskComplete(task);
                jsAsyncTaskList.remove(task);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void onComplete(JsAsyncTask asyncTask) {
        // Notify activity about completion
        jsTaskCompleteListener.onTaskComplete(asyncTask);
        // Reset task
        jsAsyncTaskList.remove(asyncTask);

        // Close progress dialog when all task is done
        finishTaskHandler(asyncTask);
    }

    /**
     * {@inheritDoc}
     */
    public void onException(JsAsyncTask asyncTask) {
        // Notify activity about completion
        jsTaskCompleteListener.onTaskException(asyncTask);
        // Reset task
        jsAsyncTaskList.remove(asyncTask);

        finishTaskHandler(asyncTask);
    }

    /**
     * Retains and detaches from <strong>progress tracker</strong> the list of <strong>Asynchronous task</strong>s in
     * <strong>state</strong> during <strong>Activity<strong>destroying.
     *
     * @return list of <strong>Asynchronous task</strong>s.
     */
    public Object retainTasks() {
        // Detach task from tracker (this) before retain
        for (JsAsyncTask task : jsAsyncTaskList) {
            task.setProgressTracker(null);
        }
        // Retains task
        return jsAsyncTaskList;
    }

    /**
     * Restores the list of <strong>Asynchronous task</strong>s from <strong>state</strong> during
     * <strong>Activity<strong> recovering.
     *
     * @param retainedTasksList list of <strong>Asynchronous task</strong>s.
     */
    public void handleRetainedTasks(List<JsAsyncTask> retainedTasksList) {
        if (retainedTasksList != null) {
            jsAsyncTaskList.addAll(retainedTasksList);
            for (JsAsyncTask task : jsAsyncTaskList) {
                task.setProgressTracker(this);
            }
        }
    }

    /**
     * Closes progress dialog when all task is done and target progress dialog showing is required for target.
     * <strong>Asynchronous task</strong>.
     *
     * @param asyncTask <strong>Asynchronous task</strong>.
     */
    private void finishTaskHandler(JsAsyncTask asyncTask) {
        if (asyncTask.isShowProgressDialog() && jsAsyncTaskList.isEmpty()) {
            progressDialog.dismiss();
        }
    }

    /**
     * Tracks current status of <strong>Asynchronous task</strong>s.
     *
     * @return <code>true</code> if there are no <strong>Asynchronous task</strong>s in executing mode, otherwise
     * <code>false</code>.
     */
    public boolean isWorking() {
        return jsAsyncTaskList.isEmpty();
    }

    /**
     * Tracks current status of the target <strong>Asynchronous task</strong>.
     *
     * @return <code>true</code> if <strong>Asynchronous task</strong> is in executing mode, otherwise
     * <code>false</code>.
     */
    public boolean isTaskWorking(JsAsyncTask asyncTask) {
        return jsAsyncTaskList.contains(asyncTask);
    }
}
