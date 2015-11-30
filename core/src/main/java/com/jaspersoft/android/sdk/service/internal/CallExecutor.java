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

package com.jaspersoft.android.sdk.service.internal;

import com.jaspersoft.android.sdk.network.HttpException;
import com.jaspersoft.android.sdk.service.RestClient;
import com.jaspersoft.android.sdk.service.auth.Credentials;
import com.jaspersoft.android.sdk.service.exception.ServiceException;
import com.jaspersoft.android.sdk.service.token.TokenCache;
import org.jetbrains.annotations.TestOnly;

import java.io.IOException;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public class CallExecutor {
    private final TokenCache mTokenCache;
    private final TokenFactory mTokenFactory;
    private final Credentials mCredentials;

    @TestOnly
    CallExecutor(Credentials credentials, TokenCache tokenCache, TokenFactory tokenFactory) {
        mTokenCache = tokenCache;
        mTokenFactory = tokenFactory;
        mCredentials = credentials;
    }

    public static CallExecutor create(RestClient client, Credentials credentials) {
        return new CallExecutor(credentials, client.getTokenCache(), new TokenFactory(client));
    }

    // TODO: Discuss ServiceException reconsider approach on basis of Status result object
/*
    public <T extends Status> T execute(Call<T> call) {
    }
*/
    public <T> T execute(Call<T> call) throws ServiceException {
        String token = mTokenCache.get(mCredentials);
        try {
            if (token == null) {
                token = mTokenFactory.create(mCredentials);
                mTokenCache.put(mCredentials, token);
            }
            return call.perform(token);
        } catch (IOException e) {
            throw ServiceExceptionMapper.transform(e);
        } catch (HttpException e) {
            if (e.code() == 401) {
                mTokenCache.evict(mCredentials);

                try {
                    token = mTokenFactory.create(mCredentials);
                    mTokenCache.put(mCredentials, token);
                    return call.perform(token);
                } catch (IOException e1) {
                    throw ServiceExceptionMapper.transform(e1);
                } catch (HttpException e1) {
                    throw ServiceExceptionMapper.transform(e1);
                }
            } else {
                throw ServiceExceptionMapper.transform(e);
            }
        }
    }
}
