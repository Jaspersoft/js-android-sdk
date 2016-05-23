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

package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.data.server.ServerVersion;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.info.InfoCacheManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.verification.Times;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class ProxyJobSearchTaskTest {

    @Mock
    InfoCacheManager mInfoCacheManager;
    @Mock
    JobSearchTaskFactory mSearchTaskFactory;
    @Mock
    JobSearchTask mJobSearchTask;

    private ProxyJobSearchTask taskUnderTest;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        taskUnderTest = new ProxyJobSearchTask(mInfoCacheManager, mSearchTaskFactory);
    }

    @Test
    public void should_create_and_cache_proxied_task_for_next_lookup() throws Exception {
        givenSearchTaskFactory();
        giveInfoCacheManagerWithVersion(ServerVersion.v6);

        whenRequestsLookup();
        thenShouldCreateTask();

        whenRequestsLookup();
        thenShouldReuseTask();

        thenCachedTaskShouldRequestLookup(times(2));
    }

    @Test
    public void should_always_negate_has_next_condition_if_lookuo_was_not_called() throws Exception {
        givenSearchTaskFactory();
        giveInfoCacheManagerWithVersion(ServerVersion.v6);

        whenProxyRequestHasNextFlag();

        thenShouldIgnoreSearchTask();
    }

    @Test
    public void should_delegate_has_next_call_on_cached_instance() throws Exception {
        givenSearchTaskFactory();
        giveInfoCacheManagerWithVersion(ServerVersion.v6);

        whenRequestsLookup();
        whenProxyRequestHasNextFlag();
        whenProxyRequestHasNextFlag();

        thenCachedTaskShouldRequestHasNextFlag(times(2));
    }

    private void givenSearchTaskFactory() {
        when(mSearchTaskFactory.create(any(ServerVersion.class))).thenReturn(mJobSearchTask);
    }

    private void giveInfoCacheManagerWithVersion(ServerVersion version) throws ServiceException {
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setVersion(version);
        when(mInfoCacheManager.getInfo()).thenReturn(serverInfo);
    }

    private void whenProxyRequestHasNextFlag() {
        taskUnderTest.hasNext();
    }

    private void whenRequestsLookup() throws ServiceException {
        taskUnderTest.nextLookup();
    }

    private void thenShouldCreateTask() throws ServiceException {
        verify(mInfoCacheManager).getInfo();
        verify(mSearchTaskFactory).create(ServerVersion.v6);
    }

    private void thenCachedTaskShouldRequestHasNextFlag(Times times) throws Exception {
        verify(mJobSearchTask, times).hasNext();
    }

    private void thenCachedTaskShouldRequestLookup(Times times) throws ServiceException {
        verify(mJobSearchTask, times).nextLookup();
    }

    private void thenShouldReuseTask() {
        verifyNoMoreInteractions(mSearchTaskFactory);
    }

    private void thenShouldIgnoreSearchTask() {
        verifyNoMoreInteractions(mJobSearchTask);
    }
}