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

package com.jaspersoft.android.sdk.client.async.task;

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.oxm.ResourceDescriptor;

/**
 * <p>Declaration of the <strong>ModifyResourceAsyncTask</strong> which is subclass of <strong>JsRestAsyncTask</strong>
 * abstract class and overrides <code>doInBackground(Object... arg0)</code> method from it.</p>
 *
 * @author Volodya Sabadosh (vsabadosh@jaspersoft.com)
 * @author Ivan Gadzhega
 * @version $Id$
 * @since 1.0
 */
public class ModifyResourceAsyncTask extends JsRestAsyncTask<Object, Void> {

    private ResourceDescriptor resourceDescriptor;

    /**
     * Creates a new <strong>ModifyResourceAsyncTask</strong> entity with the specified parameters.
     *
     * @param id <strong>ModifyResourceAsyncTask</strong> identifier.
     * @param jsRestClient <strong>JsRestClient</strong>.
     * @param resourceDescriptor <strong>Resource descriptor</strong>.
     */
    public ModifyResourceAsyncTask(int id, JsRestClient jsRestClient, ResourceDescriptor resourceDescriptor) {
        super(id, jsRestClient);
        this.resourceDescriptor = resourceDescriptor;
    }

    /**
     * Creates a new <strong>ModifyResourceAsyncTask</strong> entity with the specified parameters.
     *
     * @param id <strong>ModifyResourceAsyncTask</strong> identifier.
     * @param progressMessage message of <strong>Progress dialog</strong>.
     * @param jsRestClient <strong>JsRestClient</strong>.
     * @param resourceDescriptor <strong>Resource descriptor</strong>.
     */
    public ModifyResourceAsyncTask(int id, String progressMessage,
            JsRestClient jsRestClient, ResourceDescriptor resourceDescriptor) {
        super(id, progressMessage, jsRestClient);
        this.resourceDescriptor = resourceDescriptor;
    }

    /**
     *
     * @param id <strong>ModifyResourceAsyncTask</strong> identifier.
     * @param progressMessage message of <strong>Progress dialog</strong>.
     * @param showDialogTimeout the time interval (in milliseconds) <strong>Progress dialog</strong> should be appear
     * after.
     * @param jsRestClient <strong>JsRestClient</strong>.
     * @param resourceDescriptor <strong>Resource descriptor</strong>.
     */
    public ModifyResourceAsyncTask(int id, String progressMessage,
            JsRestClient jsRestClient, long showDialogTimeout, ResourceDescriptor resourceDescriptor) {
        super(id, progressMessage, showDialogTimeout, jsRestClient);
        this.resourceDescriptor = resourceDescriptor;
    }

    /**
     * Overrides the <code>doInBackground(Object... arg0)</code> method by calling <strong>JsRestClient</strong>
     * <code>modifyResource(ResourceDescriptor resourceDescriptor)</code> method.
     *
     * @param arg0 the parameters of the <strong>Asynchronous task</strong>. Current implementation does not use this params.
     * @return nothing.
     */
    @Override
    protected Void doInBackground(Object... arg0) {
        super.doInBackground(arg0);
        try {
            getJsRestClient().modifyResource(this.resourceDescriptor);
        } catch (Exception e) {
            setTaskException(e);
        }
        return null;
    }

    // Getters

    public ResourceDescriptor getResourceDescriptor() {
        return resourceDescriptor;
    }
}
