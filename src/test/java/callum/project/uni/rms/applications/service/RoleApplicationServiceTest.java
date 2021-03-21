package callum.project.uni.rms.applications.service;

import callum.project.uni.rms.applications.model.request.AppCreateReq;
import callum.project.uni.rms.applications.model.source.RoleApplication;
import callum.project.uni.rms.applications.model.target.ApplicationsForUser;
import callum.project.uni.rms.applications.model.target.HasUserAlreadyApplied;
import callum.project.uni.rms.applications.model.target.NumOfApps;
import callum.project.uni.rms.applications.model.target.TargetApplication;
import callum.project.uni.rms.applications.service.repository.RoleApplicationRepository;
import callum.project.uni.rms.parent.exception.InternalServiceException;
import callum.project.uni.rms.parent.exception.NotFoundException;
import org.hibernate.HibernateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static callum.project.uni.rms.applications.helper.AssertionHelper.assertApplication;
import static callum.project.uni.rms.applications.helper.TargetApplicationBuilder.*;
import static callum.project.uni.rms.applications.service.helper.SourceApplicationBuilder.buildCreatedSourceRoleApplication;
import static callum.project.uni.rms.applications.service.helper.SourceApplicationBuilder.buildSourceRoleApplication;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleApplicationServiceTest {

    private RoleApplicationService roleApplicationService;

    private RoleApplicationRepository roleApplicationRepository;

    @BeforeEach
    void setUp() {
        roleApplicationRepository = mock(RoleApplicationRepository.class);
        roleApplicationService = new RoleApplicationService(roleApplicationRepository);
    }

    @Test
    void retrieveApplicationById_happyPath(){
        when(roleApplicationRepository.findById(eq(APPLICATION_ID)))
                .thenReturn(Optional.of(buildSourceRoleApplication()));

        TargetApplication res = roleApplicationService.retrieveApplicationById(APPLICATION_ID);
        assertApplication(res);
    }

    @Test
    void retrieveApplicationById_notFound(){
        when(roleApplicationRepository.findById(eq(APPLICATION_ID)))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> roleApplicationService.retrieveApplicationById(APPLICATION_ID));
    }

    @Test
    void retrieveApplicationById_serverError(){
        when(roleApplicationRepository.findById(eq(APPLICATION_ID)))
                .thenThrow(HibernateException.class);

        assertThrows(InternalServiceException.class,
                () -> roleApplicationService.retrieveApplicationById(APPLICATION_ID));
    }

    @Test
    void retrieveApplicationByRoleAndUser_happyPath(){
        when(roleApplicationRepository.findByRoleIdAndApplicantId(eq(ROLE_ID), eq(APPLICANT_ID)))
                .thenReturn(Optional.of(buildSourceRoleApplication()));

        TargetApplication res = roleApplicationService.retrieveApplicationByRoleAndUser(ROLE_ID, APPLICANT_ID);
        assertApplication(res);
    }

    @Test
    void retrieveApplicationByRoleAndUser_notFound(){
        when(roleApplicationRepository.findByRoleIdAndApplicantId(eq(ROLE_ID), eq(APPLICANT_ID)))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> roleApplicationService.retrieveApplicationByRoleAndUser(ROLE_ID, APPLICANT_ID));
    }

    @Test
    void retrieveApplicationByRoleAndUser_serverError(){
        when(roleApplicationRepository.findByRoleIdAndApplicantId(eq(ROLE_ID), eq(APPLICANT_ID)))
                .thenThrow(HibernateException.class);

        assertThrows(InternalServiceException.class,
                () -> roleApplicationService.retrieveApplicationByRoleAndUser(ROLE_ID, APPLICANT_ID));
    }

    @Test
    void retrieveAllApplicationsForUser_happyPath(){
        when(roleApplicationRepository.findAllByApplicantId(eq(APPLICANT_ID)))
                .thenReturn(singletonList(buildSourceRoleApplication()));

        ApplicationsForUser res = roleApplicationService.retrieveAllApplicationsForUser(APPLICANT_ID);
        assertApplication(res.getApplicationInfoList().get(0));
    }

    @Test
    void retrieveAllApplicationsForUser_serverError(){
        when(roleApplicationRepository.findAllByApplicantId(eq(APPLICANT_ID)))
                .thenThrow(HibernateException.class);

        assertThrows(InternalServiceException.class,
                () -> roleApplicationService.retrieveAllApplicationsForUser(APPLICANT_ID));
    }

    @Test
    void retrieveApplicationsForRm_happyPath(){
        when(roleApplicationRepository.findAllByResourceManager(eq(RM_ID)))
                .thenReturn(singletonList(buildSourceRoleApplication()));

        ApplicationsForUser res = roleApplicationService.retrieveApplicationsForRm(RM_ID);
        assertApplication(res.getApplicationInfoList().get(0));
    }

    @Test
    void retrieveApplicationsForRm_serverError(){
        when(roleApplicationRepository.findAllByResourceManager(eq(RM_ID)))
                .thenThrow(HibernateException.class);

        assertThrows(InternalServiceException.class,
                () -> roleApplicationService.retrieveApplicationsForRm(RM_ID));
    }

    @Test
    void retrieveNumOfApplicationsForUser_happyPath(){
        when(roleApplicationRepository.countDistinctByApplicantId(eq(APPLICANT_ID)))
                .thenReturn(1);

        NumOfApps res = roleApplicationService.retrieveNumOfApplicationsForUser(APPLICANT_ID);
        assertEquals(1, res.getNumOfApplications());
    }

    @Test
    void retrieveNumOfApplicationsForUser_serverError(){
        when(roleApplicationRepository.countDistinctByApplicantId(eq(APPLICANT_ID)))
                .thenThrow(HibernateException.class);

        assertThrows(InternalServiceException.class,
                () -> roleApplicationService.retrieveNumOfApplicationsForUser(APPLICANT_ID));
    }

    @Test
    @DisplayName("Test happy path, should send all params to db")
    void addNewApplication_happyPath() throws InternalServiceException {

        RoleApplication roleApplication = buildCreatedSourceRoleApplication();

        when(roleApplicationRepository.save(eq(roleApplication)))
                .thenReturn(buildSourceRoleApplication());

        AppCreateReq createReq = buildAppCreateReq();

        TargetApplication res = roleApplicationService.addNewApplication(createReq);
        assertApplication(res);
    }

    @Test
    void addNewApplication_serverError() throws InternalServiceException {

        RoleApplication roleApplication = buildCreatedSourceRoleApplication();

        when(roleApplicationRepository.save(eq(roleApplication)))
                .thenThrow(HibernateException.class);

        AppCreateReq createReq = buildAppCreateReq();

        assertThrows(InternalServiceException.class, () -> roleApplicationService.addNewApplication(createReq));
    }

    @Test
    void userAlreadyApplied_true(){
        when(roleApplicationRepository.findFirstByRoleIdAndApplicantId(eq(APPLICANT_ID), eq(ROLE_ID)))
                .thenReturn(Optional.of(buildSourceRoleApplication()));

        HasUserAlreadyApplied res = roleApplicationService.userAlreadyApplied(APPLICANT_ID, ROLE_ID);
        assertTrue( res.isUserAlreadyApplied());
    }

    @Test
    void userAlreadyApplied_false(){
        when(roleApplicationRepository.findFirstByRoleIdAndApplicantId(eq(APPLICANT_ID), eq(ROLE_ID)))
                .thenReturn(Optional.empty());

        HasUserAlreadyApplied res = roleApplicationService.userAlreadyApplied(APPLICANT_ID, ROLE_ID);
        assertFalse(res.isUserAlreadyApplied());
    }

    @Test
    void userAlreadyApplied_serverError(){
        when(roleApplicationRepository.findFirstByRoleIdAndApplicantId(eq(APPLICANT_ID), eq(ROLE_ID)))
                .thenThrow(HibernateException.class);

        assertThrows(InternalServiceException.class,
                () -> roleApplicationService.userAlreadyApplied(APPLICANT_ID, ROLE_ID));
    }
}
