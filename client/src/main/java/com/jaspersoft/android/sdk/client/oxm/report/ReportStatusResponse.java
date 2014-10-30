package com.jaspersoft.android.sdk.client.oxm.report;

import com.jaspersoft.android.sdk.client.oxm.converter.ReportStatusConverter;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;

/**
 * @author Tom Koptel
 * @since 1.9
 */
@Root(name = "status")
@Convert(ReportStatusConverter.class)
public class ReportStatusResponse {

    @Element(required = false)
    private String mStatus;

    /**
     * Otherwise, the framework cannot instantiate the class for deserialization.
     *
     * http://stackoverflow.com/questions/7470992/exception-with-simple-xml-framework-deserialization
     */
    public ReportStatusResponse() {
    }

    public ReportStatusResponse(String status) {
        mStatus = status;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        this.mStatus = status;
    }
}
