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
import com.jaspersoft.android.sdk.service.internal.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;
import rx.Observable;
import rx.functions.Func0;

/**
 * The corresponding service allows to request server info by wrapping one in Rx {@link Observable}
 *
 * <pre>
 * {@code
 *
 *  Server server = Server.builder()
 *          .withBaseUrl("http://mobiledemo2.jaspersoft.com/jasperserver-pro/")
 *          .build();
 *
 *  AnonymousClient anonymousClient = server.newClient().create();
 *
 *  Credentials credentials = SpringCredentials.builder()
 *          .withPassword("phoneuser")
 *          .withUsername("phoneuser")
 *          .withOrganization("organization_1")
 *          .build();
 *
 *  RxServerInfoService service = RxServerInfoService.newService(anonymousClient);
 *  service.requestServerInfo().subscribe(
 *          new Action1<Credentials>() {
 *              &#064;
 *              public void call(ServerInfo info) {
 *                  // success
 *              }
 *          },
 *          new Action1<Throwable>() {
 *              &#064;
 *              public void call(Throwable throwable) {
 *                  // handle API exception
 *              }
 *          }
 *  );
 *
 * }
 * </pre>
 *
 * @author Tom Koptel
 * @since 2.3
 */
public class RxServerInfoService {
    private final ServerInfoService mSyncDelegate;

    @TestOnly
    RxServerInfoService(ServerInfoService infoService) {
        mSyncDelegate = infoService;
    }

    /**
     * Performs request that returns serve info
     *
     * @return observable serve metadata that wraps latest serve info data
     */
    @NotNull
    public Observable<ServerInfo> requestServerInfo() {
        return Observable.defer(new Func0<Observable<ServerInfo>>() {
            @Override
            public Observable<ServerInfo> call() {
                try {
                    ServerInfo serverInfo = mSyncDelegate.requestServerInfo();
                    return Observable.just(serverInfo);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
            }
        });
    }

    /**
     * Factory method to create new service
     *
     * @param anonymousClient anonymous network client
     * @return instance of newly created service
     */
    @NotNull
    public static RxServerInfoService newService(@NotNull AnonymousClient anonymousClient) {
        Preconditions.checkNotNull(anonymousClient, "Client should not be null");
        ServerInfoService service = ServerInfoService.newService(anonymousClient);
        return new RxServerInfoService(service);
    }
}
