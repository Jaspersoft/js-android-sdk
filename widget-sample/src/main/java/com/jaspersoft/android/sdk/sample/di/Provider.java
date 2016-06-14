package com.jaspersoft.android.sdk.sample.di;

/**
 * @author Tom Koptel
 * @since 2.6
 */
public interface Provider<Dependency> {
    Dependency provide();
}
