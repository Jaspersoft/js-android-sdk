/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of Jaspersoft Mobile for Android.
 *
 * Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */
package com.jaspersoft.android.sdk.service.common;

import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class CallPendingResult<Response extends Result> implements PendingResult<Response> {
    private final CallAction<Response> mCall;
    private final Scheduler mBackgroundScheduler;
    private final Scheduler mUIScheduler;

    public CallPendingResult(CallAction<Response> call) {
        mCall = call;
        mBackgroundScheduler = Schedulers.newThread();
        mUIScheduler = AndroidSchedulers.mainThread();
    }

    @Override
    public Response await() {
        return mCall.doCall();
    }

    @Override
    public void setResultCallback(final Callback<Response> resultCallback) {
        Observable.defer(
                new Func0<Observable<Response>>() {
                    @Override
                    public Observable<Response> call() {
                        return Observable.just(mCall.doCall());
                    }
                })
                .subscribeOn(mBackgroundScheduler)
                .observeOn(mUIScheduler)
                .subscribe(new Action1<Response>() {
                    @Override
                    public void call(Response response) {
                        resultCallback.onResult(response);
                    }
                });
    }
}
