/*
 * Copyright � 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile for Android.
 *
 * TIBCO Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.sample;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jaspersoft.android.sdk.widget.report.renderer.Destination;
import com.jaspersoft.android.sdk.widget.report.view.PaginationView;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class SimplePaginationView extends PaginationView implements View.OnClickListener {

    private View pageValues;
    private ImageButton firstPage, previousPage, nextPage, lastPage;
    private TextView currentPageLabel, totalPagesLabel;
    private ProgressBar loading;

    public SimplePaginationView(Context context) {
        super(context);
    }

    public SimplePaginationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimplePaginationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SimplePaginationView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onPagesCountChanged(Integer totalPages) {
        super.onPagesCountChanged(totalPages);

        totalPagesLabel.setText(totalPages == null ? "" : "of" + getTotalPages());
        loading.setVisibility(totalPages == null ? VISIBLE : GONE);
    }

    @Override
    public void onCurrentPageChanged(int currentPage) {
        super.onCurrentPageChanged(currentPage);
        this.currentPageLabel.setText(String.valueOf(currentPage));
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        firstPage.setEnabled(getCurrentPage() != 1 && enabled);
        previousPage.setEnabled(getCurrentPage() != 1 && enabled);
        nextPage.setEnabled(!getCurrentPage().equals(getTotalPages()) && enabled);
        lastPage.setEnabled(getTotalPages() != null && !getCurrentPage().equals(getTotalPages()) && enabled);
        pageValues.setEnabled(enabled);

        firstPage.setAlpha(firstPage.isEnabled() ? 1f : 0.3f);
        previousPage.setAlpha(previousPage.isEnabled() ? 1f : 0.3f);
        nextPage.setAlpha(nextPage.isEnabled() ? 1f : 0.3f);
        lastPage.setAlpha(lastPage.isEnabled() ? 1f : 0.3f);
    }

    @Override
    protected void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_simple_pagination, this);
        currentPageLabel = (TextView) findViewById(R.id.currentPage);
        totalPagesLabel = (TextView) findViewById(R.id.totalPageLabel);
        loading = (ProgressBar) findViewById(R.id.loading);
        firstPage = (ImageButton) findViewById(R.id.firstPage);
        previousPage = (ImageButton) findViewById(R.id.previousPage);
        nextPage = (ImageButton) findViewById(R.id.nextPage);
        lastPage = (ImageButton) findViewById(R.id.lastPage);
        pageValues = findViewById(R.id.pageValues);

        firstPage.setOnClickListener(this);
        previousPage.setOnClickListener(this);
        nextPage.setOnClickListener(this);
        lastPage.setOnClickListener(this);

        super.init();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        Destination newDestination = null;
        if (i == R.id.firstPage) {
            newDestination = new Destination(1);
        } else if (i == R.id.previousPage) {
            newDestination = new Destination(getCurrentPage() - 1);
        } else if (i == R.id.nextPage) {
            newDestination = new Destination(getCurrentPage() + 1);
        } else if (i == R.id.lastPage) {
            newDestination = new Destination(getTotalPages());
        }
        if (newDestination == null) {
            throw new RuntimeException("Pagination command undefined!");
        }
        if (getPaginationListener() != null) {
            getPaginationListener().onNavigateTo(newDestination);
        }
    }
}
