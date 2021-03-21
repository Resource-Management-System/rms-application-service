package callum.project.uni.rms.applications.service.helper;

import callum.project.uni.rms.applications.model.ApplicationStatus;
import callum.project.uni.rms.applications.model.request.AppCreateReq;
import callum.project.uni.rms.applications.model.source.RoleApplication;
import callum.project.uni.rms.applications.model.target.TargetApplication;

import java.sql.Date;
import java.time.LocalDate;

import static callum.project.uni.rms.applications.helper.TargetApplicationBuilder.*;

public class SourceApplicationBuilder {

    public static RoleApplication buildCreatedSourceRoleApplication(){
        return RoleApplication.builder()
                .applicantId(APPLICANT_ID)
                .accountId("1")
                .applicationDate(Date.valueOf(LocalDate.now()))
                .applicationStatus(ApplicationStatus.SUBMITTED)
                .lastUpdatedDate(Date.valueOf(LocalDate.now()))
                .projectId("1")
                .roleId(ROLE_ID)
                .build();
    }

    public static RoleApplication buildSourceRoleApplication(){
        return RoleApplication.builder()
                .applicantId(APPLICANT_ID)
                .applicationId(APPLICATION_ID)
                .accountId("1")
                .applicationDate(Date.valueOf(LocalDate.now()))
                .applicationStatus(ApplicationStatus.SUBMITTED)
                .lastUpdatedDate(Date.valueOf(LocalDate.now()))
                .projectId("1")
                .roleId(ROLE_ID)
                .build();
    }
}
