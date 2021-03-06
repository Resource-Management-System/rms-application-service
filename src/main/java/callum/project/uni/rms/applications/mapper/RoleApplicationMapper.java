package callum.project.uni.rms.applications.mapper;

import callum.project.uni.rms.applications.model.request.AppCreateReq;
import callum.project.uni.rms.applications.model.target.TargetApplication;
import callum.project.uni.rms.applications.model.source.RoleApplication;
import callum.project.uni.rms.applications.model.ApplicationStatus;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;

import static callum.project.uni.rms.parent.mapper.MapperUtils.convertSqlDateToLocalDate;


@Service
public class RoleApplicationMapper {

    public static RoleApplication mapRequestToRoleApplication(AppCreateReq createReq){
        return  RoleApplication.builder()
                .applicationDate(Date.valueOf(LocalDate.now()))
                .applicationStatus(ApplicationStatus.SUBMITTED)
                .accountId(createReq.getAccountNumber())
                .projectId(createReq.getProjectCode())
                .applicantId(createReq.getApplicantId())
                .lastUpdatedDate(Date.valueOf(LocalDate.now()))
                .roleId(createReq.getRoleId())
                .build();
    }

    public static TargetApplication mapAppToTargetApp(RoleApplication app){

        return TargetApplication.builder()
                .accountId(app.getAccountId())
                .projectId(app.getProjectId())
                .roleId(app.getRoleId())
                .applicationId(app.getApplicationId())
                .applicantId(app.getApplicantId())
                .applicationDate(convertSqlDateToLocalDate(app.getApplicationDate()))
                .applicationStatus(app.getApplicationStatus())
                .lastUpdatedDate(convertSqlDateToLocalDate(app.getLastUpdatedDate()))
                .build();
    }
}
