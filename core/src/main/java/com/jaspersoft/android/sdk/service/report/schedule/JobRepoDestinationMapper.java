/*
 * Copyright (C) 2016 TIBCO Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/mobile-sdk-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile SDK for Android.
 *
 * TIBCO Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile SDK for Android. If not, see
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
    static final JobRepoDestinationMapper INSTANCE = new JobRepoDestinationMapper(JobOutputFtpInfoMapper.INSTANCE);

    private final JobOutputFtpInfoMapper ftpInfoMapper;

    JobRepoDestinationMapper(JobOutputFtpInfoMapper ftpInfoMapper) {
        this.ftpInfoMapper = ftpInfoMapper;
    }

    @Override
    public void mapFormOnEntity(JobForm form, JobFormEntity entity) {
        RepositoryDestination serviceFormDestination = form.getRepositoryDestination();
        RepositoryDestinationEntity destination = entity.getRepoDestination();

        destination.setFolderURI(serviceFormDestination.getFolderUri());
        destination.setOutputLocalFolder(serviceFormDestination.getOutputLocalFolder());
        destination.setOutputDescription(serviceFormDestination.getOutputDescription());
        destination.setTimestampPattern(serviceFormDestination.getTimestampPattern());
        destination.setDefaultReportOutputFolderURI(serviceFormDestination.getDefaultReportOutputFolderURI());
        destination.setUsingDefaultReportOutputFolderURI(serviceFormDestination.getUseDefaultReportOutputFolderURI());
        destination.setSaveToRepository(serviceFormDestination.getSaveToRepository());
        destination.setSequentialFilenames(serviceFormDestination.getSequentialFileNames());
        destination.setOverwriteFiles(serviceFormDestination.getOverwriteFiles());

        ftpInfoMapper.mapFormOnEntity(form, entity);
    }

    @Override
    public void mapEntityOnForm(JobForm.Builder form, JobFormEntity entity) {
        RepositoryDestination.Builder builder = new RepositoryDestination.Builder();

        RepositoryDestinationEntity destination = entity.getRepoDestination();
        String folderURI = destination.getFolderURI();
        if (folderURI != null) {
            builder.withFolderUri(folderURI);
        }
        String localFolder = destination.getOutputLocalFolder();
        if (localFolder != null) {
            builder.withOutputLocalFolder(localFolder);
        }
        String outputFolderURI = destination.getDefaultReportOutputFolderURI();
        if (outputFolderURI != null) {
            builder.withDefaultReportOutputFolderURI(outputFolderURI);
        }

        builder.withOutputDescription(destination.getOutputDescription());
        builder.withTimestampPattern(destination.getTimestampPattern());
        builder.withUseDefaultReportOutputFolderURI(destination.getUsingDefaultReportOutputFolderURI());
        builder.withSaveToRepository(destination.getSaveToRepository());
        builder.withSequentialFileNames(destination.getSequentialFilenames());
        builder.withOverwriteFiles(destination.getOverwriteFiles());


        ftpInfoMapper.mapEntityOnForm(builder, entity);
        RepositoryDestination repositoryDestination = builder.build();

        form.withRepositoryDestination(repositoryDestination);
    }
}
