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

import static org.hamcrest.Matchers.containsString;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class IsRecordedRequestHasPath extends TypeSafeDiagnosingMatcher<RecordedRequest> {
    private final String mPath;

    public IsRecordedRequestHasPath(String path) {
        mPath = path;
    }

    @Override
    protected boolean matchesSafely(RecordedRequest item, Description mismatchDescription) {
        Matcher<String> pathMatcher = containsString(mPath);
        String targetPath = item.getPath();
        if (pathMatcher.matches(targetPath)) {
            return true;
        }
        mismatchDescription.appendText("recorded request path was ")
                .appendText(String.format("'%s'", targetPath));
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("recorded request path should be ")
                .appendText(String.format("'%s'", mPath));
    }

    public static <T> Matcher<? super RecordedRequest> hasPath(String path) {
        return new IsRecordedRequestHasPath(path);
    }
}
