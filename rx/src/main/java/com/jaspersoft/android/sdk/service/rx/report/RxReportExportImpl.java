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

import com.jaspersoft.android.sdk.service.data.report.ReportExportOutput;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.report.ReportAttachment;
import com.jaspersoft.android.sdk.service.report.ReportExport;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;
import rx.Observable;
import rx.functions.Func0;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class RxReportExportImpl implements RxReportExport {
    private final ReportExport mSyncDelegate;

    @TestOnly
    RxReportExportImpl(ReportExport export) {
        mSyncDelegate = export;
    }

    @NotNull
    @Override
    public Observable<List<RxReportAttachment>> getAttachments() {
        return Observable.defer(new Func0<Observable<List<RxReportAttachment>>>() {
            @Override
            public Observable<List<RxReportAttachment>> call() {
                List<ReportAttachment> attachments = mSyncDelegate.getAttachments();
                int size = attachments.size();
                List<RxReportAttachment> rxAttachments = new ArrayList<>(size);
                for (int i = 0; i < size; i++) {
                    ReportAttachment attachment = attachments.get(i);
                    RxReportAttachmentImpl rxAttachment = new RxReportAttachmentImpl(attachment);
                    rxAttachments.add(rxAttachment);
                }
                return Observable.just(rxAttachments);
            }
        });
    }

    @NotNull
    @Override
    public Observable<ReportExportOutput> download() {
        return Observable.defer(new Func0<Observable<ReportExportOutput>>() {
            @Override
            public Observable<ReportExportOutput> call() {
                try {
                    ReportExportOutput content = mSyncDelegate.download();
                    return Observable.just(content);
                } catch (ServiceException e) {
                    return Observable.error(e);
                }
            }
        });
    }
}
