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
import com.jaspersoft.android.sdk.service.data.report.ResourceOutput;
import com.jaspersoft.android.sdk.service.data.repository.Resource;
import com.jaspersoft.android.sdk.service.data.repository.ResourceType;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.internal.Preconditions;
import com.jaspersoft.android.sdk.service.repository.RepositorySearchCriteria;
import com.jaspersoft.android.sdk.service.repository.RepositorySearchTask;
import com.jaspersoft.android.sdk.service.repository.RepositoryService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
import rx.Observable;
import rx.functions.Func0;

import java.util.List;


/**
 * Public API that allows to perform search request, list details and download content of resources, list root folders.
 * All responses wrapped as Rx {@link rx.Observable}.
 *
 * <pre>
 * {@code
 *
 * Server server = Server.builder()
 *         .withBaseUrl("http://mobiledemo2.jaspersoft.com/jasperserver-pro/")
 *         .build();
 *
 * Credentials credentials = SpringCredentials.builder()
 *         .withPassword("phoneuser")
 *         .withUsername("phoneuser")
 *         .withOrganization("organization_1")
 *         .build();
 *
 * AuthorizedClient client = server.newClient(credentials).create();
 * RxRepositoryService service = RxRepositoryService.newService(client);
 *
 * Action1<Throwable> errorHandler = new Action1<Throwable>() {
 *     &#064;
 *     public void call(Throwable throwable) {
 *         // handle error
 *     }
 * };
 *
 * String resourceUri = "/my/report/uri";
 *
 * service.fetchResourceContent(resourceUri).subscribe(new Action1<ResourceOutput>() {
 *     &#064;
 *     public void call(ResourceOutput resourceOutput) {
 *         // success
 *     }
 * }, errorHandler);
 *
 * RepositorySearchCriteria criteria = RepositorySearchCriteria.builder()
 *         .withQuery("reports")
 *         .withLimit(100)
 *         .withOffset(0)
 *         .build();
 * RxRepositorySearchTask search = service.search(criteria);
 * while (search.hasNext()) {
 *     // returns by 100 items until end reached
 *     search.nextLookup().subscribe(new Action1<List<Resource>>() {
 *         &#064;
 *         public void call(List<Resource> resources) {
 *             // success
 *         }
 *     }, errorHandler);
 * }
 *
 * service.fetchResourceDetails(resourceUri, ResourceType.file).subscribe(new Action1<Resource>() {
 *     &#064;
 *     public void call(Resource resource) {
 *         // success
 *     }
 * }, errorHandler);
 *
 * service.fetchRootFolders().subscribe(new Action1<List<Resource>>() {
 *     &#064;
 *     public void call(List<Resource> resources) {
 *         // success
 *     }
 * }, errorHandler);
 *
 * }
 * </pre>
 *
 * @author Tom Koptel
 * @since 2.3
 */
public class RxRepositoryService {
    private final RepositoryService mSyncDelegate;

    @TestOnly
    RxRepositoryService(RepositoryService repositoryService) {
        mSyncDelegate = repositoryService;
    }

    /**
     * Performs search inside JRS repository
     *
     * @param criteria search options to control our search response
     * @return task that wraps in iterator format bundle of search response
     */
    @NotNull
    public RxRepositorySearchTask search(@Nullable RepositorySearchCriteria criteria) {
        RepositorySearchTask repositorySearchTask = mSyncDelegate.search(criteria);
        return new RxRepositorySearchTask(repositorySearchTask);
    }

    /**
     * Retrieves details of resources on the basis of supplied format
     *
     * @param resourceUri unique resource uri
     * @param type        concrete type of resource. E.g. reportUnit, file
     * @return subclass of {@link Resource} object
     */
    @NotNull
    public Observable<Resource> fetchResourceDetails(@NotNull final String resourceUri, final @NotNull ResourceType type) {
        Preconditions.checkNotNull(resourceUri, "Resource uri should not be null");
        Preconditions.checkNotNull(type, "Resource type should not be null");

        return Observable.defer(new Func0<Observable<Resource>>() {
            @Override
            public Observable<Resource> call() {
                try {
                    Resource resource = mSyncDelegate.fetchResourceDetails(resourceUri, type);
                    return Observable.just(resource);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    /**
     * Performs download operation on concrete resource
     *
     * @param resourceUri unique resource uri
     * @return output of resource that wraps {@link java.io.InputStream}
     */
    @NotNull
    public Observable<ResourceOutput> fetchResourceContent(@NotNull final String resourceUri) {
        Preconditions.checkNotNull(resourceUri, "Resource uri should not be null");

        return Observable.defer(new Func0<Observable<ResourceOutput>>() {
            @Override
            public Observable<ResourceOutput> call() {
                try {
                    ResourceOutput output = mSyncDelegate.fetchResourceContent(resourceUri);
                    return Observable.just(output);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    /**
     * Returns predefined list of root repositories
     *
     * @return list of root repositories
     */
    @NotNull
    public Observable<List<Resource>> fetchRootFolders() {
        return Observable.defer(new Func0<Observable<List<Resource>>>() {
            @Override
            public Observable<List<Resource>> call() {
                try {
                    List<Resource> resources = mSyncDelegate.fetchRootFolders();
                    return Observable.just(resources);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    /**
     * Factory method to create new service
     *
     * @param client authorized network client
     * @return instance of newly created service
     */
    @NotNull
    public static RxRepositoryService newService(@NotNull AuthorizedClient client) {
        Preconditions.checkNotNull(client, "Client should not be null");
        RepositoryService repositoryService = RepositoryService.newService(client);
        return new RxRepositoryService(repositoryService);
    }

    /**
     * Provides synchronous counterpart of service
     *
     * @return wrapped version of service {@link RepositoryService}
     */
    public RepositoryService toBlocking() {
        return mSyncDelegate;
    }
}
