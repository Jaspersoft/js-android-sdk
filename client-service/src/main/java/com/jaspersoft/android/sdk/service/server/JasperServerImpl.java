package com.jaspersoft.android.sdk.service.server;

import com.jaspersoft.android.sdk.network.api.RestError;
import com.jaspersoft.android.sdk.network.api.ServerRestApi;
import com.jaspersoft.android.sdk.network.entity.server.ServerInfoResponse;
import com.jaspersoft.android.sdk.service.data.server.ServerInfo;
import com.jaspersoft.android.sdk.service.common.BaseContentResult;
import com.jaspersoft.android.sdk.service.common.CallAction;
import com.jaspersoft.android.sdk.service.common.CallPendingResult;
import com.jaspersoft.android.sdk.service.common.ContentResult;
import com.jaspersoft.android.sdk.service.common.PendingResult;

/**
 * @author Tom Koptel
 * @since 2.0
 */
final class JasperServerImpl implements JasperServer {
    private final CallAction<ContentResult<ServerInfo>> mInfoCall;

    public JasperServerImpl(ServerRestApi restApi, ServerInfoTransformer transformer) {
        mInfoCall = new RequestInfoCall(restApi, transformer);
    }

    @Override
    public PendingResult<ContentResult<ServerInfo>> requestInfo() {
        return new CallPendingResult<ContentResult<ServerInfo>>(mInfoCall);
    }

    private static class RequestInfoCall implements CallAction<ContentResult<ServerInfo>> {
        private final ServerRestApi mRestApi;
        private final ServerInfoTransformer mTransformer;

        private RequestInfoCall(ServerRestApi restApi, ServerInfoTransformer transformer) {
            mRestApi = restApi;
            mTransformer = transformer;
        }

        @Override
        public ContentResult<ServerInfo> doCall() {
            try {
                ServerInfoResponse response = mRestApi.requestServerInfo();
                ServerInfo serverInfo = mTransformer.transform(response);
                return new BaseContentResult<ServerInfo>(serverInfo);
            } catch (RestError er) {
                return new BaseContentResult<ServerInfo>(er);
            }
        }
    }
}
