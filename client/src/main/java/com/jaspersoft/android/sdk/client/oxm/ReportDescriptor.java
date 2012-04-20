/*
 * Copyright (C) 2005 - 2012 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com.
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of  the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public  License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.jaspersoft.android.sdk.client.oxm;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * This class represents a report descriptor entity for convenient XML serialization process.
 *
 * @author Ivan Gadzhega
 * @version $Id$
 * @since 1.0
 */

@Root(name="report", strict=false)
public class ReportDescriptor {

    @Element
    private String uuid;
    @Element
    private String originalUri;
    @Element
    private Integer totalPages;
    @Element
    private Integer startPage;
    @Element
    private Integer endPage;

    @ElementList(entry="file", inline=true, empty=false)
    private List<ReportAttachment> attachments;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getOriginalUri() {
        return originalUri;
    }

    public void setOriginalUri(String originalUri) {
        this.originalUri = originalUri;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getStartPage() {
        return startPage;
    }

    public void setStartPage(Integer startPage) {
        this.startPage = startPage;
    }

    public Integer getEndPage() {
        return endPage;
    }

    public void setEndPage(Integer endPage) {
        this.endPage = endPage;
    }

    public List<ReportAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<ReportAttachment> attachments) {
        this.attachments = attachments;
    }
}
