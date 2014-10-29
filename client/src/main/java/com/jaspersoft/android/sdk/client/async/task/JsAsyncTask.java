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

package com.jaspersoft.android.sdk.client.async.task;

import android.os.AsyncTask;

import com.jaspersoft.android.sdk.client.async.JsProgressTracker;

import java.util.Timer;
import java.util.TimerTask;

/**
 * <p>Declaration of the <strong>JsAsyncTask</strong> which is subclass of Android core <strong>AsyncTask</strong>
 * abstract class that enables proper and easy use of the <strong>Android</strong> UI thread.
 * <strong>JsAsyncTask</strong> enables of <strong>Progress dialog</strong> integration due of encapsulating of
 * <strong>Progress tracker</strong>.</p>
 *
 * @author Volodya Sabadosh (vsabadosh@jaspersoft.com)
 * @author Ivan Gadzhega
 * @since 1.0
 *
 * @deprecated Use {@link com.jaspersoft.android.sdk.client.async.request.BaseRequest BaseRequest}
 * instead.
 */

@Deprecated
public abstract class JsAsyncTask<Params, Result> extends AsyncTask<Params, String, Result> {

    // constant for dialog showing default timeout (1 second).
    public static final long SHOW_DIALOG_DEFAULT_TIMEOUT = 1000;

    private int id;
    private Result result;
    private Exception taskException;
    private JsProgressTracker progressTracker;
    private boolean showProgressDialog;
    private long showDialogTimeout;
    private String progressMessage;

    /**
     * Creates a new <strong>JsAsyncTask</strong> entity with the specified parameters.
     *
     * @param id <strong>Asynchronous task</strong> identifier.
     */
    public JsAsyncTask(int id) {
        this.id = id;
    }

    /**
     * Creates a new <strong>JsAsyncTask</strong> entity with the specified parameters.
     *
     * @param id <strong>Asynchronous task</strong> identifier.
     * @param progressMessage <strong>Progress dialog</strong> message.
     */
    public JsAsyncTask(int id, String progressMessage) {
        this(id, progressMessage, SHOW_DIALOG_DEFAULT_TIMEOUT);
        showProgressDialog = true;
    }

    /**
     * Creates a new <strong>JsAsyncTask</strong> entity with the specified parameters.
     *
     * @param id <strong>Asynchronous task</strong> identifier.
     * @param progressMessage <strong>Progress dialog</strong> message.
     * @param showDialogTimeout the time interval (in milliseconds) <strong>Progress dialog</strong> should be appear
     * after.
     */
    public JsAsyncTask(int id, String progressMessage, long showDialogTimeout) {
        this.id = id;
        showProgressDialog = true;
        this.showDialogTimeout = showDialogTimeout;
        this.progressMessage = progressMessage;
    }

    /**
     * Attaches <strong>Progress tracker</strong> for target <strong>Asynchronous task</strong>.
     *
     * @param progressTracker <strong>Progress tracker</strong>.
     */
    public void setProgressTracker(JsProgressTracker progressTracker) {
        // Attach to progress tracker
        this.progressTracker = progressTracker;
        // Initialise progress tracker with current task state
        if (this.progressTracker != null) {
            //this.progressTracker.onProgress(progressMessage);
            if (result != null) {
                this.progressTracker.onComplete(this);
            }
        }
    }

    /**
     * Overrides the android core <strong>AsyncTask</strong> method which runs on the UI thread after
     * <code>cancel(boolean)</code> is invoked and <code>doInBackground(Object[])</code> has finished. Overriding method
     * detaches from progress tracker.
     */
    @Override
    protected void onCancelled() {
        // Detach from progress tracker
        this.progressTracker = null;
    }

    /**
     * Overrides the android core <strong>AsyncTask</strong> method which invokes on the UI thread after a
     * <code>publishProgress(Progress...)</code> calling. Overriding method update progress message and sent it to
     * progress tracker.
     *
     * @param values the values indicating progress - <strong>progress dialog</strong> message in current implementation.
     */
    @Override
    protected void onProgressUpdate(String... values) {
        // Update progress message
        this.progressMessage = values[0];
        // And send it to progress tracker
        if (this.progressTracker != null) {
            this.progressTracker.onProgress(this, this.progressMessage);
        }
    }

    /**
     * Overrides the android core <strong>AsyncTask</strong> method which runs on the UI thread after
     * <code>doInBackground(Params...)</code>. This method won't be invoked if the task was cancelled.
     *
     * @param result the specified result is the value returned by <code>doInBackground(Params...)</code>.
     */
    @Override
    protected void onPostExecute(Result result) {
        // Update result
        this.result = result;
        // And send it to progress tracker
        if (taskException == null) {
            progressTracker.onComplete(this);
        } else {
            progressTracker.onException(this);
        }
        // Detach from progress tracker
        progressTracker = null;
    }

    /**
     * Overrides the android core <strong>AsyncTask</strong> method which performs a computation on a background thread.
     * Overriding is done by scheduling of <strong>progress dialog</strong> if it required for target
     * <strong>Asynchronous task</strong>.
     *
     * @param params the parameters of the <strong>Asynchronous task</strong>.
     * @return a result, defined by the subclass of this class.
     */
    protected Result doInBackground(Params... params) {
        if (showProgressDialog) {
            //Init starting time of execution show progress dialog.
            Timer timer = new Timer();
            TimerTask showProgressDialogTask = new ShowProgressDialogTask();
            //Show progress dialog after <param> showDialogTimeout milliseconds </param>.
            timer.schedule(showProgressDialogTask, showDialogTimeout);
        }
        return null;
    }

    /**
     * Declaration of runnable <strong>ShowProgressDialogTask</strong> which publishes updates on the UI thread while the
     * background computation is still running.
     */
    private class ShowProgressDialogTask extends TimerTask {
        @Override
        public void run() {
            publishProgress(progressMessage);
        }
    }

    /**
     * Tracks whether to show <strong>Progress dialog</strong> for target <strong>Asynchronous task</strong>.
     *
     * @return <code>true</code> if <strong>Asynchronous task</strong> required of <strong>Progress dialog</strong>
     * showing, otherwise <code>false</code>.
     */
    public boolean isShowProgressDialog() {
        return showProgressDialog;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Exception getTaskException() {
        return taskException;
    }

    public void setTaskException(Exception taskException) {
        this.taskException = taskException;
    }
}
