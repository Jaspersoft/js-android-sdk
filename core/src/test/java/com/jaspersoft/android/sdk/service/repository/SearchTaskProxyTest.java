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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class SearchTaskProxyTest {

    @Mock
    SearchTaskFactory mSearchTaskFactory;
    @Mock
    SearchTask mDelegate;

    private SearchTaskProxy searchTask;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(mSearchTaskFactory.create()).thenReturn(mDelegate);
        searchTask = new SearchTaskProxy(mSearchTaskFactory);
    }

    @Test
    public void testNextLookup() throws Exception {
        searchTask.nextLookup();
        verify(mSearchTaskFactory).create();

        searchTask.nextLookup();
        verifyNoMoreInteractions(mSearchTaskFactory);
    }

    @Test
    public void testHasNext() throws Exception {
        when(mDelegate.hasNext()).thenReturn(true);

        assertThat("Has next returns false by default", searchTask.hasNext(), is(false));
        searchTask.nextLookup();
        assertThat("Has next returns true from delegate", searchTask.hasNext(), is(true));
    }
}