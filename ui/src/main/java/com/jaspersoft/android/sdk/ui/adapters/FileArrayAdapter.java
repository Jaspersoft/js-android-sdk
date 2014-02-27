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
import java.util.List;

/**
 * A concrete ArrayAdapter that is backed by an array of File objects.
 *
 * @author Ivan Gadzhega
 * @since 1.8
 */
public class FileArrayAdapter extends ArrayAdapter<File> {

    private final Context context;
    private final List<File> files;

    public FileArrayAdapter(Context context, File[] files) {
        this(context, Arrays.asList(files));
    }

    public FileArrayAdapter(Context context, List<File> files) {
        super(context, R.layout.file_list_item, files);
        this.context = context;
        this.files = files;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        File file = files.get(position);

        String fileName = file.getName();
        String baseName = FileUtils.getBaseName(fileName);
        String extension = FileUtils.getExtension(fileName);


        Drawable icon = getFileIconByExtension(extension);
        String dateModified = getFormattedDateModified(file);
        String size = getHumanReadableFileSize(file);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.file_list_item, parent, false);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.file_list_item_icon);
        TextView labelView = (TextView) rowView.findViewById(R.id.file_list_item_label);
        TextView dateView = (TextView) rowView.findViewById(R.id.file_list_item_date);
        TextView sizeView = (TextView) rowView.findViewById(R.id.file_list_item_size);

        imageView.setImageDrawable(icon);
        labelView.setText(baseName);
        dateView.setText(dateModified);
        sizeView.setText(size);

        return rowView;
    }

    //---------------------------------------------------------------------
    // Helper methods
    //---------------------------------------------------------------------

    private Drawable getFileIconByExtension(String extension) {
        FileType type = getFileTypeByExtension(extension);

        Drawable fileIcon;
        switch (type) {
            case HTML:
                fileIcon = context.getResources().getDrawable(R.drawable.ic_type_html);
                break;
            case PDF:
                fileIcon = context.getResources().getDrawable(R.drawable.ic_type_pdf);
                break;
            case XLS:
                fileIcon = context.getResources().getDrawable(R.drawable.ic_type_xls);
                break;
            default:
                fileIcon = context.getResources().getDrawable(R.drawable.ic_type_unknown);
                break;
        }

        return fileIcon;
    }

    private String getFormattedDateModified(File file) {
        return DateUtils.formatDateTime(context, file.lastModified(),
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

    private FileType getFileTypeByExtension(String extension) {
        try {
            return FileType.valueOf(extension);
        } catch (IllegalArgumentException ex) {
            return FileType.UNKNOWN;
        }
    }

    //---------------------------------------------------------------------
    // Nested Classes
    //---------------------------------------------------------------------

    private enum FileType {
        HTML,
        PDF,
        XLS,
        UNKNOWN
    }

}
