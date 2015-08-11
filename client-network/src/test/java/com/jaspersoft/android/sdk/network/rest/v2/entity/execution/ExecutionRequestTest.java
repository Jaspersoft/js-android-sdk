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

package com.jaspersoft.android.sdk.network.rest.v2.entity.execution;

import com.google.gson.Gson;
import com.jaspersoft.android.sdk.network.rest.v2.entity.type.GsonFactory;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(JUnitParamsRunner.class)
public class ExecutionRequestTest {

    private ExecutionRequest requestUnderTest;

    @Rule
    public final ExpectedException mExpectedException = ExpectedException.none();
    private Gson mGson;

    @Before
    public void setup() {
        requestUnderTest = ExecutionRequest.newRequest("/some/uri");
        mGson = GsonFactory.create();
    }

    @Test
    @Parameters({
            "withOutputFormat",
            "withPages",
            "withTransformerKey",
            "withAnchor",
            "withBaseUrl",
    })
    public void shouldNotAcceptNullForBuilderMethods(String methodName) throws Exception {
        mExpectedException.expect(IllegalArgumentException.class);
        Method method = requestUnderTest.getClass().getMethod(methodName, String.class);
        method.invoke(new Object[]{null});
    }

    @Test
    @Parameters({
            "withOutputFormat",
            "withPages",
            "withTransformerKey",
            "withAnchor",
            "withBaseUrl",
    })
    public void shouldNotAcceptEmptyForBuilderMethods(String methodName) throws Exception {
        mExpectedException.expect(IllegalArgumentException.class);
        Method method = requestUnderTest.getClass().getMethod(methodName, String.class);
        method.invoke("");
    }

    @Test
    public void shouldWithAttachmentsPrefixShouldEncodePrefix() {
        String prefix = requestUnderTest.withAttachmentsPrefix("./").getAttachmentsPrefix();
        assertThat(prefix, is(".%2F"));
    }

    @Test
    public void factoryMethodShouldNotAllowNull() {
        mExpectedException.expect(IllegalArgumentException.class);
        ExecutionRequest.newRequest(null);
    }

    @Test
    public void factoryMethodShouldNotAllowEmptyString() {
        mExpectedException.expect(IllegalArgumentException.class);
        ExecutionRequest.newRequest("");
    }

    @Test
    @Parameters({
            "withOutputFormat, outputFormat",
            "withPages, pages",
            "withTransformerKey, transformerKey",
            "withAnchor, anchor",
            "withBaseUrl, baseUrl",
    })
    public void shouldSerializeStringField(String methodName, String key) throws Exception {
        Method method = requestUnderTest.getClass().getMethod(methodName, String.class);
        method.invoke(requestUnderTest, "some value");
        String json = mGson.toJson(requestUnderTest);
        assertThat(json, is("{\"reportUnitUri\":\"/some/uri\",\"" + key + "\":\"some value\"}"));
    }

    @Test
    @Parameters({
            "withAsync, async",
            "withFreshData, freshData",
            "withIgnorePagination, ignorePagination",
            "withInteractive, interactive",
            "withSaveDataSnapshot, saveDataSnapshot",
    })
    public void shouldSerializeBooleanField(String methodName, String key) throws Exception {
        Method method = requestUnderTest.getClass().getMethod(methodName, boolean.class);
        method.invoke(requestUnderTest, true);
        String json = mGson.toJson(requestUnderTest);
        assertThat(json, is("{\"" + key + "\":true,\"reportUnitUri\":\"/some/uri\"}"));
    }

    @Test
    public void shouldSerializeParametersList() {
        ReportParameter reportParameter = ReportParameter.createWithEmptyValue("some_param");
        Set<ReportParameter> parameters = new HashSet<>();
        parameters.add(reportParameter);
        requestUnderTest.withParameters(parameters);
        String json = mGson.toJson(requestUnderTest);
        assertThat(json, is("{\"reportUnitUri\":\"/some/uri\",\"parameters\":{\"reportParameter\":[{\"name\":\"some_param\",\"value\":[]}]}}"));
    }
}
