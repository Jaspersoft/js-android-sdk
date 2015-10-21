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

package com.jaspersoft.android.sdk.client.async.request;

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.retry.DefaultRetryPolicy;

import roboguice.util.temp.Ln;

/**
 * Base class for writing requests using {@link JsRestClient}.
 * Simply override {@link #loadDataFromNetwork()} to define the network operation of a request.
 *
 * @author Ivan Gadzhega
 * @since 1.6
 */
public abstract class BaseRequest<T> extends SpiceRequest<T> {

    private JsRestClient jsRestClient;

    public BaseRequest(JsRestClient jsRestClient, Class<T> clazz) {
        super(clazz);
        this.setRetryPolicy(new DefaultRetryPolicy(0, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.jsRestClient = jsRestClient;
    }

    /**
     * This method doesn't really work within the Spring Android module : once the request is
     * loading data from network, there is no way to interrupt it. This is weakness of the spring android framework,
     * and seems to come from even deeper. The IO operations on which it relies don't support the interrupt flag
     * properly.
     * Nevertheless, there are still some opportunities to cancel the request, basically during cache operations.
     */
    @Override
    public void cancel() {
        super.cancel();
        Ln.w(BaseRequest.class.getName(), "Cancel can't be invoked directly on "
                + BaseRequest.class.getName() + " requests. You must call SpiceManager.cancelAllRequests().");
    }

    public JsRestClient getJsRestClient() {
        return jsRestClient;
    }

}
