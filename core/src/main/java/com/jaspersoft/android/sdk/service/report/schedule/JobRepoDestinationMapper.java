/*
 * Copyright © 2015 TIBCO Software, Inc. All rights reserved.
 * http://community.jaspersoft.com/project/jaspermobile-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile for Android.
 *
 * TIBCO Jaspersoft Mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.schedule.JobFormEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.RepositoryDestinationEntity;
import com.jaspersoft.android.sdk.service.data.schedule.JobForm;
import com.jaspersoft.android.sdk.service.data.schedule.RepositoryDestination;

/**
 * @author Tom Koptel
 * @since 2.3
 */
class JobRepoDestinationMapper extends JobMapper {
    static final JobRepoDestinationMapper INSTANCE = new JobRepoDestinationMapper();

    @Override
    public void mapFormOnEntity(JobForm form, JobFormEntity entity) {
        RepositoryDestination repositoryDestination = form.getRepositoryDestination();
        entity.setRepositoryDestination(repositoryDestination.getFolderUri());
    }

    @Override
    public void mapEntityOnForm(JobForm.Builder form, JobFormEntity entity) {
        RepositoryDestination.Builder builder = new RepositoryDestination.Builder();

        RepositoryDestinationEntity destination = entity.getRepoDestination();
        builder.withFolderUri(destination.getFolderURI());
        builder.withOutputLocalFolder(destination.getOutputLocalFolder());
        builder.withOutputDescription(destination.getOutputDescription());
        builder.withTimestampPattern(destination.getTimestampPattern());
        builder.withDefaultReportOutputFolderURI(destination.getDefaultReportOutputFolderURI());
        builder.withUseDefaultReportOutputFolderURI(destination.getUsingDefaultReportOutputFolderURI());
        builder.withSaveToRepository(destination.getSaveToRepository());
        builder.withSequentialFilenames(destination.getSequentialFilenames());
        builder.withOverwriteFiles(destination.getOverwriteFiles());

        RepositoryDestination repositoryDestination = builder.build();
        form.withRepositoryDestination(repositoryDestination);
    }
}
