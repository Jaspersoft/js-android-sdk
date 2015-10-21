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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.jaspersoft.android.sdk.client.oxm.ResourceProperty;

import java.util.List;

/**
 * A concrete ArrayAdapter that is backed by an array of ResourceProperty objects.
 *
 * @author Ivan Gadzhega
 * @since 1.0
 */
public class ResourcePropertyArrayAdapter extends ArrayAdapter<ResourceProperty> {

    private int mFieldId = 0;
    private int mResource;
    private int mDropDownResource;
    private LayoutInflater mInflater;

    public ResourcePropertyArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        init(context, textViewResourceId, 0);
    }

    public ResourcePropertyArrayAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        init(context, resource, textViewResourceId);
    }

    public ResourcePropertyArrayAdapter(Context context, int textViewResourceId, ResourceProperty[] objects) {
        super(context, textViewResourceId, objects);
        init(context, textViewResourceId, 0);
    }

    public ResourcePropertyArrayAdapter(Context context, int resource, int textViewResourceId, ResourceProperty[] objects) {
        super(context, resource, textViewResourceId, objects);
        init(context, resource, textViewResourceId);
    }

    public ResourcePropertyArrayAdapter(Context context, int textViewResourceId, List<ResourceProperty> objects) {
        super(context, textViewResourceId, objects);
        init(context, textViewResourceId, 0);
    }

    public ResourcePropertyArrayAdapter(Context context, int resource, int textViewResourceId, List<ResourceProperty> objects) {
        super(context, resource, textViewResourceId, objects);
        init(context, resource, textViewResourceId);
    }

    private void init(Context context, int resource, int textViewResourceId) {
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResource = mDropDownResource = resource;
        mFieldId = textViewResourceId;
    }

    @Override
    public void setDropDownViewResource(int resource) {
        super.setDropDownViewResource(resource);
        this.mDropDownResource = resource;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, mDropDownResource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, mResource);
    }

    private View createViewFromResource(int position, View convertView, ViewGroup parent,
                                        int resource) {
        View view;
        TextView text;

        if (convertView == null) {
            view = mInflater.inflate(resource, parent, false);
        } else {
            view = convertView;
        }

        try {
            if (mFieldId == 0) {
                //  If no custom field is assigned, assume the whole resource is a TextView
                text = (TextView) view;
            } else {
                //  Otherwise, find the TextView field within the layout
                text = (TextView) view.findViewById(mFieldId);
            }
        } catch (ClassCastException e) {
            Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "ArrayAdapter requires the resource ID to be a TextView", e);
        }

        ResourceProperty item = getItem(position);
        text.setText(item.getValue());

        return view;
    }
}
