/*
 * Copyright (c) 2014. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.jaspersoft.android.sdk.client.oxm.report;

import com.jaspersoft.android.sdk.client.oxm.resource.ResourceLookup;

import org.simpleframework.xml.Root;

@Root(name = "folder")
public class FolderDataResponse extends ResourceLookup {
    public FolderDataResponse() {
        this.resourceType = ResourceType.folder.toString();
    }
}
