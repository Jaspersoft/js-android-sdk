/*
 * Copyright (C) 2012 Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/mobile-sdk-android
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

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.WindowManager;
import com.jaspersoft.android.sdk.client.async.task.JsAsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Declaration of <strong>Asynchronous Task Manager</strong>,
 * used to manage <strong>Asynchronous Task</strong>s. It responsible for the following: <br>
 * 1) Creating a <strong>Progress dialog</strong> during it initialization; <br>
 * 2) Executing the task when received into <strong>Asynchronous Task Manager</strong> control;   <br>
 * 3) Displaying the status of the target <strong>Asynchronous Task</strong> in the <strong>Progress dialog</strong>; <br>
 * 4) Retaining/restoring the list of <strong>Asynchronous Task</strong>s from <strong>state</strong> during
 * <strong>Activity</strong> recovery  <br>
 * 5) Canceling the strong>Asynchronous task</strong> when <strong>Progress dialog</strong> is canceled.  <br>
 * 6) Closing the <strong>Progress dialog</strong> when all active <strong>Asynchronous Task</strong>s are completed.   <br>
 * 7) Informing Activity about completion or cancellation of <strong>Asynchronous Task</strong>s</p>.  <br>
 *
 * @author Volodya Sabadosh (vsabadosh@jaspersoft.com)
 * @author Ivan Gadzhega
 * @since 1.0
 *
 * @deprecated  Use {@link JsXmlSpiceService} instead.
 */

@Deprecated
public class JsAsyncTaskManager implements JsProgressTracker, DialogInterface.OnCancelListener {

    private static final String TAG = "JsAsyncTaskManager";

    private final JsOnTaskCallbackListener jsTaskCompleteListener;
    private AlertDialog progressDialog;
    private List<JsAsyncTask> jsAsyncTaskList = new ArrayList<JsAsyncTask>();

    /**
     * Creates a new <strong>Asynchronous Task Manager</strong> entity with the specified parameters.
     *
     * @param context <strong>Android</strong> context.
     * @param jsTaskCompleteListener <strong>Asynchronous task callback listener</strong>.
     */
    public JsAsyncTaskManager(Context context, JsOnTaskCallbackListener jsTaskCompleteListener) {
        this(context, jsTaskCompleteListener, null);
    }

    /**
     * Creates a new <strong>Asynchronous Task Manager</strong> entity with the specified parameters.
     *
     * @param context <strong>Android</strong> context.
     * @param jsTaskCompleteListener <strong>Asynchronous Task Callback Listener</strong>.
     * @param progressDialog <strong>Progress dialog</strong>.
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
     * Executes target <strong>Asynchronous Task</strong>.
     *
     * @param jsAsyncTask <strong>Asynchronous Task</strong>.
     */
    public void executeTask(JsAsyncTask jsAsyncTask) {
        // Keep async tasks
        jsAsyncTaskList.add(jsAsyncTask);
        // Wire task to tracker (this)
        jsAsyncTask.setProgressTracker(this);
        // Start task
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            jsAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else {
            jsAsyncTask.execute();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void onProgress(JsAsyncTask jsAsyncTask, String message) {
        if (jsAsyncTask.isShowProgressDialog()) {
            // Show dialog if it wasn't shown yet or was removed on configuration (rotation) change
            if (!progressDialog.isShowing()) {
                try {
                    progressDialog.show();
                } catch (WindowManager.BadTokenException ex) {
                    jsAsyncTask.cancel(true);
                    jsAsyncTaskList.remove(jsAsyncTask);
                    Log.w(TAG, ex.getMessage());
                }
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
     * Retains and detaches from <strong>progress tracker</strong> the list of <strong>Asynchronous Task</strong>s in
     * <strong>state</strong> during <strong>Activity</strong> destruction.
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
     * Restores the list of <strong>Asynchronous Task</strong>s from <strong>state</strong> during
     * <strong>Activity</strong> recovery.
     *
     * @param retainedTasksList list of <strong>Asynchronous Task</strong>s.
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
     * @param asyncTask <strong>Asynchronous Task</strong>.
     */
    private void finishTaskHandler(JsAsyncTask asyncTask) {
        if (progressDialog.isShowing() && asyncTask.isShowProgressDialog() && jsAsyncTaskList.isEmpty()) {
            try {
                progressDialog.dismiss();
            } catch (IllegalArgumentException ex) {
                Log.w(TAG, ex.getMessage());
            }

        }
    }

    /**
     * Tracks current status of <strong>Asynchronous Task</strong>s.
     *
     * @return <code>true</code> if there are <strong>Asynchronous Task</strong>s executing, otherwise
     * <code>false</code>.
     */
    public boolean isWorking() {
        return !jsAsyncTaskList.isEmpty();
    }

    /**
     * Tracks current status of the target <strong>Asynchronous Task</strong>.
     *
     * @return <code>true</code> if given <strong>Asynchronous Task</strong> is executing, otherwise
     * <code>false</code>.
     */
    public boolean isTaskWorking(JsAsyncTask asyncTask) {
        return jsAsyncTaskList.contains(asyncTask);
    }
}
