package com.jaspersoft.android.sdk.widget.report.view;

import android.os.Parcel;
import android.os.Parcelable;

import com.jaspersoft.android.sdk.widget.report.renderer.Bookmark;
import com.jaspersoft.android.sdk.widget.report.renderer.ReportPart;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class ReportProperties implements Parcelable {
    private boolean isMultiPage;
    private Integer currentPage;
    private Integer pagesCount;
    private List<Bookmark> bookmarkList;
    private List<ReportPart> reportPartList;

    public ReportProperties() {
        currentPage = 1;
        bookmarkList = new ArrayList<>();
        reportPartList = new ArrayList<>();
    }

    public boolean isMultiPage() {
        return isMultiPage;
    }

    public void setMultiPage(boolean multiPage) {
        isMultiPage = multiPage;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPagesCount() {
        return pagesCount;
    }

    public void setPagesCount(Integer pagesCount) {
        this.pagesCount = pagesCount;
    }

    public List<Bookmark> getBookmarkList() {
        return bookmarkList;
    }

    public void setBookmarkList(List<Bookmark> bookmarkList) {
        this.bookmarkList = bookmarkList;
    }

    public List<ReportPart> getReportPartList() {
        return reportPartList;
    }

    public void setReportPartList(List<ReportPart> reportPartList) {
        this.reportPartList = reportPartList;
    }

    private ReportProperties(Parcel in) {
        isMultiPage = in.readByte() != 0;
        currentPage = (Integer) in.readSerializable();
        pagesCount = (Integer) in.readSerializable();
        bookmarkList = in.createTypedArrayList(Bookmark.CREATOR);
        reportPartList = in.createTypedArrayList(ReportPart.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isMultiPage ? 1 : 0));
        dest.writeSerializable(currentPage);
        dest.writeSerializable(pagesCount);
        dest.writeTypedList(bookmarkList);
        dest.writeTypedList(reportPartList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReportProperties> CREATOR = new Creator<ReportProperties>() {
        @Override
        public ReportProperties createFromParcel(Parcel in) {
            return new ReportProperties(in);
        }

        @Override
        public ReportProperties[] newArray(int size) {
            return new ReportProperties[size];
        }
    };
}
