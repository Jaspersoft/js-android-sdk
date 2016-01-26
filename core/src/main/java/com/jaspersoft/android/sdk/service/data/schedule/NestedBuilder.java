package com.jaspersoft.android.sdk.service.data.schedule;

/**
 * @author Tom Koptel
 * @since 2.0
 */
abstract class NestedBuilder<Parent, Value> {
    public abstract Parent done();
    public abstract Value build();
}
