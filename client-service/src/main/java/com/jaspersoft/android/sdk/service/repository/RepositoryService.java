package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.service.data.resource.ResourceLookup;

import java.util.Collection;

/**
 * @author Tom Koptel
 * @since 2.0
 */
public interface RepositoryService {
    Collection<ResourceLookup> searchResources();
}
