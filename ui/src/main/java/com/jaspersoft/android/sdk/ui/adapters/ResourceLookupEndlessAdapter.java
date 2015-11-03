/*
 * Copyright (C) 2015 TIBCO Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/mobile-sdk-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile SDK for Android.
 *
 * TIBCO Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile SDK for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jaspersoft.android.sdk.client.oxm.resource.ResourceLookup;

import java.util.EnumMap;
import java.util.List;

import static com.jaspersoft.android.sdk.client.oxm.resource.ResourceLookup.ResourceType;

/**
 * @author Ivan Gadzhega
 * @since 2.0
 */
public class ResourceLookupEndlessAdapter extends ResourceLookupArrayAdapter {

    private LayoutInfo layoutInfo;
    private boolean keepOnAppending;
    private View pendingView;

    public ResourceLookupEndlessAdapter(Context context, LayoutInfo layoutInfo, List<ResourceLookup> resourceLookups) {
        super(context, layoutInfo, resourceLookups);
        this.layoutInfo = layoutInfo;
    }

    public ResourceLookupEndlessAdapter(Context context, LayoutInfo layoutInfo, EnumMap<ResourceType, Integer> drawableIdsMap,
                                        List<ResourceLookup> resourceLookups) {
        super(context, layoutInfo, drawableIdsMap, resourceLookups);
        this.layoutInfo = layoutInfo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (keepOnAppending && position == super.getCount()) {
            return getPendingView(parent);
        } else {
            return super.getView(position, convertView, parent);
        }
    }

    @Override
    public int getCount() {
        return (keepOnAppending) ? super.getCount() + 1 : super.getCount();
    }

    public int getItemViewType(int position) {
        return (position < super.getCount()) ? super.getItemViewType(position) : IGNORE_ITEM_VIEW_TYPE;
    }

    public int getViewTypeCount() {
        return super.getViewTypeCount() + 1;
    }

    @Override
    public ResourceLookup getItem(int position) {
        return (position < super.getCount()) ? super.getItem(position) : null;
    }

    //---------------------------------------------------------------------
    // Helper methods
    //---------------------------------------------------------------------

    private View getPendingView(ViewGroup parent) {
        if (pendingView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            pendingView = inflater.inflate(layoutInfo.getPendingViewResId(), parent, false);
        }
        return pendingView;
    }

    //---------------------------------------------------------------------
    // Getters & Setters
    //---------------------------------------------------------------------

    public void setKeepOnAppending(boolean keepOnAppending) {
        this.keepOnAppending = keepOnAppending;
    }

    public boolean isKeepOnAppending() {
        return keepOnAppending;
    }

    //---------------------------------------------------------------------
    // Nested Classes
    //---------------------------------------------------------------------

    public static class LayoutInfo extends ResourceLookupArrayAdapter.LayoutInfo {

        private int pendingViewResId;

        public LayoutInfo(int layoutResId) {
            super(layoutResId);
        }

        public int getPendingViewResId() {
            return pendingViewResId;
        }

        public void setPendingViewResId(int pendingViewResId) {
            this.pendingViewResId = pendingViewResId;
        }
    }

}