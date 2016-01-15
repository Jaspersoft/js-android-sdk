package com.jaspersoft.android.sdk.env;

/**
 * @author Tom Koptel
 * @since 2.3
 */
public final class ServerBundle {
    private final String token;
    private final String url;

    public ServerBundle(String token, String url) {
        this.token = token;
        this.url = url;
    }

    public String getToken() {
        return token;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return url;
    }
}
