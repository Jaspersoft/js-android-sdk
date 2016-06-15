package com.jaspersoft.android.sdk.widget.internal;

import com.jaspersoft.android.sdk.network.AuthorizedClient;
import com.jaspersoft.android.sdk.network.SpringCredentials;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class SetupOptions {
    private final Server server;
    private final Auth auth;

    private SetupOptions(Server server, Auth auth) {
        this.server = server;
        this.auth = auth;
    }

    public static SetupOptions create(AuthorizedClient client, double version) {
        String baseUrl = client.getBaseUrl();
        // TODO generify credentials adaptation
        SpringCredentials credentials = (SpringCredentials) client.getCredentials();

        Auth auth = new Auth(
                credentials.getUsername(),
                credentials.getPassword(),
                credentials.getOrganization()
        );
        Server server = new Server(baseUrl, version);

        return new SetupOptions(server, auth);
    }

    private static class Server {
        private final String url;
        private final double version;

        private Server(String url, double version) {
            this.url = url;
            this.version = version;
        }
    }

    private static class Auth {
        private final String name;
        private final String password;
        private final String organization;

        private Auth(String name, String password, String organization) {
            this.name = name;
            this.password = password;
            this.organization = organization;
        }
    }
}
