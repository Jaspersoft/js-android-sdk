/*
 * Copyright (C) 2012-2013 Jaspersoft Corporation. All rights reserved.
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

import java.util.List;

/**
 * A concrete ArrayAdapter that is backed by an array of ResourceLookup objects.
 *
 * @author Ivan Gadzhega
 * @since 1.7
 */
public class ResourceLookupArrayAdapter extends ArrayAdapter<ResourceLookup>{
    private final Context context;
    private final List<ResourceLookup> resourceLookups;

    public ResourceLookupArrayAdapter(Context context, List<ResourceLookup> resourceLookups) {
        super(context, R.layout.resource_list_item, resourceLookups);
        this.context = context;
        this.resourceLookups = resourceLookups;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ResourceLookup resourceLookup = resourceLookups.get(position);

        Drawable drawable;
        switch (resourceLookup.getResourceType()) {
            case folder:
                drawable = context.getResources().getDrawable(R.drawable.ic_type_folder);
                break;
            case reportUnit:
                drawable = context.getResources().getDrawable(R.drawable.ic_type_report);
                break;
            case dashboard:
                drawable = context.getResources().getDrawable(R.drawable.ic_type_dashboard);
                break;
            default:
                // for an unknown resource
                drawable = context.getResources().getDrawable(R.drawable.ic_type_unknown);
                break;
        }

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.resource_list_item, parent, false);

        ImageView image = (ImageView) rowView.findViewById(R.id.resource_list_item_icon);
        TextView label = (TextView) rowView.findViewById(R.id.resource_list_item_label);
        TextView uri = (TextView) rowView.findViewById(R.id.resource_list_item_uri);

        image.setImageDrawable(drawable);
        label.setText(resourceLookup.getLabel());
        uri.setText(resourceLookup.getUri());

        return rowView;
    }
}
