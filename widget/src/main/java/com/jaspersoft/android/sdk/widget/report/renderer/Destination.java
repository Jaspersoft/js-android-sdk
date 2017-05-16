package com.jaspersoft.android.sdk.widget.report.renderer;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class Destination {
    private final Integer page;
    private final String anchor;

    Destination(Integer page, String anchor) {
        this.page = page;
        this.anchor = anchor;
    }

    public Destination(int page) {
        this(page, null);
        if (page < 1) throw new IllegalArgumentException("Destination page can not be less then 1");
    }

    public Destination(String anchor) {
        this(null, anchor);
        if (anchor == null)
            throw new IllegalArgumentException("Destination anchor can not be null");
    }

    public Integer getPage() {
        return page;
    }

    public String getAnchor() {
        return anchor;
    }

    public boolean isPageType() {
        return page != null;
    }

    public boolean isAnchorType() {
        return !isPageType();
    }
}
