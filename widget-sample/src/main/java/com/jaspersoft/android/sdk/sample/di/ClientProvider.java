package com.jaspersoft.android.sdk.sample.di;

import android.content.Context;

import com.jaspersoft.android.sdk.sample.cookie.CookieAuthenticationHandler;
import com.jaspersoft.android.sdk.sample.cookie.RestCookieManager;
import com.jaspersoft.android.sdk.network.AuthenticationLifecycle;
import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.Credentials;
import com.jaspersoft.android.sdk.network.Server;
import com.jaspersoft.android.sdk.network.SpringCredentials;
import com.jaspersoft.android.sdk.sample.entity.Profile;

import java.net.CookieManager;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class ClientProvider implements Provider<AuthorizedClient> {
    private final Context context;
    private final Profile profile;

    public ClientProvider(Context context, Profile profile) {
        this.context = context;
        this.profile = profile;
    }

    @Override
    public AuthorizedClient provide() {
        Server server = Server.builder()
                .withBaseUrl(profile.getUrl())
                .build();
        Credentials credentials = SpringCredentials.builder()
                .withPassword("superuser")
                .withUsername("superuser")
                .build();

        CookieManager cookieManager = new RestCookieManager.Builder(context)
                .handleWebViewCookies(true)
                .build();
        AuthenticationLifecycle lifecycle = new CookieAuthenticationHandler(cookieManager);

        return server.newClient(credentials)
                .withCookieHandler(cookieManager)
                .withAuthenticationLifecycle(lifecycle)
                .create();
    }
}
