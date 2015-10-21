/*
 * Copyright (C) 2015 TIBCO Jaspersoft Corporation. All rights reserved.
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

package com.jaspersoft.android.sdk.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Tom Koptel
 * @since 1.9
 */
public class StaticCacheHelper {

    private static final long TIME_TO_LIVE = TimeUnit.MINUTES.toMillis(20);

    private static final Map<String, Element> cacheMap = new HashMap<String, Element>();

    /**
     * Retrieves an item from the cache. If found, the method compares
     * the object's expiration date to the current time and only returns
     * the object if the expiration date has not passed.
     *
     * @param cacheKey
     * @return
     */
    public static Object retrieveObjectFromCache(String cacheKey) {
        Element e = cacheMap.get(cacheKey);
        Object o = null;
        if (e != null) {
            Date now = new Date();
            if (e.getExpirationDate().after(now)) {
                o = e.getObject();
            } else {
                removeCacheItem(cacheKey);
            }
        }
        return o;
    }

    /**
     * Stores an object in the cache, wrapped by an Element object.
     * The Element object has an expiration date, which will be set to
     * now + this class' TIME_TO_LIVE setting.
     *
     * @param cacheKey
     * @param object
     */
    public static void storeObjectInCache(String cacheKey, Object object) {
        Date expirationDate = new Date(System.currentTimeMillis() + TIME_TO_LIVE);
        Element e = new Element(object, expirationDate);
        cacheMap.put(cacheKey, e);
    }

    /**
     * Stores an object in the cache, wrapped by an Element object.
     * The Element object has an expiration date, which will be set to
     * now + the timeToLiveInMilliseconds value that is passed into the method.
     *
     * @param cacheKey
     * @param object
     * @param timeToLiveInMilliseconds
     */
    public static void storeObjectInCache(String cacheKey, Object object, int timeToLiveInMilliseconds) {
        Date expirationDate = new Date(System.currentTimeMillis() + timeToLiveInMilliseconds);
        Element e = new Element(object, expirationDate);
        cacheMap.put(cacheKey, e);
    }

    public static void removeCacheItem(String cacheKey) {
        cacheMap.remove(cacheKey);
    }

    public static void clearCache() {
        cacheMap.clear();
    }

    static class Element {

        private Object object;
        private Date expirationDate;

        /**
         * @param object
         * @param key
         * @param expirationDate
         */
        private Element(Object object, Date expirationDate) {
            super();
            this.object = object;
            this.expirationDate = expirationDate;
        }
        /**
         * @return the object
         */
        public Object getObject() {
            return object;
        }
        /**
         * @param object the object to set
         */
        public void setObject(Object object) {
            this.object = object;
        }
        /**
         * @return the expirationDate
         */
        public Date getExpirationDate() {
            return expirationDate;
        }
        /**
         * @param expirationDate the expirationDate to set
         */
        public void setExpirationDate(Date expirationDate) {
            this.expirationDate = expirationDate;
        }
    }
}