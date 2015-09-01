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

package com.jaspersoft.android.sdk.network.entity.execution;

import com.google.gson.annotations.Expose;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static com.jaspersoft.android.sdk.test.matcher.HasAnnotation.hasAnnotation;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@RunWith(JUnitParamsRunner.class)
public class ExecutionRequestOptionsTest {

    @Rule
    public final ExpectedException mExpectedException = ExpectedException.none();
    ExecutionRequestOptions requestUnderTest;

    @Before
    public void setup() {
        requestUnderTest = ExecutionRequestOptions.newInstance();
    }

    @Test
    @Parameters({
            "async",
            "freshData",
            "ignorePagination",
            "interactive",
            "saveDataSnapshot",
            "outputFormat",
            "pages",
            "transformerKey",
            "anchor",
            "baseUrl",
    })
    public void shouldHaveExposeAnnotationForField(String fieldName) throws NoSuchFieldException {
        Field field = ExecutionRequestOptions.class.getDeclaredField(fieldName);
        assertThat(field, hasAnnotation(Expose.class));
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
}
