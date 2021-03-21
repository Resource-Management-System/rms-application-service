package callum.project.uni.rms.applications.service;

import callum.project.uni.rms.applications.service.repository.RoleApplicationRepository;
import callum.project.uni.rms.parent.exception.InternalServiceException;
import org.hibernate.HibernateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;

import static callum.project.uni.rms.applications.helper.TargetApplicationBuilder.APPLICATION_ID;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleAppUpdateServiceTest {

    private RoleAppUpdateService roleAppUpdateService;
    private RoleApplicationRepository roleApplicationRepository;

    @BeforeEach
    private void setUp(){
        roleApplicationRepository = mock(RoleApplicationRepository.class);
        roleAppUpdateService = new RoleAppUpdateService(roleApplicationRepository);
    }

    @Test
    void confirmApplication() {
        roleAppUpdateService.confirmApplication(APPLICATION_ID);
    }

    @Test
    void confirmApplication_serverError() {

        doThrow(new HibernateException("ex"))
                .when(roleApplicationRepository)
                .updateApplicationStatus(eq(APPLICATION_ID), eq(Date.valueOf(LocalDate.now())), eq(4));

        assertThrows(InternalServiceException.class, () -> roleAppUpdateService.confirmApplication(APPLICATION_ID));
    }

    @Test
    void shortlistApplication() {
        doThrow(new HibernateException("ere"))
                .when(roleApplicationRepository)
                .updateApplicationStatus(eq(APPLICATION_ID), eq(Date.valueOf(LocalDate.now())), eq(3));

        assertThrows(InternalServiceException.class, () -> roleAppUpdateService.shortlistApplication(APPLICATION_ID));

    }

    @Test
    void rejectApplication() {
        doThrow(new HibernateException("ex"))
                .when(roleApplicationRepository)
                .updateApplicationStatus(eq(APPLICATION_ID), eq(Date.valueOf(LocalDate.now())), eq(2));

        assertThrows(InternalServiceException.class, () -> roleAppUpdateService.rejectApplication(APPLICATION_ID));
    }

    @Test
    void markApplicationAsInReview() {
        doThrow(new HibernateException("ex"))
                .when(roleApplicationRepository)
                .updateApplicationStatus(eq(APPLICATION_ID), eq(Date.valueOf(LocalDate.now())), eq(1));

        assertThrows(InternalServiceException.class, () -> roleAppUpdateService.markApplicationAsInReview(APPLICATION_ID));
    }
}