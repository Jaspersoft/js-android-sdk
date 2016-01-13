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

package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.service.data.repository.Resource;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class SearchTaskProxy extends SearchTask {
    private final SearchTaskFactory mSearchTaskFactory;

    private SearchTask mDelegate;

    SearchTaskProxy(SearchTaskFactory searchTaskFactory) {
        mSearchTaskFactory = searchTaskFactory;
    }

    @NotNull
    @Override
    public List<Resource> nextLookup() throws ServiceException {
        if (mDelegate == null) {
            mDelegate = mSearchTaskFactory.create();
        }
        return mDelegate.nextLookup();
    }

    @Override
    public boolean hasNext() {
        return mDelegate != null && mDelegate.hasNext();
    }
}
