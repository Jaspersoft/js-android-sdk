package com.jaspersoft.android.sdk.widget.report.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.jaspersoft.android.sdk.widget.report.renderer.Destination;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public abstract class PaginationView extends RelativeLayout {
    private Integer currentPage, totalPages;

    private PaginationListener paginationListener;

    public PaginationView(Context context) {
        super(context);
        init();
    }

    public PaginationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PaginationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PaginationView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void onPagesCountChanged(Integer totalPages) {
        this.totalPages = totalPages;

        setVisibility(totalPages == null || totalPages > 1 ? VISIBLE : GONE);
        setEnabled(isEnabled());
    }

    public void onCurrentPageChanged(int currentPage) {
        this.currentPage = currentPage;
        setEnabled(isEnabled());
    }

    public void setPaginationListener(PaginationListener paginationListener) {
        this.paginationListener = paginationListener;
    }

    protected Integer getCurrentPage() {
        return currentPage;
    }

    protected Integer getTotalPages() {
        return totalPages;
    }

    protected PaginationListener getPaginationListener() {
        return paginationListener;
    }

    protected void init() {
        currentPage = 1;
        onCurrentPageChanged(currentPage);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.currentPage = this.currentPage;
        ss.totalPages = this.totalPages;
        ss.enabled = isEnabled();

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        int currentPage = ss.currentPage;
        Integer totalPages = ss.totalPages;

        setEnabled(ss.enabled);
        onCurrentPageChanged(currentPage);
        onPagesCountChanged(totalPages);
    }

    public interface PaginationListener {
        void onNavigateTo(Destination destination);
    }

    private static class SavedState extends BaseSavedState {
        private Integer currentPage, totalPages;
        private boolean enabled;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.currentPage = in.readInt();
            this.totalPages = in.readInt();
            this.enabled = in.readInt() == 1;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.currentPage);
            out.writeInt(this.totalPages);
            out.writeInt(enabled ? 1 : 0);
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}
