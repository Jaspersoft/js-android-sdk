package com.jaspersoft.android.sdk.widget.report.renderer.event;

import com.jaspersoft.android.sdk.widget.report.renderer.Bookmark;

import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class BookmarksEvent implements Event {
    private final List<Bookmark> bookmarkList;

    public BookmarksEvent(List<Bookmark> bookmarkList) {
        this.bookmarkList = bookmarkList;
    }

    public List<Bookmark> getBookmarkList() {
        return bookmarkList;
    }
}
