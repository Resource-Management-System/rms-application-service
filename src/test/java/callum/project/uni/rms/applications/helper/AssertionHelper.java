package callum.project.uni.rms.applications.helper;

import callum.project.uni.rms.applications.model.ApplicationStatus;
import callum.project.uni.rms.applications.model.target.TargetApplication;

import java.time.LocalDate;

import static callum.project.uni.rms.applications.helper.TargetApplicationBuilder.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssertionHelper {

    public static void assertApplication(TargetApplication application) {
        assertAll("Validating Application",
                () -> assertEquals("1", application.getAccountId()),
                () -> assertEquals("1", application.getProjectId()),
                () -> assertEquals(APPLICANT_ID, application.getApplicantId()),
                () -> assertEquals(LocalDate.now(), application.getApplicationDate()),
                () -> assertEquals(ROLE_ID, application.getRoleId()),
                () -> assertEquals(ApplicationStatus.SUBMITTED, application.getApplicationStatus()),
                () -> assertEquals(APPLICATION_ID, application.getApplicationId()));
    }
}
