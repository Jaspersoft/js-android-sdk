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

package com.jaspersoft.android.sdk.service.rx.info;

import com.jaspersoft.android.sdk.network.AnonymousClient;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.info.ServerInfoService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import rx.observers.TestSubscriber;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RxServerInfoServiceTest {

    @Mock
    ServerInfoService mSyncDelegate;

    @Mock
    ServerInfo mServerInfo;
    @Mock
    ServiceException mServiceException;

    @Mock
    AnonymousClient mAnonymousClient;

    @Rule
    public ExpectedException expected = none();

    private RxServerInfoServiceImpl rxServerInfoService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        rxServerInfoService = new RxServerInfoServiceImpl(mSyncDelegate);
    }

    @Test
    public void should_delegate_info_request_call() throws Exception {
        when(mSyncDelegate.requestServerInfo()).thenReturn(mServerInfo);

        TestSubscriber<ServerInfo> test = new TestSubscriber<>();
        rxServerInfoService.requestServerInfo().subscribe(test);

        test.assertNoErrors();
        test.assertCompleted();
        test.assertValueCount(1);

        verify(mSyncDelegate).requestServerInfo();
    }

    @Test
    public void should_delegate_service_exception_on_info_request_call() throws Exception {
        when(mSyncDelegate.requestServerInfo()).thenThrow(mServiceException);

        TestSubscriber<ServerInfo> test = new TestSubscriber<>();
        rxServerInfoService.requestServerInfo().subscribe(test);

        test.assertError(mServiceException);
        test.assertNotCompleted();

        verify(mSyncDelegate).requestServerInfo();
    }

    @Test
    public void should_provide_impl_with_factory_method() throws Exception {
        RxServerInfoService service = RxServerInfoService.newService(mAnonymousClient);
        assertThat(service, is(instanceOf(RxServerInfoServiceImpl.class)));
        assertThat(service, is(notNullValue()));
    }

    @Test
    public void should_not_accept_null_for_factory_method() throws Exception {
        expected.expectMessage("Client should not be null");
        expected.expect(NullPointerException.class);
        RxServerInfoService.newService(null);
    }
}