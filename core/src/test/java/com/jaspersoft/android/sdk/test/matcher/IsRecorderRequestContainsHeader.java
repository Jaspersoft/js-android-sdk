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

import com.squareup.okhttp.mockwebserver.RecordedRequest;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.core.IsCollectionContaining.hasItem;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class IsRecorderRequestContainsHeader extends TypeSafeDiagnosingMatcher<RecordedRequest> {
    private final String mHeaderName;
    private final String mHeaderValue;

    public IsRecorderRequestContainsHeader(String headerName, String headerValue) {
        mHeaderName = headerName;
        mHeaderValue = headerValue;
    }

    @Override
    public void describeTo(Description description) {
        description
                .appendText("recorded request missing header ")
                .appendText(String.format("with key '%s' and/or value '%s'", mHeaderName, mHeaderValue));
    }

    @Override
    protected boolean matchesSafely(RecordedRequest item, Description mismatchDescription) {
        Map<String, List<String>> headers = item.getHeaders().toMultimap();

        Matcher<Iterable<? super String>> headerKeyMatcher = hasItem(mHeaderName);
        Set<String> headerKeys = headers.keySet();
        if (headerKeyMatcher.matches(headerKeys)) {

            List<String> headerValues = new ArrayList<>();
            for (List<String> values : headers.values()) {
                headerValues.addAll(values);
            }
            Matcher<Iterable<? super String>> headerValuesMatcher = hasItem(mHeaderValue);

            if (headerValuesMatcher.matches(headerValues)){
                return true;
            } else {
                mismatchDescription.appendText("recorder request headers values was ")
                        .appendText(String.valueOf(headerValues));
            }
        } else {
            mismatchDescription.appendText("recorder request headers keys was ")
                    .appendText(String.valueOf(headerKeys));
        }
        return false;
    }

    public static <T> Matcher<? super RecordedRequest> containsHeader(String headerName, String headerValue) {
        return new IsRecorderRequestContainsHeader(headerName, headerValue);
    }
}
