package com.jaspersoft.android.sdk.widget.report.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class SimplePaginationView extends RelativeLayout implements PaginationView {
    public SimplePaginationView(Context context) {
        super(context);
        init();
    }

    public SimplePaginationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SimplePaginationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SimplePaginationView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    public void onPagesCountChanged(int totalPages) {

    }

    @Override
    public void onCurrentPageChanged(int currentPage) {

    }

    private void init() {
        LayoutInflater.from(getContext()).inflate( com.jaspersoft.android.sdk.R.layout.view_simple_pagination, this);
    }
}
