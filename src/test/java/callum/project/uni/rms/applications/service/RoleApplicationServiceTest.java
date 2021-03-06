package callum.project.uni.rms.applications.service;

import callum.project.uni.rms.applications.model.ApplicationStatus;
import callum.project.uni.rms.applications.model.request.AppCreateReq;
import callum.project.uni.rms.applications.model.source.RoleApplication;
import callum.project.uni.rms.applications.model.target.ApplicationsForUser;
import callum.project.uni.rms.applications.service.repository.RoleApplicationRepository;
import callum.project.uni.rms.parent.exception.InternalServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleApplicationServiceTest {

    private RoleApplicationService roleApplicationService;

    private RoleApplicationRepository roleApplicationRepository;
    private static final Long USER_ID = 1L;
    private static final Long ROLE_ID = 1L;


    @BeforeEach
    void setup() {
        roleApplicationRepository = mock(RoleApplicationRepository.class);
        roleApplicationService = new RoleApplicationService(roleApplicationRepository);
    }

    @Test
    @DisplayName("Test happy path, should send all params to db")
    void addNewApplication_happyPath() throws InternalServiceException {

        RoleApplication roleApplication = RoleApplication.builder()
                .accountId("123")
                .applicantId(USER_ID)
                .projectId("2")
                .roleId(ROLE_ID)
                .applicationDate(Date.valueOf(LocalDate.now()))
                .lastUpdatedDate(Date.valueOf(LocalDate.now()))
                .applicationStatus(ApplicationStatus.SUBMITTED)
                .applicationId(null)
                .build();

        when(roleApplicationRepository.save(eq(roleApplication)))
                .thenReturn(new RoleApplication());

        AppCreateReq createReq = AppCreateReq.builder()
                .accountNumber("123")
                .applicantId(USER_ID)
                .projectCode("2")
                .roleId(ROLE_ID)
                .build();

        roleApplicationService.addNewApplication(createReq);
    }

    @Test
    @DisplayName("Test empty response from role application retrieve all apps")
    void retrieveAllApplicationsForUser_emptyList() throws InternalServiceException {
        when(roleApplicationRepository.findAllByApplicantId(eq(USER_ID)))
                .thenReturn(emptyList());
        ApplicationsForUser response = roleApplicationService.retrieveAllApplicationsForUser(USER_ID);
        assertTrue(response.getApplicationInfoList().isEmpty());
    }
}
