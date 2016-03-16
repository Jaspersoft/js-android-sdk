/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile for Android.
 *
 * TIBCO Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.test.matcher;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public class IsRecordedRequestHasQuery extends TypeSafeDiagnosingMatcher<RecordedRequest> {
    private final String mQueryName;
    private final String mQueryValue;

    public IsRecordedRequestHasQuery(String queryName, String queryValue) {
        mQueryName = queryName;
        mQueryValue = queryValue;
    }

    @Override
    protected boolean matchesSafely(RecordedRequest item, Description mismatchDescription) {
        HttpUrl url = HttpUrl.parse("http://stub/").resolve(item.getPath());
        Matcher<Iterable<? super String>> queryNameMatcher = hasItem(mQueryName);
        Matcher<Iterable<? super String>> queryValueMatcher = hasItem(mQueryValue);
        assertThat(url.queryParameterNames(), Matchers.hasItem(mQueryName));
        if (queryNameMatcher.matches(url.queryParameterNames()) &&
                queryValueMatcher.matches(url.queryParameterValues(mQueryName))) {
            return true;
        }
        mismatchDescription.appendText("recorded request query was ")
                .appendText(url.query())
                .appendDescriptionOf(queryNameMatcher);
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("recorded request query ")
                .appendText(String.format("should contain '%s=%s'", mQueryName, mQueryValue));
    }

    public static <T> Matcher<? super RecordedRequest> hasQuery(String queryName, String queryValue) {
        return new IsRecordedRequestHasQuery(queryName, queryValue);
    }
}
