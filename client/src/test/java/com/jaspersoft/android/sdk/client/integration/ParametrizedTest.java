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

package com.jaspersoft.android.sdk.client.integration;

import com.jaspersoft.android.sdk.client.JsRestClient;
import com.jaspersoft.android.sdk.client.util.FactoryGirl;
import com.jaspersoft.android.sdk.client.util.ServerCollection;
import com.jaspersoft.android.sdk.client.util.ServerUnderTest;

import java.util.Collection;

/**
 * @author Tom Koptel
 * @since 1.10
 */
public abstract class ParametrizedTest {
    private final JsRestClient jsRestClient;
    private final FactoryGirl factoryGirl;
    private final ServerUnderTest mServer;
    private final String mDatatype;

    public static Collection<Object[]> data(Class<?> targetClass) {
        return ServerCollection.newInstance(targetClass).load();
    }

    protected ParametrizedTest(String versionCode, String url, String dataType) {
        mServer = ServerUnderTest.createBuilderWithDefaults()
                .setVersionCode(versionCode)
                .setServerUrl(url)
                .build();
        mDatatype = dataType;
        factoryGirl = FactoryGirl.newInstance();
        jsRestClient = factoryGirl.createJsRestClient(dataType, mServer);
    }

    public JsRestClient getJsRestClient() {
        return jsRestClient;
    }

    public FactoryGirl getFactoryGirl() {
        return factoryGirl;
    }

    public String getDatatype() {
        return mDatatype;
    }

    public ServerUnderTest getServer() {
        return mServer;
    }
}
