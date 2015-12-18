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

package com.jaspersoft.android.sdk.network;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.util.*;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class Cookies {

    private final List<String> mCookies = new ArrayList<>(3);

    @TestOnly
    Cookies(List<String> cookies) {
        mCookies.addAll(cookies);
    }

    @NotNull
    public List<String> get() {
        return mCookies;
    }

    public static Cookies parse(String cookiesString) {
        Utils.checkNotNull(cookiesString, "Cookies should not be null");
        Utils.checkNotNull(cookiesString.length() > 0, "Cookies should not be empty String");
        if (cookiesString.contains(";")) {
            String[] cookies = cookiesString.split(";");
            if (cookies.length == 0) {
                return new Cookies(Collections.<String>emptyList());
            }
            return new Cookies(Arrays.asList(cookies));
        }
        return new Cookies(Collections.singletonList(cookiesString));
    }

    @Override
    public String toString() {
        Iterator<String> iterator = mCookies.iterator();
        StringBuilder result = new StringBuilder();
        while (iterator.hasNext()) {
            result.append(iterator.next());
            if (iterator.hasNext()) {
                result.append(";");
            }
        }
        return result.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cookies cookies = (Cookies) o;

        if (mCookies != null ? !mCookies.equals(cookies.mCookies) : cookies.mCookies != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return mCookies != null ? mCookies.hashCode() : 0;
    }
}
