package com.jaspersoft.android.sdk.service.repository;

import com.jaspersoft.android.sdk.network.entity.resource.ResourceLookup;
import org.jetbrains.annotations.NotNull;

import static org.mockito.Mockito.when;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class MockResourceFactory {
    private MockResourceFactory() {
    }

    @NotNull
    public static <T extends ResourceLookup> T mockCommonFields(@NotNull T target) {
        when(target.getCreationDate()).thenReturn("2013-10-03 16:32:05");
        when(target.getUpdateDate()).thenReturn("2013-11-03 16:32:05");
        when(target.getResourceType()).thenReturn("reportUnit");
        when(target.getDescription()).thenReturn("description");
        when(target.getLabel()).thenReturn("label");
        when(target.getPermissionMask()).thenReturn(0);
        when(target.getVersion()).thenReturn(100);
        when(target.getUri()).thenReturn("/my/uri");
        when(target.getResourceType()).thenReturn("reportUnit");
        return target;
    }
}
