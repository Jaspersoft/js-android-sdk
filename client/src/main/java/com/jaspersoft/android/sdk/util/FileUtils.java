/*
 * Copyright (C) 2012-2014 Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of Jaspersoft Mobile for Android.
 *
 * Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * @author Ivan Gadzhega
 * @since 1.8
 */
public final class FileUtils {

    public static final char EXTENSION_SEPARATOR = '.';

    private static final String TAG = "FileUtils";

    // This class cannot be instantiated
    private FileUtils() {}

    /**
     * Helper Method to Test if external Storage is Available
     * @return <code>true</code> if storage is writable, <code>false</code> otherwise
     */
    public static boolean isExternalStorageWritable() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(extStorageState);
    }

    /**
     * Recursively delete files in the specified folder.
     * @param directory directory file
     * @return {@code true} if these files were deleted, {@code false} otherwise.
     */
    public static boolean deleteFilesInDirectory(File directory) {
        if(directory.exists()) {
            File[] childFiles = directory.listFiles();
            if (childFiles != null) {
                for (File childFile : childFiles) {
                    if (childFile.isDirectory()) {
                        deleteFilesInDirectory(childFile);
                    }
                    // http://stackoverflow.com/questions/13948890/android-mkdirs-creates-a-zero-byte-file-instead-of-a-folder
                    File tmpFile = new File(childFile.getAbsolutePath() + System.currentTimeMillis());
                    boolean deleted = childFile.renameTo(tmpFile) && tmpFile.delete();
                    if (!deleted) {
                        Log.e(TAG, "Unable to delete " + childFile);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static long calculateFileSize(File file) {
        long result = 0;

        if (file.isDirectory()) {
            // Recursive call if it's a directory
            File[] childFiles = file.listFiles();
            if (childFiles != null) {
                for (File childFile : childFiles) {
                    result += calculateFileSize(childFile);
                }
            }
        } else {
            result += file.length();
        }

        return result;
    }

    public static String getHumanReadableByteCount(long bytes) {
        int unit = 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        char pre = ("kMGTPE").charAt(exp-1);
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    /**
     * Check if name contains any of the following characters:
     * * \ / " ' : ? | < > + [ ]
     */
    public static boolean nameContainsReservedChars(String fileName) {
        return fileName.matches(".*[\\Q*/\\\"':?|<>+[]\\E].*");
    }

    /**
     * Gets the base name, minus extension, from a filename.
     */
    public static String getBaseName(String fileName) {
        if (fileName == null) return null;
        int index = fileName.lastIndexOf(EXTENSION_SEPARATOR);
        return (index == -1) ? fileName : fileName.substring(0, index);
    }

    /**
     * Gets the extension of a filename.
     */
    public static String getExtension(String fileName) {
        if (fileName == null) return null;
        int index = fileName.lastIndexOf(EXTENSION_SEPARATOR);
        return (index == -1) ? "" : fileName.substring(index + 1);
    }

}
