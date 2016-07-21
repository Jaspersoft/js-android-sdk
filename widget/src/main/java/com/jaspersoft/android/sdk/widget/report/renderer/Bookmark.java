package com.jaspersoft.android.sdk.widget.report.renderer;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class Bookmark implements Parcelable{
    private final String anchor;
    private final int page;
    private final ArrayList<Bookmark> bookmarks;

    public Bookmark(String anchor, int page, ArrayList<Bookmark> bookmarks) {
        this.anchor = anchor;
        this.page = page;
        this.bookmarks = bookmarks;
    }

    private Bookmark(Parcel in) {
        anchor = in.readString();
        page = in.readInt();
        bookmarks = in.readArrayList(Bookmark.class.getClassLoader());
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
