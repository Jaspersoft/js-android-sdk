/*
 * Copyright � 2015 TIBCO Software, Inc. All rights reserved.
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

package com.jaspersoft.android.sdk.network.api;

import com.jaspersoft.android.sdk.test.MockResponseFactory;
import com.jaspersoft.android.sdk.test.WebMockRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Collections;

/**
 * @author Tom Koptel
 * @since 2.0
 */
@SuppressWarnings("unchecked")
public class ReportOptionRestApiTest {
    @Rule
    public final WebMockRule mWebMockRule = new WebMockRule();
    @Rule
    public final ExpectedException mExpectedException = ExpectedException.none();

    private ReportOptionRestApi restApiUnderTest;

    @Before
    public void setup() {
        restApiUnderTest = new ReportOptionRestApi.Builder(mWebMockRule.getRootUrl()).build();
    }

    @Test
    public void requestReportOptionsListShouldNotAllowNullReportUnitUri() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Report uri should not be null");
        restApiUnderTest.requestReportOptionsList(null);
    }

    @Test
    public void requestReportOptionsListShouldThrow500Error() {
        mExpectedException.expect(RestError.class);

        mWebMockRule.enqueue(MockResponseFactory.create500());

        restApiUnderTest.requestReportOptionsList("any_id");
    }

    @Test
    public void updateReportOptionShouldThrowRestErrorFor500() {
        mExpectedException.expect(RestError.class);

        mWebMockRule.enqueue(MockResponseFactory.create500());

        restApiUnderTest.updateReportOption("any_id", "option_id", Collections.EMPTY_MAP);
    }

    @Test
    public void deleteReportOptionShouldThrowRestErrorFor500() {
        mExpectedException.expect(RestError.class);

        mWebMockRule.enqueue(MockResponseFactory.create500());

        restApiUnderTest.deleteReportOption("any_id", "option_id");
    }

    @Test
    public void createReportOptionShouldNotAllowNullReportUri() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Report uri should not be null");
        restApiUnderTest.createReportOption(null, "label", Collections.EMPTY_MAP, false);
    }


    @Test
    public void createReportOptionShouldNotAllowNullOptionLabel() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Option label should not be null");
        restApiUnderTest.createReportOption("any_id", null, Collections.EMPTY_MAP, false);
    }

    @Test
    public void createReportOptionShouldNotAllowNullControlsValues() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Controls values should not be null");
        restApiUnderTest.createReportOption("any_id", "label", null, false);
    }

    @Test
    public void updateReportOptionShouldNotAllowNullReportUri() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Report uri should not be null");
        restApiUnderTest.updateReportOption(null, "option_id", Collections.EMPTY_MAP);
    }

    @Test
    public void updateReportOptionShouldNotAllowNullOptionId() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Option id should not be null");
        restApiUnderTest.updateReportOption("any_id", null, Collections.EMPTY_MAP);
    }

    @Test
    public void updateReportOptionShouldNotAllowNullControlsValues() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Controls values should not be null");
        restApiUnderTest.updateReportOption("any_id", "option_id", null);
    }

    @Test
    public void deleteReportOptionShouldNotAllowNullReportUri() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Report uri should not be null");
        restApiUnderTest.deleteReportOption(null, "option_id");
    }

    @Test
    public void deleteReportOptionShouldNotAllowNullOptionId() {
        mExpectedException.expect(NullPointerException.class);
        mExpectedException.expectMessage("Option id should not be null");
        restApiUnderTest.deleteReportOption("any_id", null);
    }
}
