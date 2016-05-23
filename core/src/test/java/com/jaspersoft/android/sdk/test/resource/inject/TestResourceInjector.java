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

package com.jaspersoft.android.sdk.test.resource.inject;

import com.jaspersoft.android.sdk.test.resource.ResourceFile;
import com.jaspersoft.android.sdk.test.resource.TestResource;

import java.lang.reflect.Field;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public final class TestResourceInjector {
    private final Object mTarget;

    TestResourceInjector(Object target) {
        mTarget = target;
    }

    public static void inject(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("Target object should not be null");
        }
        TestResourceInjector injector = new TestResourceInjector(object);
        injector.inject();
    }

    void inject() {
        Class<?> clazz = mTarget.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(ResourceFile.class)) {
                ResourceFile annotation = field.getAnnotation(ResourceFile.class);
                String path = annotation.value();
                TestResource resource = TestResource.create(path);
                try {
                    field.setAccessible(true);
                    field.set(mTarget, resource);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
