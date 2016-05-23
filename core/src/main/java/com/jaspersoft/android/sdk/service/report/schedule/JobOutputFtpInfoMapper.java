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
import com.jaspersoft.android.sdk.network.entity.schedule.OutputFtpInfoEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.RepositoryDestinationEntity;
import com.jaspersoft.android.sdk.service.data.schedule.JobForm;
import com.jaspersoft.android.sdk.service.data.schedule.JobOutputFtpInfo;
import com.jaspersoft.android.sdk.service.data.schedule.RepositoryDestination;

/**
 * @author Tom Koptel
 * @since 2.5
 */
class JobOutputFtpInfoMapper {
    static final JobOutputFtpInfoMapper INSTANCE = new JobOutputFtpInfoMapper();

    public void mapFormOnEntity(JobForm form, JobFormEntity formEntity) {
        OutputFtpInfoEntity ftpEntity = createFtpEntity(formEntity);
        JobOutputFtpInfo ftpInfo = extractFtpInfo(form);

        if (ftpInfo != null) {
            JobOutputFtpInfo.Type type = ftpInfo.getType();
            if (type != null) {
                ftpEntity.setType(type.name().toLowerCase());
            }
            JobOutputFtpInfo.Prot prot = ftpInfo.getProt();
            if (prot != null) {
                ftpEntity.setProt(prot.getValue());
            }


            ftpEntity.setProtectionBufferSize(ftpInfo.getProtectionBufferSize());
            ftpEntity.setPort(ftpInfo.getPort());

            JobOutputFtpInfo.Protocol protocol = ftpInfo.getProtocol();
            if (protocol != null) {
                ftpEntity.setProtocol(protocol.name());
            }

            ftpEntity.setImplicit(ftpInfo.getImplicit());
            ftpEntity.setPassword(ftpInfo.getPassword());
            ftpEntity.setUserName(ftpInfo.getUserName());
            ftpEntity.setFolderPath(ftpInfo.getFolderPath());
            ftpEntity.setServerName(ftpInfo.getServerName());
        }
    }

    private JobOutputFtpInfo extractFtpInfo(JobForm form) {
        RepositoryDestination destination = form.getRepositoryDestination();
        return destination.getOutputFtpInfo();
    }

    private OutputFtpInfoEntity createFtpEntity(JobFormEntity entity) {
        RepositoryDestinationEntity repoDestination = entity.getRepoDestination();
        OutputFtpInfoEntity outputFTPInfo = new OutputFtpInfoEntity();
        repoDestination.setOutputFTPInfo(outputFTPInfo);
        return outputFTPInfo;
    }

    public void mapEntityOnForm(RepositoryDestination.Builder destinationBuilder, JobFormEntity formEntity) {
        OutputFtpInfoEntity entity = extractFtpEntity(formEntity);
        if (entity != null) {
            JobOutputFtpInfo.Builder builder = new JobOutputFtpInfo.Builder();

            String stringType = entity.getType();
            if (stringType != null) {
                stringType = stringType.toUpperCase();
                JobOutputFtpInfo.Type type = JobOutputFtpInfo.Type.valueOf(stringType);
                builder.withType(type);
            }

            String stringProt = entity.getProt();
            if (stringProt != null) {
                stringProt = stringProt.toUpperCase();
                JobOutputFtpInfo.Prot prot = JobOutputFtpInfo.Prot.valueOfEntity(stringProt);
                builder.withProt(prot);
            }


            builder.withProtectionBufferSize(entity.getProtectionBufferSize());
            builder.withPort(entity.getPort());

            String stringProtocol = entity.getProtocol();
            if (stringProtocol != null) {
                stringProtocol = stringProtocol.toUpperCase();
                JobOutputFtpInfo.Protocol protocol = JobOutputFtpInfo.Protocol.valueOf(stringProtocol);
                builder.withProtocol(protocol);
            }

            builder.withImplicit(entity.getImplicit());
            builder.withPassword(entity.getPassword());
            builder.withUserName(entity.getUserName());
            builder.withFolderPath(entity.getFolderPath());
            builder.withServerName(entity.getServerName());

            destinationBuilder.withFtp(builder.build());
        }
    }

    private OutputFtpInfoEntity extractFtpEntity(JobFormEntity entity) {
        RepositoryDestinationEntity repoDestination = entity.getRepoDestination();
        if (repoDestination != null) {
            return repoDestination.getOutputFTPInfo();
        }
        return null;
    }
}
