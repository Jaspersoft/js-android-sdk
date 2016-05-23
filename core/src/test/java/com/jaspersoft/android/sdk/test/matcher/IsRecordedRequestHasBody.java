/*
 * Copyright (C) 2016 TIBCO Jaspersoft Corporation. All rights reserved.
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

package com.jaspersoft.android.sdk.test.matcher;

import com.squareup.okhttp.mockwebserver.RecordedRequest;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import static org.hamcrest.Matchers.is;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class IsRecordedRequestHasBody extends TypeSafeDiagnosingMatcher<RecordedRequest> {
    private final String mBody;

    public IsRecordedRequestHasBody(String body) {
        mBody = body;
    }

    @Override
    protected boolean matchesSafely(RecordedRequest item, Description mismatchDescription) {
        Matcher<String> pathMatcher = is(mBody);
        String targetBody = item.getBody().readUtf8();
        if (pathMatcher.matches(targetBody)) {
            return true;
        }
        mismatchDescription.appendText("recorded request body was ")
                .appendText(String.format("'%s'", targetBody));
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("recorded request body should be ")
                .appendText(String.format("'%s'", mBody));
    }

    public static <T> Matcher<? super RecordedRequest> hasBody(String path) {
        return new IsRecordedRequestHasBody(path);
    }
}
