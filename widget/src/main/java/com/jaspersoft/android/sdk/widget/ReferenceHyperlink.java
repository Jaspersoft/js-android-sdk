package com.jaspersoft.android.sdk.widget;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class ReferenceHyperlink extends Hyperlink {
    private final String source;

    ReferenceHyperlink(String source) {
        super(Type.REFERENCE);
        this.source = source;
    }

    public String getSource() {
        return source;
    }
}
