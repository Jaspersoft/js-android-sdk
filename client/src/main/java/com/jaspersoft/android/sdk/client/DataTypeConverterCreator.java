package com.jaspersoft.android.sdk.client;

import android.support.annotation.NonNull;

import org.springframework.http.converter.HttpMessageConverter;

/**
 * @author Tom Koptel
 * @since 1.10
 */
interface DataTypeConverterCreator<T extends HttpMessageConverter<?>> {
    @NonNull
    T create();
}
