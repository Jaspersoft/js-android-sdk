package com.jaspersoft.android.sdk.widget.base;

import android.annotation.TargetApi;
import android.os.Build;
import android.webkit.WebResourceResponse;

/**
 * @author Andrew Tivodar
 * @since 2.5
 */
public class NativeWebResponseMapper {
    public WebResourceResponse toNativeResponse(WebResponse webResponse) {
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                      return mapToLollipopResponse(webResponse);
                  }
              return mapToLegacyResponse(webResponse);
          }

              @TargetApi(Build.VERSION_CODES.LOLLIPOP)
      private WebResourceResponse mapToLollipopResponse(WebResponse webResponse) {
              return new WebResourceResponse(
                              webResponse.getMimeType(),
                              webResponse.getEncoding(),
                              webResponse.getStatusCode(),
                              webResponse.getReasonPhrase(),
                              webResponse.getResponseHeaders(),
                              webResponse.getData()
                              );
          }

              private WebResourceResponse mapToLegacyResponse(WebResponse webResponse) {
              return new WebResourceResponse(
                              webResponse.getMimeType(),
                              webResponse.getEncoding(),
                              webResponse.getData()
                              );
          }
}
