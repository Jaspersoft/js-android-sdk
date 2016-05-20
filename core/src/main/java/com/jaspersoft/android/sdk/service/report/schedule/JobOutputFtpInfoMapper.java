package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.schedule.JobFormEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.OutputFtpInfoEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.RepositoryDestinationEntity;
import com.jaspersoft.android.sdk.service.data.schedule.JobForm;
import com.jaspersoft.android.sdk.service.data.schedule.OutputFtpInfo;
import com.jaspersoft.android.sdk.service.data.schedule.RepositoryDestination;

/**
 * @author Tom Koptel
 * @since 2.5
 */
class JobOutputFtpInfoMapper {
    static final JobOutputFtpInfoMapper INSTANCE = new JobOutputFtpInfoMapper();

    public void mapFormOnEntity(JobForm form, JobFormEntity formEntity) {
        OutputFtpInfoEntity ftpEntity = createFtpEntity(formEntity);
        OutputFtpInfo ftpInfo = extractFtpInfo(form);

        if (ftpInfo != null) {
            OutputFtpInfo.Type type = ftpInfo.getType();
            if (type != null) {
                ftpEntity.setType(type.name().toLowerCase());
            }
            OutputFtpInfo.Prot prot = ftpInfo.getProt();
            if (prot != null) {
                ftpEntity.setProt(prot.getValue());
            }


            ftpEntity.setProtectionBufferSize(ftpInfo.getProtectionBufferSize());
            ftpEntity.setPort(ftpInfo.getPort());

            OutputFtpInfo.Protocol protocol = ftpInfo.getProtocol();
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

    private OutputFtpInfo extractFtpInfo(JobForm form) {
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
            OutputFtpInfo.Builder builder = new OutputFtpInfo.Builder();

            String stringType = entity.getType();
            if (stringType != null) {
                stringType = stringType.toUpperCase();
                OutputFtpInfo.Type type = OutputFtpInfo.Type.valueOf(stringType);
                builder.withType(type);
            }

            String stringProt = entity.getProt();
            if (stringProt != null) {
                stringProt = stringProt.toUpperCase();
                OutputFtpInfo.Prot prot = OutputFtpInfo.Prot.valueOfEntity(stringProt);
                builder.withProt(prot);
            }


            builder.withProtectionBufferSize(entity.getProtectionBufferSize());
            builder.withPort(entity.getPort());

            String stringProtocol = entity.getProtocol();
            if (stringProtocol != null) {
                stringProtocol = stringProtocol.toUpperCase();
                OutputFtpInfo.Protocol protocol = OutputFtpInfo.Protocol.valueOf(stringProtocol);
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
