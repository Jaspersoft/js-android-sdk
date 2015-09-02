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

package retrofit;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import retrofit.client.Response;
import retrofit.converter.ConversionException;
import retrofit.converter.Converter;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public final class RestAdapterWrapper {
    private final Converter converter;
    private final RestAdapter restAdapter;
    volatile RestAdapter.LogLevel logLevel;
    private final RestAdapter.Log log;

    private RestAdapterWrapper(RestAdapter restAdapter) {
        this.restAdapter = restAdapter;
        this.converter = restAdapter.converter;
        this.log = restAdapter.log;
        this.logLevel = restAdapter.logLevel;
    }

    public static RestAdapterWrapper wrap(RestAdapter restAdapter) {
        return new RestAdapterWrapper(restAdapter);
    }

    public RestAdapter getRestAdapter() {
        return restAdapter;
    }

    @SuppressWarnings("unchecked")
    public <Entity> ResponseEntity<Entity> produce(Response response, Class<Entity> type) {
        String url = response.getUrl();
        try {
            ExceptionCatchingTypedInput wrapped = new ExceptionCatchingTypedInput(response.getBody());
            try {
                Object convert = converter.fromBody(wrapped, type);
                Entity enity = (Entity) convert;
                return new ResponseEntity<Entity>(enity, response);
            } catch (ConversionException e) {
                // If the underlying input stream threw an exception, propagate that rather than
                // indicating that it was a conversion exception.
                if (wrapped.threwException()) {
                    throw wrapped.getThrownException();
                }

                // The response body was partially read by the converter. Replace it with null.
                response = Utils.replaceResponseBody(response, null);

                throw RetrofitError.conversionError(response.getUrl(), response, converter, type, e);
            }
        } catch (RetrofitError e) {
            throw e; // Pass through our own errors.
        } catch (IOException e) {
            if (logLevel.log()) {
                logException(e, url);
            }
            throw RetrofitError.networkError(url, e);
        } catch (Throwable t) {
            if (logLevel.log()) {
                logException(t, url);
            }
            throw RetrofitError.unexpectedError(url, t);
        }
    }

    /** Log an exception that occurred during the processing of a request or response. */
    void logException(Throwable t, String url) {
        log.log(String.format("---- ERROR %s", url != null ? url : ""));
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        log.log(sw.toString());
        log.log("---- END ERROR");
    }
}
