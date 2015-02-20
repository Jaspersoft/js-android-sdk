/*
 * Copyright (C) 2012-2013 Jaspersoft Corporation. All rights reserved.
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

package com.jaspersoft.android.sdk.client.async.request.cacheable;

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.oxm.resource.ResourceLookupSearchCriteria;
import com.jaspersoft.android.sdk.client.oxm.resource.ResourceLookupsList;

import java.util.List;

/**
 * Request that retrieves the list of resource lookup objects for the resources
 * contained in the given parent folder and matching the specified parameters.
 *
 * @author Ivan Gadzhega
 * @since 1.7
 */
public class GetResourceLookupsRequest extends CacheableRequest<ResourceLookupsList> {

    private ResourceLookupSearchCriteria searchCriteria;

    /**
     * Creates a new instance of {@link GetResourceLookupsRequest}.
     *
     * @param folderUri parent folder URI (e.g. /reports/samples/)
     * @param recursive Get resources recursively
     * @param offset    Pagination. Start index for requested page.
     * @param limit     Pagination. Resources count per page.
     */
    public GetResourceLookupsRequest(JsRestClient jsRestClient, String folderUri, boolean recursive, int offset, int limit) {
        this(jsRestClient, folderUri, null, null, recursive, offset, limit);
    }

    /**
     * Creates a new instance of {@link GetResourceLookupsRequest}.
     *
     * @param folderUri parent folder URI (e.g. /reports/samples/)
     * @param types     Match only resources of the given types. Multiple resource types allowed.
     *                  (can be <code>null</code>)
     * @param offset    Pagination. Start index for requested page.
     * @param limit     Pagination. Resources count per page.
     */
    public GetResourceLookupsRequest(JsRestClient jsRestClient, String folderUri, List<String> types, int offset, int limit) {
        this(jsRestClient, folderUri, null, types, false, offset, limit);
    }

    /**
     * Creates a new instance of {@link GetResourceLookupsRequest}.
     *
     * @param folderUri parent folder URI (e.g. /reports/samples/)
     * @param query     Match only resources having the specified text in the name or description.
     *                  (can be <code>null</code>)
     * @param types     Match only resources of the given types. Multiple resource types allowed.
     *                  (can be <code>null</code>)
     * @param recursive Get resources recursively
     * @param offset    Pagination. Start index for requested page.
     * @param limit     Pagination. Resources count per page.
     */
    public GetResourceLookupsRequest(JsRestClient jsRestClient, String folderUri, String query, List<String> types,
                                     boolean recursive, int offset, int limit) {
        this(jsRestClient, folderUri, query, types, null, recursive, offset, limit);
    }

    /**
     * Creates a new instance of {@link GetResourceLookupsRequest}.
     *
     * @param folderUri parent folder URI (e.g. /reports/samples/)
     * @param query     Match only resources having the specified text in the name or description.
     *                  (can be <code>null</code>)
     * @param types     Match only resources of the given types. Multiple resource types allowed.
     *                  (can be <code>null</code>)
     * @param sortBy    Represents a field in the results to sort by: uri, label, description, type, creationDate,
     *                  updateDate, accessTime, or popularity (based on access events).
     *                  (can be <code>null</code>)
     * @param recursive Get resources recursively
     * @param offset    Pagination. Start index for requested page.
     * @param limit     Pagination. Resources count per page.
     */
    public GetResourceLookupsRequest(JsRestClient jsRestClient, String folderUri, String query, List<String> types,
                                     String sortBy, boolean recursive, int offset, int limit) {
        super(jsRestClient, ResourceLookupsList.class);
        searchCriteria = new ResourceLookupSearchCriteria();
        searchCriteria.setFolderUri(folderUri);
        searchCriteria.setQuery(query);
        searchCriteria.setTypes(types);
        searchCriteria.setSortBy(sortBy);
        searchCriteria.setRecursive(recursive);
        searchCriteria.setOffset(offset);
        searchCriteria.setLimit(limit);
    }

    /**
     * Creates a new instance of {@link GetResourceLookupsRequest}.
     *
     * @param searchCriteria the search criteria
     */
    public GetResourceLookupsRequest(JsRestClient jsRestClient, ResourceLookupSearchCriteria searchCriteria) {
        super(jsRestClient, ResourceLookupsList.class);
        this.searchCriteria = searchCriteria;
    }

    @Override
    public ResourceLookupsList loadDataFromNetwork() throws Exception {
        return getJsRestClient().getResourceLookups(searchCriteria);
    }

    @Override
    protected String createCacheKeyString() {
        return super.createCacheKeyString()
                + searchCriteria.getFolderUri()
                + searchCriteria.getQuery()
                + searchCriteria.getTypes()
                + searchCriteria.getSortBy()
                + searchCriteria.isRecursive()
                + searchCriteria.getOffset()
                + searchCriteria.getLimit();
    }

    //---------------------------------------------------------------------
    // Getters & Setters
    //---------------------------------------------------------------------

    public ResourceLookupSearchCriteria getSearchCriteria() {
        return searchCriteria;
    }

}
