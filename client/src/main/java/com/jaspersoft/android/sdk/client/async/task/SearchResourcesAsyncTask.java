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

package com.jaspersoft.android.sdk.client.async.task;

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.oxm.ResourceDescriptor;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Declaration of the <strong>SearchResourcesAsyncTask</strong> which is subclass of <strong>JsRestAsyncTask</strong>
 * abstract class and overrides <code>doInBackground(Object... arg0)</code> method from it.</p>
 *
 * @author Volodya Sabadosh (vsabadosh@jaspersoft.com)
 * @author Ivan Gadzhega
 * @since 1.0
 *
 * @deprecated Use {@link com.jaspersoft.android.sdk.client.async.request.cacheable.SearchResourcesRequest SearchResourcesRequest}
 * instead.
 */

@Deprecated
public class SearchResourcesAsyncTask extends JsRestAsyncTask<Object, List<ResourceDescriptor>> {

    private String resourceUri;
    private String query;
    private List<String> types;
    private Boolean recursive;
    private Integer limit;

    /**
     * Creates a new <strong>SearchResourcesAsyncTask</strong> entity with the specified parameters.
     *
     * @param id <strong>SearchResourcesAsyncTask</strong> identifier.
     * @param jsRestClient <strong>JsRestClient</strong>.
     * @param resourceUri repository URI (e.g. /reports/samples/)
     * @param query Match only resources having the specified text in the name or description (can be <code>null</code>)
     * @param type  Match only resources of the given type
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
     * @param resourceUri repository URI (e.g. /reports/samples/)
     * @param query Match only resources having the specified text in the name or description (can be <code>null</code>)
     * @param type  Match only resources of the given type
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
     * @param resourceUri resource URI (e.g. /reports/samples/)
     * @param query Match only resources having the specified text in the name or description (can be <code>null</code>)
     * @param type  Match only resources of the given type
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

    /**
     * Creates a new <strong>SearchResourcesAsyncTask</strong> entity with the specified parameters.
     *
     * @param id <strong>SearchResourcesAsyncTask</strong> identifier.
     * @param jsRestClient <strong>JsRestClient</strong>.
     * @param resourceUri repository URI (e.g. /reports/samples/)
     * @param query Match only resources having the specified text in the name or description (can be <code>null</code>)
     * @param types  Match only resources of the given types
     * @param recursive Get resources recursively and not only in the specified URI. Used only when a search criteria
     *                  is specified, either query or type. (can be <code>null</code>)
     * @param limit Maximum number of items returned to the client. The default is 0 (can be <code>null</code>),
     *                  meaning no limit.
     */
    public SearchResourcesAsyncTask(int id, JsRestClient jsRestClient, String resourceUri, String query, List<String> types,
                                    Boolean recursive, Integer limit) {
        super(id, jsRestClient);
        init(resourceUri, query, types, recursive, limit);
    }

    /**
     * Creates a new <strong>SearchResourcesAsyncTask</strong> entity with the specified parameters.
     *
     * @param id <strong>SearchResourcesAsyncTask</strong> identifier.
     * @param progressMessage message of <strong>Progress dialog</strong>.
     * @param jsRestClient <strong>JsRestClient</strong>.
     * @param resourceUri repository URI (e.g. /reports/samples/)
     * @param query Match only resources having the specified text in the name or description (can be <code>null</code>)
     * @param types  Match only resources of the given types
     * @param recursive Get resources recursively and not only in the specified URI. Used only when a search criteria
     *                  is specified, either query or type. (can be <code>null</code>)
     * @param limit Maximum number of items returned to the client. The default is 0 (can be <code>null</code>),
     *                  meaning no limit.
     */
    public SearchResourcesAsyncTask(int id, String progressMessage, JsRestClient jsRestClient, String resourceUri,
                                    String query, List<String> types, Boolean recursive, Integer limit) {
        super(id, progressMessage, jsRestClient);
        init(resourceUri, query, types, recursive, limit);
    }

    /**
     * Creates a new <strong>SearchResourcesAsyncTask</strong> entity with the specified parameters.
     *
     * @param id <strong>SearchResourcesAsyncTask</strong> identifier.
     * @param progressMessage message of <strong>Progress dialog</strong>.
     * @param showDialogTimeout the time interval (in milliseconds) <strong>Progress dialog</strong> should be appear
     * after.
     * @param jsRestClient <strong>JsRestClient</strong>.
     * @param resourceUri resource URI (e.g. /reports/samples/)
     * @param query Match only resources having the specified text in the name or description (can be <code>null</code>)
     * @param types  Match only resources of the given type
     * @param recursive Get resources recursively and not only in the specified URI. Used only when a search criteria
     *                  is specified, either query or type. (can be <code>null</code>)
     * @param limit Maximum number of items returned to the client. The default is 0 (can be <code>null</code>),
     *                  meaning no limit.
     */
    public SearchResourcesAsyncTask(int id, String progressMessage, long showDialogTimeout, JsRestClient jsRestClient,
                                    String resourceUri, String query, List<String> types, Boolean recursive, Integer limit) {
        super(id, progressMessage, showDialogTimeout, jsRestClient);
        init(resourceUri, query, types, recursive, limit);
    }

    private void init(String resourceUri, String query, String type, Boolean recursive, Integer limit) {
        List<String> types = new ArrayList<String>();
        types.add(type);
        init(resourceUri, query, types, recursive, limit);
    }

    private void init(String resourceUri, String query, List<String> types, Boolean recursive, Integer limit) {
        this.resourceUri = resourceUri;
        this.query = query;
        this.types = types;
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
            return getJsRestClient().getResourcesList(resourceUri, query, types, recursive, limit);
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

    public List<String> getTypes() {
        return types;
    }

    public Boolean getRecursive() {
        return recursive;
    }

    public Integer getLimit() {
        return limit;
    }
}
