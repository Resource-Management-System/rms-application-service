package callum.project.uni.rms.applications.service;

import callum.project.uni.rms.applications.model.ApplicationStatus;
import callum.project.uni.rms.applications.service.repository.RoleApplicationRepository;
import callum.project.uni.rms.parent.exception.InternalServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class RoleAppUpdateService {
    private final RoleApplicationRepository roleApplicationRepository;


    public void confirmApplication(Long applicationId) {
        try {
            roleApplicationRepository.updateApplicationStatus(applicationId, createCurrentDate(),
                    ApplicationStatus.ACCEPTED.ordinal());
        } catch (HibernateException e) {
            throw new InternalServiceException(e.getMessage(),e);
        }
    }

    public void shortlistApplication(Long applicationId) {
        try {
            log.info("Confirming the following application: " + applicationId);

            roleApplicationRepository.updateApplicationStatus(applicationId,
                    createCurrentDate(), ApplicationStatus.SHORTLISTED.ordinal());
        } catch (HibernateException e) {
            throw new InternalServiceException(e.getMessage(),e);
        }
    }

    public void rejectApplication(Long applicationId) {
        try {
            log.info("Rejecting the following application: " + applicationId);
            roleApplicationRepository.updateApplicationStatus(applicationId,
                    createCurrentDate(), ApplicationStatus.REJECTED.ordinal());
        } catch (HibernateException e) {
            throw new InternalServiceException(e.getMessage(), e);
        }
    }

    public void markApplicationAsInReview(Long applicationId) {
        try {
            log.info("Rejecting the following application: " + applicationId);
            roleApplicationRepository.updateApplicationStatus(applicationId,
                    createCurrentDate(), ApplicationStatus.IN_REVIEW.ordinal());
        } catch (HibernateException e) {
            throw new InternalServiceException(e.getMessage(), e);
        }
    }

    private Date createCurrentDate() {
        return Date.valueOf(LocalDate.now());
    }

}
