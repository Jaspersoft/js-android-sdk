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

import android.os.AsyncTask;
import com.jaspersoft.android.sdk.client.async.JsProgressTracker;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Declaration
 * @author Volodya Sabadosh (vsabadosh@jaspersoft.com)
 * @author Ivan Gadzhega
 * @version $Id$
 * @since 1.0
 */
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


    public JsAsyncTask(int id) {
        this.id = id;
    }

    public JsAsyncTask(int id, String progressMessage) {
        this(id, progressMessage, SHOW_DIALOG_DEFAULT_TIMEOUT);
        showProgressDialog = true;
    }

    public JsAsyncTask(int id, String progressMessage, long showDialogTimeout) {
        this.id = id;
        showProgressDialog = true;
        this.showDialogTimeout = showDialogTimeout;
        this.progressMessage = progressMessage;
    }

    /* UI Thread */
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

    /* UI Thread */
    @Override
    protected void onCancelled() {
        // Detach from progress tracker
        this.progressTracker = null;
    }

    /* UI Thread */
    @Override
    protected void onProgressUpdate(String... values) {
        // Update progress message
        this.progressMessage = values[0];
        // And send it to progress tracker
        if (this.progressTracker != null) {
            this.progressTracker.onProgress(this, this.progressMessage);
        }
    }

    /* UI Thread */
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

    private class ShowProgressDialogTask extends TimerTask {
        @Override
        public void run() {
            publishProgress(progressMessage);
        }
    }

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
