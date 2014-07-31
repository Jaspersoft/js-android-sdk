/*
 * Copyright (C) 2012-2014 Jaspersoft Corporation.
 * Portions (C) 2008-2009 CommonsWare, LLC
 * Portions (C) 2009 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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