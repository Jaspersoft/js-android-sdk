package com.jaspersoft.android.sdk.widget.report.renderer;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class Bookmark implements Parcelable {
    private final String anchor;
    private final int page;
    private final ArrayList<Bookmark> bookmarks;

    public Bookmark(String anchor, int page, ArrayList<Bookmark> bookmarks) {
        this.anchor = anchor;
        this.page = page;
        this.bookmarks = bookmarks;
    }

    protected Bookmark(Parcel in) {
        anchor = in.readString();
        page = in.readInt();
        bookmarks = new ArrayList<>();
        in.readTypedList(bookmarks, Bookmark.CREATOR);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bookmark bookmark = (Bookmark) o;

        if (page != bookmark.page) return false;
        if (anchor != null ? !anchor.equals(bookmark.anchor) : bookmark.anchor != null)
            return false;
        return bookmarks != null ? bookmarks.equals(bookmark.bookmarks) : bookmark.bookmarks == null;

    }

    @Override
    public int hashCode() {
        int result = anchor != null ? anchor.hashCode() : 0;
        result = 31 * result + page;
        result = 31 * result + (bookmarks != null ? bookmarks.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(anchor);
        dest.writeInt(page);
        dest.writeTypedList(bookmarks);
    }

    public static final Parcelable.Creator<Bookmark> CREATOR = new Parcelable.Creator<Bookmark>() {
        @Override
        public Bookmark createFromParcel(Parcel in) {
            return new Bookmark(in);
        }

        @Override
        public Bookmark[] newArray(int size) {
            return new Bookmark[size];
        }
    };
}
