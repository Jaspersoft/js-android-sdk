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

package com.jaspersoft.android.sdk.service.rx.report;

import com.jaspersoft.android.sdk.service.data.report.ResourceOutput;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.report.ReportAttachment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;
import rx.Observable;
import rx.functions.Func0;


/**
 * Public API that wraps concrete attachment requested by user.
 * All responses wrapped as Rx {@link rx.Observable}.
 *
 * @author Tom Koptel
 * @since 2.3
 */
public class RxReportAttachment {
    private final ReportAttachment mSyncDelegate;

    @TestOnly
    RxReportAttachment(ReportAttachment attachment) {
        mSyncDelegate = attachment;
    }

    /**
     * File name of attachment associated to file on JRS side
     *
     * @return file name with extension
     */
    @NotNull
    public String getFileName() {
        return mSyncDelegate.getFileName();
    }

    /**
     * Performs network operation that returns output of attachment
     *
     * @return output of export that wraps {@link java.io.InputStream}
     */
    @NotNull
    public Observable<ResourceOutput> download() {
        return Observable.defer(new Func0<Observable<ResourceOutput>>() {
            @Override
            public Observable<ResourceOutput> call() {
                try {
                    ResourceOutput content = mSyncDelegate.download();
                    return Observable.just(content);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
            }
        });
    }
}
