package com.jaspersoft.android.sdk.util;

import android.util.Base64;
import android.util.Log;

import com.jaspersoft.android.sdk.client.JsServerProfile;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.List;

/**
 * @author Tom Koptel
 * @since 1.9
 */
public class CookieHttpRequestInterceptor implements ClientHttpRequestInterceptor {
    private static final String SET_COOKIE = "set-cookie";
    private static final String COOKIE = "cookie";
    private static final String COOKIE_STORE = "cookieStore";
    private final JsServerProfile jsServerProfile;

    public CookieHttpRequestInterceptor(JsServerProfile jsServerProfile) {
        this.jsServerProfile = jsServerProfile;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] byteArray,
                                        ClientHttpRequestExecution execution) throws IOException {

        Log.d(getClass().getSimpleName(), ">>> entering intercept");
        List<String> cookies = request.getHeaders().get(COOKIE);
        // if the header doesn't exist, add any existing, saved cookies
        if (cookies == null) {
            List<String> cookieStore = (List<String>) StaticCacheHelper.retrieveObjectFromCache(COOKIE_STORE);
            // if we have stored cookies, add them to the headers
            if (cookieStore != null) {
                for (String cookie : cookieStore) {
                    request.getHeaders().add(COOKIE, cookie);
                }
            } else {
                Log.d(getClass().getSimpleName(), "Setting basic auth");
                // Basic Authentication
                String authorisation = jsServerProfile.getUsernameWithOrgId() + ":" + jsServerProfile.getPassword();
                byte[] encodedAuthorisation = Base64.encode(authorisation.getBytes(), Base64.NO_WRAP);
                request.getHeaders().set("Authorization", "Basic " + new String(encodedAuthorisation));
                // disable buggy keep-alive
                request.getHeaders().set("Connection", "close");
            }
        }

        // execute the request
        ClientHttpResponse response = execution.execute(request, byteArray);
        // pull any cookies off and store them
        cookies = response.getHeaders().get(SET_COOKIE);
        if (cookies != null) {
            for (String cookie : cookies) {
                Log.d(getClass().getSimpleName(), ">>> response cookie = " + cookie);
            }
            StaticCacheHelper.storeObjectInCache(COOKIE_STORE, cookies);
        }
        Log.d(getClass().getSimpleName(), ">>> leaving intercept");
        return response;
    }
}
