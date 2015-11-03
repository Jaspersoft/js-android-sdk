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
import android.graphics.drawable.Drawable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaspersoft.android.sdk.ui.R;
import com.jaspersoft.android.sdk.util.FileUtils;

import java.io.File;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

/**
 * A concrete ArrayAdapter that is backed by an array of File objects.
 *
 * @author Ivan Gadzhega
 * @since 1.8
 */
public class FileArrayAdapter extends ArrayAdapter<File> {

    private LayoutInfo layoutInfo;
    private EnumMap<FileType, Integer> drawableIdsMap;

    @Deprecated
    public FileArrayAdapter(Context context, File[] files) {
        this(context, Arrays.asList(files));
    }

    public FileArrayAdapter(Context context, LayoutInfo layoutInfo, File[] files) {
        this(context, layoutInfo, Arrays.asList(files));
    }

    public FileArrayAdapter(Context context, LayoutInfo layoutInfo,
                            EnumMap<FileType,Integer> drawableIdsMap, File[] files) {
        this(context, layoutInfo, drawableIdsMap, Arrays.asList(files));
    }

    @Deprecated
    public FileArrayAdapter(Context context, List<File> files) {
        this(context, new LayoutInfo(R.layout.file_list_item), new EnumMap<FileType, Integer>(FileType.class), files);

        layoutInfo.setIconViewId(R.id.file_list_item_icon);
        layoutInfo.setLabelViewId(R.id.file_list_item_label);
        layoutInfo.setDateViewId(R.id.file_list_item_date);
        layoutInfo.setSizeViewId(R.id.file_list_item_size);

        drawableIdsMap.put(FileType.HTML, R.drawable.ic_type_html);
        drawableIdsMap.put(FileType.PDF, R.drawable.ic_type_pdf);
        drawableIdsMap.put(FileType.XLS, R.drawable.ic_type_xls);
        drawableIdsMap.put(FileType.UNKNOWN, R.drawable.ic_type_unknown);
    }

    public FileArrayAdapter(Context context, LayoutInfo layoutInfo, List<File> files) {
        this(context, layoutInfo, null, files);
    }

    public FileArrayAdapter(Context context, LayoutInfo layoutInfo,
                            EnumMap<FileType, Integer> drawableIdsMap, List<File> files) {
        super(context, layoutInfo.layoutResId, files);
        this.layoutInfo = layoutInfo;
        this.drawableIdsMap = drawableIdsMap;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        File file = getItem(position);

        // reuse views
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(layoutInfo.getLayoutResId(), parent, false);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.iconView = (ImageView) rowView.findViewById(layoutInfo.getIconViewId());
            viewHolder.labelView = (TextView) rowView.findViewById(layoutInfo.getLabelViewId());
            viewHolder.dateView = (TextView) rowView.findViewById(layoutInfo.getDateViewId());
            viewHolder.sizeView = (TextView) rowView.findViewById(layoutInfo.getSizeViewId());
            rowView.setTag(viewHolder);
        }
        // fill data
        ViewHolder viewHolder = (ViewHolder) rowView.getTag();
        if (viewHolder.iconView != null && drawableIdsMap != null) {
            String extension = FileUtils.getExtension(file.getName());
            Drawable icon = getFileIconByExtension(extension);
            viewHolder.iconView.setImageDrawable(icon);
        }
        if (viewHolder.labelView != null) {
            String baseName = FileUtils.getBaseName(file.getName());
            viewHolder.labelView.setText(baseName);
        }
        if (viewHolder.dateView != null) {
            String dateModified = getFormattedDateModified(file);
            viewHolder.dateView.setText(dateModified);
        }
        if (viewHolder.sizeView != null) {
            String size = getHumanReadableFileSize(file);
            viewHolder.sizeView.setText(size);
        }

        // return configured view
        return rowView;
    }

    //---------------------------------------------------------------------
    // Helper methods
    //---------------------------------------------------------------------

    private Drawable getFileIconByExtension(String extension) {
        FileType type = getFileTypeByExtension(extension);
        int drawableResId = drawableIdsMap.get(type);
        return getContext().getResources().getDrawable(drawableResId);
    }

    private FileType getFileTypeByExtension(String extension) {
        try {
            return FileType.valueOf(extension);
        } catch (IllegalArgumentException ex) {
            return FileType.UNKNOWN;
        }
    }

    private String getFormattedDateModified(File file) {
        return DateUtils.formatDateTime(getContext(), file.lastModified(),
                                        DateUtils.FORMAT_SHOW_DATE |
                                        DateUtils.FORMAT_SHOW_TIME |
                                        DateUtils.FORMAT_SHOW_YEAR |
                                        DateUtils.FORMAT_NUMERIC_DATE |
                                        DateUtils.FORMAT_24HOUR
        );
    }

    private String getHumanReadableFileSize(File file) {
        long bytes = FileUtils.calculateFileSize(file);
        return FileUtils.getHumanReadableByteCount(bytes);
    }

    //---------------------------------------------------------------------
    // Nested Classes
    //---------------------------------------------------------------------

    public enum FileType {
        HTML,
        PDF,
        XLS,
        UNKNOWN
    }

    public static class LayoutInfo {
        private int layoutResId;
        private int iconViewId;
        private int labelViewId;
        private int dateViewId;
        private int sizeViewId;

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

        public int getDateViewId() {
            return dateViewId;
        }

        public void setDateViewId(int dateViewId) {
            this.dateViewId = dateViewId;
        }

        public int getSizeViewId() {
            return sizeViewId;
        }

        public void setSizeViewId(int sizeViewId) {
            this.sizeViewId = sizeViewId;
        }
    }

    private static class ViewHolder {
        private ImageView iconView;
        private TextView labelView;
        private TextView dateView;
        private TextView sizeView;
    }

}
