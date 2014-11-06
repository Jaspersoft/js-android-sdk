package com.jaspersoft.android.sdk.client.oxm.converter;

import com.jaspersoft.android.sdk.client.oxm.report.ReportStatusResponse;

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

/**
 * @author Tom Koptel
 * @since 1.9
 */
public class ReportStatusConverter implements Converter<ReportStatusResponse> {
    @Override
    public ReportStatusResponse read(InputNode node) throws Exception {
        return new ReportStatusResponse(node.getValue());
    }

    @Override
    public void write(OutputNode node, ReportStatusResponse value) throws Exception {
        // Do nothing
    }
}
