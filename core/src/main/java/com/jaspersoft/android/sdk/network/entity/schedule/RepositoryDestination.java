package com.jaspersoft.android.sdk.network.entity.schedule;

import com.google.gson.annotations.Expose;

/**
 * @author Tom Koptel
 * @since 2.0
 */
class RepositoryDestination {
    @Expose
    private final String folderURI;

    RepositoryDestination(String folderURI) {
        this.folderURI = folderURI;
    }
}
