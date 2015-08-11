/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of Jaspersoft Mobile for Android.
 *
 * Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.network.rest.v2.api;

import com.jaspersoft.android.sdk.network.rest.v2.entity.resource.ResourceSearchResponse;

import retrofit.client.Header;
import retrofit.client.Response;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class ResourceSearchResponseAdapter {

    private ResourceSearchResponseAdapter() {}

    public static ResourceSearchResponse adapt(Response response) {
        ResponseWrapper<ResourceSearchResponse> wrapper = ResponseWrapper.wrap(response, ResourceSearchResponse.class);
        Header resultCountHeader = wrapper.getFirstHeader("Result-Count");
        Header totalCountHeader = wrapper.getFirstHeader("Total-Count");
        Header startIndexHeader = wrapper.getFirstHeader("Start-Index");
        Header nextOffsetHeader = wrapper.getFirstHeader("Next-Offset");

        int resultCount = asInt(resultCountHeader);
        int totalCount = asInt(totalCountHeader);
        int startIndex = asInt(startIndexHeader);
        int nextOffset = asInt(nextOffsetHeader);

        ResourceSearchResponse resourceSearchResponse = wrapper.parseResponse();
        resourceSearchResponse.setResultCount(resultCount);
        resourceSearchResponse.setTotalCount(totalCount);
        resourceSearchResponse.setStartIndex(startIndex);
        resourceSearchResponse.setNextOffset(nextOffset);
        return resourceSearchResponse;
    }

    public static ResourceSearchResponse emptyResponse() {
        return new ResourceSearchResponse();
    }

    static int asInt(Header header) {
        if (header == null) {
            return 0;
        }
        String value = header.getValue();
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

}
