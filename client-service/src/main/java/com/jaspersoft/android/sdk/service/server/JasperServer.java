package com.jaspersoft.android.sdk.service.server;

import com.jaspersoft.android.sdk.network.api.ServerRestApi;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.common.ContentResult;
import com.jaspersoft.android.sdk.service.common.PendingResult;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public interface JasperServer {
    PendingResult<ContentResult<ServerInfo>> requestInfo();

    class Factory {
        public static JasperServer create(String baseUrl) {
            ServerRestApi restApi = new ServerRestApi.Builder()
                    .baseUrl(baseUrl)
                    .build();
            return create(restApi);
        }

        public static JasperServer create(ServerRestApi restApi) {
            return new JasperServerImpl(restApi, ServerInfoTransformer.getInstance());
        }
    }
}
