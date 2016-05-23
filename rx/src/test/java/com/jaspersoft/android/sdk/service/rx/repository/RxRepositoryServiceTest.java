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

package com.jaspersoft.android.sdk.service.rx.repository;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.service.data.report.ReportResource;
import com.jaspersoft.android.sdk.service.data.report.ResourceOutput;
import com.jaspersoft.android.sdk.service.data.repository.Resource;
import com.jaspersoft.android.sdk.service.data.repository.ResourceType;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.repository.RepositorySearchCriteria;
import com.jaspersoft.android.sdk.service.repository.RepositorySearchTask;
import com.jaspersoft.android.sdk.service.repository.RepositoryService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import rx.observers.TestSubscriber;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RxRepositoryServiceTest {

    private static final String REPORT_URI = "/my/uri";

    @Mock
    AuthorizedClient mAuthorizedClient;
    @Mock
    RepositoryService mSyncDelegate;
    @Mock
    RepositorySearchTask mRepositorySearchTask;
    @Mock
    ReportResource mReportResource;
    @Mock
    ServiceException mServiceException;

    @Rule
    public ExpectedException expected = none();

    private RxRepositoryService rxRepositoryService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        rxRepositoryService = new RxRepositoryService(mSyncDelegate);
    }

    @Test
    public void should_delegate_search_call() throws Exception {
        when(mSyncDelegate.search(any(RepositorySearchCriteria.class))).thenReturn(mRepositorySearchTask);
        RepositorySearchCriteria criteria = RepositorySearchCriteria.empty();

        TestSubscriber<RxRepositorySearchTask> test = new TestSubscriber<>();
        rxRepositoryService.search(criteria);

        verify(mSyncDelegate).search(criteria);
    }

    @Test
    public void should_delegate_fetch_root_folders_call() throws Exception {
        List<Resource> folders = Collections.<Resource>emptyList();
        when(mSyncDelegate.fetchRootFolders()).thenReturn(folders);

        TestSubscriber<List<Resource>> test = new TestSubscriber<>();
        rxRepositoryService.fetchRootFolders().subscribe(test);

        test.assertNoErrors();
        test.assertCompleted();
        test.assertValueCount(1);

        verify(mSyncDelegate).fetchRootFolders();
    }

    @Test
    public void root_folders_call_delegate_service_exception() throws Exception {
        when(mSyncDelegate.fetchRootFolders()).thenThrow(mServiceException);

        TestSubscriber<List<Resource>> test = new TestSubscriber<>();
        rxRepositoryService.fetchRootFolders().subscribe(test);

        test.assertError(mServiceException);
        test.assertNotCompleted();
    }

    @Test
    public void should_delegate_fetch_resource_by_details_call() throws Exception {
        when(mSyncDelegate.fetchResourceDetails(anyString(), any(ResourceType.class))).thenReturn(null);

        TestSubscriber<Resource> test = getResourceDetailsByType();

        test.assertNoErrors();
        test.assertCompleted();
        test.assertValueCount(1);
    }

    @Test
    public void resource_by_details_call_delegate_service_exception() throws Exception {
        when(mSyncDelegate.fetchResourceDetails(anyString(), any(ResourceType.class))).thenThrow(mServiceException);

        TestSubscriber<Resource> test = getResourceDetailsByType();

        test.assertError(mServiceException);
        test.assertNotCompleted();
    }

    @Test
    public void should_delegate_fetch_file_content_call() throws Exception {
        when(mSyncDelegate.fetchResourceContent(anyString())).thenReturn(null);

        TestSubscriber<ResourceOutput> test = getFileContent();

        test.assertNoErrors();
        test.assertCompleted();
        test.assertValueCount(1);
    }

    @Test
    public void fetch_file_content_call_delegate_service_exception() throws Exception {
        when(mSyncDelegate.fetchResourceContent(anyString())).thenThrow(mServiceException);

        TestSubscriber<ResourceOutput> test = getFileContent();

        test.assertError(mServiceException);
        test.assertNotCompleted();
    }

    private TestSubscriber<ResourceOutput> getFileContent() throws Exception {
        TestSubscriber<ResourceOutput> test = new TestSubscriber<>();
        rxRepositoryService.fetchResourceContent(REPORT_URI).subscribe(test);

        verify(mSyncDelegate).fetchResourceContent(REPORT_URI);

        return test;
    }

    private TestSubscriber<Resource> getResourceDetailsByType() throws Exception {
        TestSubscriber<Resource> test = new TestSubscriber<>();
        rxRepositoryService.fetchResourceDetails(REPORT_URI, ResourceType.dashboard).subscribe(test);

        verify(mSyncDelegate).fetchResourceDetails(REPORT_URI, ResourceType.dashboard);

        return test;
    }

    @Test
    public void should_accept_null_criteria() throws Exception {
        rxRepositoryService.search(null);
    }

    @Test
    public void should_provide_impl_with_factory_method() throws Exception {
        RxRepositoryService service = RxRepositoryService.newService(mAuthorizedClient);
        assertThat(service, is(instanceOf(RxRepositoryService.class)));
        assertThat(service, is(notNullValue()));
    }

    @Test
    public void should_not_accept_null_for_factory_method() throws Exception {
        expected.expectMessage("Client should not be null");
        expected.expect(NullPointerException.class);
        RxRepositoryService.newService(null);
    }


}