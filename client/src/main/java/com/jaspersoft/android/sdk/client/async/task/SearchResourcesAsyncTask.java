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

import java.util.List;

/**
 * <p>Declaration of the <strong>SearchResourcesAsyncTask</strong> which is subclass of <strong>JsRestAsyncTask</strong>
 * abstract class and overrides <code>doInBackground(Object... arg0)</code> method from it.</p>
 *
 * @author Volodya Sabadosh (vsabadosh@jaspersoft.com)
 * @author Ivan Gadzhega
 * @version $Id$
 * @since 1.0
 */
public class SearchResourcesAsyncTask extends JsRestAsyncTask<Object, List<ResourceDescriptor>> {

    private String resourceUri;
    private String query;
    private String type;
    private Boolean recursive;
    private Integer limit;

    /**
     * Creates a new <strong>SearchResourcesAsyncTask</strong> entity with the specified parameters.
     *
     * @param id <strong>SearchResourcesAsyncTask</strong> identifier.
     * @param jsRestClient <strong>JsRestClient</strong>.
     * @param resourceUri repository URI (i.e. /reports/samples/)
     * @param query Match only resources having the specified text in the name or description (can be <code>null</code>)
     * @param type  Match only resources of the given type (can be <code>null</code>)
     * @param recursive Get resources recursively and not only in the specified URI. Used only when a search criteria
     *                  is specified, either query or type. (can be <code>null</code>)
     * @param limit Maximum number of items returned to the client. The default is 0 (can be <code>null</code>),
     *                  meaning no limit.
     */
    public SearchResourcesAsyncTask(int id, JsRestClient jsRestClient, String resourceUri, String query, String type,
            Boolean recursive, Integer limit) {
        super(id, jsRestClient);
        init(resourceUri, query, type, recursive, limit);
    }

    /**
     * Creates a new <strong>SearchResourcesAsyncTask</strong> entity with the specified parameters.
     *
     * @param id <strong>SearchResourcesAsyncTask</strong> identifier.
     * @param progressMessage message of <strong>Progress dialog</strong>.
     * @param jsRestClient <strong>JsRestClient</strong>.
     * @param resourceUri repository URI (i.e. /reports/samples/)
     * @param query Match only resources having the specified text in the name or description (can be <code>null</code>)
     * @param type  Match only resources of the given type (can be <code>null</code>)
     * @param recursive Get resources recursively and not only in the specified URI. Used only when a search criteria
     *                  is specified, either query or type. (can be <code>null</code>)
     * @param limit Maximum number of items returned to the client. The default is 0 (can be <code>null</code>),
     *                  meaning no limit.
     */
    public SearchResourcesAsyncTask(int id, String progressMessage, JsRestClient jsRestClient, String resourceUri,
            String query, String type, Boolean recursive, Integer limit) {
        super(id, progressMessage, jsRestClient);
        init(resourceUri, query, type, recursive, limit);
    }

    /**
     * Creates a new <strong>SearchResourcesAsyncTask</strong> entity with the specified parameters.
     *
     * @param id <strong>SearchResourcesAsyncTask</strong> identifier.
     * @param progressMessage message of <strong>Progress dialog</strong>.
     * @param showDialogTimeout the time interval (in milliseconds) <strong>Progress dialog</strong> should be appear
     * after.
     * @param jsRestClient <strong>JsRestClient</strong>.
     * @param resourceUri resource URI (i.e. /reports/samples/)
     * @param query Match only resources having the specified text in the name or description (can be <code>null</code>)
     * @param type  Match only resources of the given type (can be <code>null</code>)
     * @param recursive Get resources recursively and not only in the specified URI. Used only when a search criteria
     *                  is specified, either query or type. (can be <code>null</code>)
     * @param limit Maximum number of items returned to the client. The default is 0 (can be <code>null</code>),
     *                  meaning no limit.
     */
    public SearchResourcesAsyncTask(int id, String progressMessage, long showDialogTimeout, JsRestClient jsRestClient,
            String resourceUri, String query, String type, Boolean recursive, Integer limit) {
        super(id, progressMessage, showDialogTimeout, jsRestClient);
        init(resourceUri, query, type, recursive, limit);
    }

    private void init(String resourceUri, String query, String type, Boolean recursive, Integer limit) {
        this.resourceUri = resourceUri;
        this.query = query;
        this.type = type;
        this.recursive = recursive;
        this.limit = limit;
    }

    /**
     * Overrides the <code>doInBackground(Object... arg0)</code> method by calling <strong>JsRestClient</strong>
     * <code>getResourcesList(...)</code> method.
     *
     * @param arg0 the parameters of the <strong>Asynchronous task</strong>. Current implementation does not use this params.
     * @return the list of <strong>ResourceDescriptor</strong>s.
     */
    @Override
    protected List<ResourceDescriptor> doInBackground(Object... arg0) {
        super.doInBackground(arg0);
        try {
            return getJsRestClient().getResourcesList(resourceUri, query, type, recursive, limit);
        } catch (Exception e) {
            setTaskException(e);
            return null;
        }
    }

    // Getters

    public String getResourceUri() {
        return resourceUri;
    }

    public String getQuery() {
        return query;
    }

    public String getType() {
        return type;
    }

    public Boolean getRecursive() {
        return recursive;
    }

    public Integer getLimit() {
        return limit;
    }
}
