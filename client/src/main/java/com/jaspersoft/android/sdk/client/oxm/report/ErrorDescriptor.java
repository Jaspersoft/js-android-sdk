package com.jaspersoft.android.sdk.client.oxm.report;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.StringWriter;

/**
 * @author Tom Koptel
 * @since 1.9
 */
@Root
public class ErrorDescriptor {
    @Element
    private String errorCode;
    @Element
    private String message;
    @Element(required = false)
    private String parameters;

    public static ErrorDescriptor valueOf(HttpStatusCodeException exception) {
        String response = exception.getResponseBodyAsString();
        Serializer serializer = new Persister();
        StringWriter stringWriter = new StringWriter();
        stringWriter.append(response);
        try {
            return serializer.read(ErrorDescriptor.class, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}