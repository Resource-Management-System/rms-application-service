package callum.project.uni.rms.applications.helper;

import callum.project.uni.rms.applications.model.ApplicationStatus;
import callum.project.uni.rms.applications.model.request.AppCreateReq;
import callum.project.uni.rms.applications.model.request.RoleAppUpdateStatusReq;
import callum.project.uni.rms.applications.model.request.ShortlistCreateReq;
import callum.project.uni.rms.applications.model.target.TargetApplication;

import java.time.LocalDate;

public class TargetApplicationBuilder {

    public static final Long APPLICATION_ID = 1L;
    public static final Long ROLE_ID = 2L;
    public static final Long APPLICANT_ID = 3L;
    public static final Long RM_ID = 4L;


    public static TargetApplication buildTargetApplication() {
        return TargetApplication.builder()
                .applicantId(APPLICANT_ID)
                .accountId("1")
                .applicationDate(LocalDate.now())
                .applicationId(APPLICATION_ID)
                .applicationStatus(ApplicationStatus.SUBMITTED)
                .businessUnitId(1L)
                .lastUpdatedDate(LocalDate.now())
                .projectId("1")
                .roleId(ROLE_ID)
                .build();
    }

    public static AppCreateReq buildAppCreateReq() {
        return AppCreateReq.builder()
                .accountNumber("1")
                .applicantId(APPLICANT_ID)
                .projectCode("1")
                .roleId(ROLE_ID)
                .build();
    }

    public static RoleAppUpdateStatusReq buildAppUpdateStatusReq() {
        return RoleAppUpdateStatusReq.builder()
                .applicationId(APPLICATION_ID)
                .authorizerId(APPLICANT_ID)
                .build();
    }

    public static ShortlistCreateReq shortlistCreateReq() {
        return ShortlistCreateReq.builder()
                .candidateId(APPLICANT_ID)
                .roleId(ROLE_ID)
                .build();
    }
}
