package com.jaspersoft.android.sdk.service.dashboard;

import com.jaspersoft.android.sdk.network.entity.export.OutputResource;
import com.jaspersoft.android.sdk.service.data.report.ResourceOutput;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
class DashboardExportMapper {
    public ResourceOutput transform(final OutputResource outputResource) {
        return new ResourceOutput() {
            @Override
            public InputStream getStream() throws IOException {
                return outputResource.getStream();
            }
        };
    }
}
