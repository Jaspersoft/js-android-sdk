package com.jaspersoft.android.sdk.network.entity.schedule;

import com.google.gson.annotations.Expose;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class RepositoryDestinationEntity {
    @Expose
    private String folderURI;

    public String getFolderURI() {
        return folderURI;
    }

    public void setFolderURI(String folderURI) {
        this.folderURI = folderURI;
    }
}
