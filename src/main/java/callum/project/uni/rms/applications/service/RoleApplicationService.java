package callum.project.uni.rms.applications.service;

import callum.project.uni.rms.applications.mapper.RoleApplicationMapper;
import callum.project.uni.rms.applications.model.request.AppCreateReq;
import callum.project.uni.rms.applications.model.source.RoleApplication;
import callum.project.uni.rms.applications.model.target.ApplicationsForUser;
import callum.project.uni.rms.applications.model.target.HasUserAlreadyApplied;
import callum.project.uni.rms.applications.model.target.NumOfApps;
import callum.project.uni.rms.applications.model.target.TargetApplication;
import callum.project.uni.rms.applications.service.repository.RoleApplicationRepository;

import callum.project.uni.rms.parent.exception.InternalServiceException;
import callum.project.uni.rms.parent.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class RoleApplicationService {

    private final RoleApplicationRepository roleApplicationRepository;

    /**
     * Retrieve particular application based on it's id. Only returns info pertaining to the application itself.
     *
     * @param applicationId application id
     * @return application info in target format.
     */
    public TargetApplication retrieveApplicationById(Long applicationId) {
        try {
            Optional<RoleApplication> roleApplication = roleApplicationRepository.findById(applicationId);

            return RoleApplicationMapper.mapAppToTargetApp(
                    roleApplication.orElseThrow(() ->
                            new NotFoundException("Application not found")));
        } catch (HibernateException e) {
            throw new InternalServiceException(e.getMessage(), e);
        }
    }

    public TargetApplication retrieveApplicationByRoleAndUser(Long roleId, Long userId){
        try {
            Optional<RoleApplication> roleApplication = roleApplicationRepository
                    .findByRoleIdAndApplicantId(roleId, userId);

            return RoleApplicationMapper.mapAppToTargetApp(
                    roleApplication.orElseThrow(() ->
                            new NotFoundException("Application not found")));
        } catch (HibernateException e) {
            throw new InternalServiceException(e.getMessage(), e);
        }
    }
    
    /**
     * Retrieve all of the applications for a user.
     *
     * @param userId the internal user id.
     * @return the basic info for all of the applications a user has.
     */
    public ApplicationsForUser retrieveAllApplicationsForUser(Long userId) {

        try {
            List<RoleApplication> applications = roleApplicationRepository.findAllByApplicantId(userId);
            return ApplicationsForUser.builder()
                    .applicationInfoList(
                            applications.stream()
                                    .map(RoleApplicationMapper::mapAppToTargetApp)
                                    .collect(Collectors.toList()))
                    .build();

        } catch (HibernateException e) {
            throw new InternalServiceException("Issue calling retrieve applications for user", e);
        }
    }

    public ApplicationsForUser retrieveApplicationsForRm(Long rmId) {

        try {
            List<RoleApplication> applications = roleApplicationRepository.findAllByResourceManager(rmId);
            return ApplicationsForUser.builder()
                    .applicationInfoList(
                            applications.stream()
                                    .map(RoleApplicationMapper::mapAppToTargetApp)
                                    .collect(Collectors.toList()))
                    .build();

        } catch (HibernateException e) {
            throw new InternalServiceException("Issue calling retrieve applications for rm", e);
        }
    }

    public NumOfApps retrieveNumOfApplicationsForUser(Long userId) {

        try {
            int numOfApps = roleApplicationRepository.countDistinctByApplicantId(userId);
            return NumOfApps.builder()
                    .numOfApplications(numOfApps)
                    .build();
        } catch (HibernateException e) {
            throw new InternalServiceException(e.getMessage(), e);
        }
    }

    public TargetApplication addNewApplication(AppCreateReq request) {
        try {
            RoleApplication application = RoleApplicationMapper.mapRequestToRoleApplication(request);

            return RoleApplicationMapper.mapAppToTargetApp(
                    roleApplicationRepository.save(application));
        } catch (HibernateException e) {
            throw new InternalServiceException("Issue creating application for user", e);
        }
    }

    public HasUserAlreadyApplied userAlreadyApplied(Long roleId, Long userId) {
        try {
            Optional<RoleApplication> hasApp = roleApplicationRepository
                    .findFirstByRoleIdAndApplicantId(roleId, userId);
            
            return HasUserAlreadyApplied.builder()
                    .userAlreadyApplied(hasApp.isPresent())
                    .build();
        } catch (HibernateException e) {
            throw new InternalServiceException("Issue checking if user has already made application", e);
        }
    }
}
