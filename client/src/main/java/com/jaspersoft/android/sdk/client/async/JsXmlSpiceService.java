/*
 * Copyright (C) 2012-2013 Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/mobile-sdk-android
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of Jaspersoft Mobile SDK for Android.
 *
 * Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Jaspersoft Mobile SDK for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.client.async;

import android.app.Application;

import com.octo.android.robospice.SpiceService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.springandroid.xml.SimpleSerializerObjectPersisterFactory;
import com.octo.android.robospice.persistence.string.InFileStringObjectPersister;

/**
 * This class offers a {@link SpiceService} dedicated to xml web services. Provides
 * caching.
 *
 * @author Ivan Gadzhega
 * @since 1.6
 */
public class JsXmlSpiceService extends SpiceService {

    @Override
    public CacheManager createCacheManager(Application application) throws CacheCreationException {
        CacheManager cacheManager = new CacheManager();
        // It is really important to keep proper persister order.
        cacheManager.addPersister(new InFileStringObjectPersister(application));
        cacheManager.addPersister(new SimpleSerializerObjectPersisterFactory(application));
        return cacheManager;
    }

}
