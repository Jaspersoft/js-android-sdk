package com.jaspersoft.android.sdk.widget.report.renderer;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public class AssetFile {
    public static final AssetFile EMPTY = new AssetFile("empty-asset");

    private String mContent;

    private AssetFile(String content) {
        mContent = content;
    }

    @Override
    public String toString() {
        return mContent;
    }

    public static class Factory {
        private Context mContext;

        public Factory(Context context) {
            mContext = context;
        }

        public AssetFile load(String name) {
            AssetManager assets = mContext.getResources().getAssets();
            try {
                InputStream stream = assets.open(name);
                String content = AssetFile.toString(stream);
                return new AssetFile(content);
            } catch (IOException e) {
                return EMPTY;
            }
        }
    }

    static String toString(InputStream stream) throws IOException {
        java.util.Scanner s = new java.util.Scanner(stream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
