package com.jaspersoft.android.sdk.widget.report.renderer;

import java.util.ArrayList;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class Bookmark {
    private final String anchor;
    private final int page;
    private final ArrayList<Bookmark> bookmarks;

    public Bookmark(String anchor, int page, ArrayList<Bookmark> bookmarks) {
        this.anchor = anchor;
        this.page = page;
        this.bookmarks = bookmarks;
    }

    public String getAnchor() {
        return anchor;
    }

    public int getPage() {
        return page;
    }

    public ArrayList<Bookmark> getBookmarks() {
        return bookmarks;
    }
}
