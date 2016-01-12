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

package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.service.data.repository.Resource;
import com.jaspersoft.android.sdk.service.data.repository.SearchResult;
import com.jaspersoft.android.sdk.service.internal.info.InfoCacheManager;
import com.jaspersoft.android.sdk.test.Chain;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class ProxyRepositoryServiceTest {
    private static final String REPORT_URI = "/my/uri";

    @Mock
    SearchUseCase mSearchUseCase;
    @Mock
    RepositoryUseCase mRepositoryUseCase;
    @Mock
    InfoCacheManager mInfoCacheManager;

    @Mock
    Resource rootFolder;
    @Mock
    Resource publicFolder;

    @Rule
    public ExpectedException expected = none();

    private ProxyRepositoryService objectUnderTest;

    @Rule
    public ExpectedException mExpectedException = none();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);



        objectUnderTest = new ProxyRepositoryService(mSearchUseCase, mRepositoryUseCase, mInfoCacheManager);
    }

    @Test
    public void shouldProvideListOfResources() {
        SearchTask searchTask = objectUnderTest.search(SearchCriteria.none());
        assertThat(searchTask, is(notNullValue()));
    }

    @Test
    public void should_accept_null_criteria() {
        SearchTask searchTask = objectUnderTest.search(null);
        assertThat(searchTask, is(notNullValue()));
    }

    @Test
    public void fetch_should_delegate_request_on_usecase() throws Exception {
        objectUnderTest.fetchReportDetails(REPORT_URI);
        verify(mRepositoryUseCase).getReportDetails(REPORT_URI);
    }

    @Test
    public void fetch_report_details_fails_with_null_uri() throws Exception {
        expected.expectMessage("Report uri should not be null");
        expected.expect(NullPointerException.class);
        objectUnderTest.fetchReportDetails(null);
    }

    @Test
    public void fetch_root_folders_performs_two_lookups() throws Exception {
        SearchResult rootFolderLookup = new SearchResult(Collections.singletonList(rootFolder), 0);
        SearchResult publicFolderLookup = new SearchResult(Collections.singletonList(publicFolder), 0);
        when(mSearchUseCase.performSearch(any(InternalCriteria.class)))
                .then(Chain.of(rootFolderLookup, publicFolderLookup));
        List<Resource> folders = objectUnderTest.fetchRootFolders();

        assertThat(folders, hasItem(rootFolder));
        assertThat(folders, hasItem(publicFolder));

        InternalCriteria rootFolder = new InternalCriteria.Builder()
                .folderUri("/")
                .resourceMask(SearchCriteria.FOLDER)
                .create();
        verify(mSearchUseCase).performSearch(rootFolder);
        InternalCriteria publicFolder = rootFolder.newBuilder()
                .folderUri("/public")
                .create();
        verify(mSearchUseCase).performSearch(publicFolder);
    }
}