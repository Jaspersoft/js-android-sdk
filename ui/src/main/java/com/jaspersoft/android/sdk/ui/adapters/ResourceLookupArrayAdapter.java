/*
 * Copyright (C) 2012-2014 Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/mobile-sdk-android
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of Jaspersoft Mobile SDK for Android.
 *
 * Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Jaspersoft Mobile SDK for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.jaspersoft.android.sdk.client.oxm.resource.ResourceLookup;
import com.jaspersoft.android.sdk.ui.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;

import static com.jaspersoft.android.sdk.client.oxm.resource.ResourceLookup.ResourceType;

/**
 * A concrete ArrayAdapter that is backed by an array of ResourceLookup objects.
 *
 * @author Ivan Gadzhega
 * @since 1.7
 */
public class ResourceLookupArrayAdapter extends ArrayAdapter<ResourceLookup>{

    private LayoutInfo layoutInfo;
    private EnumMap<ResourceType, Integer> drawableIdsMap;
    private String datetimeFormatPattern = "yyyy-MM-dd HH:mm:ss";

    public ResourceLookupArrayAdapter(Context context, LayoutInfo layoutInfo, List<ResourceLookup> resourceLookups) {
        this(context, layoutInfo, null, resourceLookups);
    }

    public ResourceLookupArrayAdapter(Context context, LayoutInfo layoutInfo,
                                      EnumMap<ResourceType,Integer> drawableIdsMap,
                                      List<ResourceLookup> resourceLookups) {
        super(context, layoutInfo.layoutResId, resourceLookups);
        this.layoutInfo = layoutInfo;
        this.drawableIdsMap = drawableIdsMap;
    }

    @Deprecated
    public ResourceLookupArrayAdapter(Context context, List<ResourceLookup> resourceLookups) {
        this(context, new LayoutInfo(R.layout.resource_list_item),
                new EnumMap<ResourceType, Integer>(ResourceType.class), resourceLookups);

        layoutInfo.setIconViewId(R.id.resource_list_item_icon);
        layoutInfo.setLabelViewId(R.id.resource_list_item_label);
        layoutInfo.setUriViewId(R.id.resource_list_item_uri);

        drawableIdsMap.put(ResourceType.folder, R.drawable.ic_type_folder);
        drawableIdsMap.put(ResourceType.reportUnit, R.drawable.ic_type_report);
        drawableIdsMap.put(ResourceType.dashboard, R.drawable.ic_type_dashboard);
        drawableIdsMap.put(ResourceType.unknown, R.drawable.ic_type_unknown);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ResourceLookup resourceLookup = getItem(position);
        // reuse views
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(layoutInfo.layoutResId, parent, false);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.iconView = (ImageView) rowView.findViewById(layoutInfo.iconViewId);
            viewHolder.labelView = (TextView) rowView.findViewById(layoutInfo.labelViewId);
            viewHolder.descriptionView = (TextView) rowView.findViewById(layoutInfo.descriptionViewId);
            viewHolder.dateView = (TextView) rowView.findViewById(layoutInfo.dateViewId);
            viewHolder.uriView = (TextView) rowView.findViewById(layoutInfo.uriViewId);
            rowView.setTag(viewHolder);
        }
        // fill data
        ViewHolder viewHolder = (ViewHolder) rowView.getTag();
        if (viewHolder.iconView != null && drawableIdsMap != null) {
            Drawable icon = getIconByType(resourceLookup.getResourceType());
            viewHolder.iconView.setImageDrawable(icon);
        }
        if (viewHolder.labelView != null) {
            String label = resourceLookup.getLabel();
            viewHolder.labelView.setText(label);
        }
        if (viewHolder.descriptionView != null) {
            String description = resourceLookup.getDescription();
            viewHolder.descriptionView.setText(description);
        }
        if (viewHolder.dateView != null) {
            String date = formatDateString(resourceLookup.getUpdateDate());
            viewHolder.dateView.setText(date);
        }
        if (viewHolder.uriView != null) {
            String uri = resourceLookup.getUri();
            viewHolder.uriView.setText(uri);
        }
        // return configured view
        return rowView;
    }

    public void setDatetimeFormatPattern(String datetimeFormatPattern) {
        this.datetimeFormatPattern = datetimeFormatPattern;
    }

    //---------------------------------------------------------------------
    // Helper methods
    //---------------------------------------------------------------------

    private Drawable getIconByType(ResourceType type) {
        type = (drawableIdsMap.containsKey(type)) ? type : ResourceType.unknown;
        int drawableResId = drawableIdsMap.get(type);
        return getContext().getResources().getDrawable(drawableResId);
    }

    private String formatDateString(String updateDate) {
        updateDate = (updateDate == null) ? "" : updateDate;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datetimeFormatPattern);
            Date dateValue = simpleDateFormat.parse(updateDate);
            DateFormat dateFormat = DateFormat.getDateInstance();
            return dateFormat.format(dateValue);
        } catch (ParseException ex) {
            return updateDate;
        }
    }

    //---------------------------------------------------------------------
    // Nested Classes
    //---------------------------------------------------------------------

    public static class LayoutInfo {
        private int layoutResId;
        private int iconViewId;
        private int labelViewId;
        private int descriptionViewId;
        private int dateViewId;
        private int uriViewId;

        public LayoutInfo(int layoutResId) {
            this.layoutResId = layoutResId;
        }

        public int getLayoutResId() {
            return layoutResId;
        }

        public int getIconViewId() {
            return iconViewId;
        }

        public void setIconViewId(int iconViewId) {
            this.iconViewId = iconViewId;
        }

        public int getLabelViewId() {
            return labelViewId;
        }

        public void setLabelViewId(int labelViewId) {
            this.labelViewId = labelViewId;
        }

        public int getDescriptionViewId() {
            return descriptionViewId;
        }

        public void setDescriptionViewId(int descriptionViewId) {
            this.descriptionViewId = descriptionViewId;
        }

        public int getDateViewId() {
            return dateViewId;
        }

        public void setDateViewId(int dateViewId) {
            this.dateViewId = dateViewId;
        }

        public int getUriViewId() {
            return uriViewId;
        }

        public void setUriViewId(int uriViewId) {
            this.uriViewId = uriViewId;
        }
    }

    private static class ViewHolder {
        private ImageView iconView;
        private TextView labelView;
        private TextView descriptionView;
        private TextView dateView;
        private TextView uriView;
    }

}