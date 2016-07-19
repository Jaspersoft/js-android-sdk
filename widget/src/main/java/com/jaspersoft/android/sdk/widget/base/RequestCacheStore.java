package com.jaspersoft.android.sdk.widget.base;

import android.content.Context;

import java.io.File;

/**
 * @author Andrew Tivodar
 * @since 2.6
 */
public class RequestCacheStore {
    private final static String CACHE_FOLDER_NAME = "jsRequestCache";
    private final static int CACHE_SIZE = 52428800; // 50 MB

    public static File getRequestCacheDir(Context context) {
        File cacheDir = context.getApplicationContext().getExternalCacheDir();
        return new File(cacheDir, RequestCacheStore.CACHE_FOLDER_NAME);
    }

    public static int getCacheSize() {
        return CACHE_SIZE;
    }

    public static void clear(Context context) {
        File requestCacheDir = getRequestCacheDir(context);
        if (requestCacheDir.isDirectory()) {
            deleteDir(requestCacheDir);
        }
    }

    private static void deleteDir(File dir) {
        if (dir == null) return;

        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                deleteDir(new File(dir, child));
            }
        }
        dir.delete();
    }
}
