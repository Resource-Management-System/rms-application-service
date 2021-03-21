package callum.project.uni.rms.applications;

import callum.project.uni.rms.applications.model.ApplicationStatus;
import callum.project.uni.rms.applications.model.request.AppCreateReq;
import callum.project.uni.rms.applications.model.target.ApplicationsForUser;
import callum.project.uni.rms.applications.model.target.HasUserAlreadyApplied;
import callum.project.uni.rms.applications.model.target.NumOfApps;
import callum.project.uni.rms.applications.model.target.TargetApplication;
import callum.project.uni.rms.applications.service.RoleApplicationService;
import callum.project.uni.rms.parent.exception.InternalServiceException;
import callum.project.uni.rms.parent.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;

import static callum.project.uni.rms.applications.helper.AssertionHelper.assertApplication;
import static callum.project.uni.rms.applications.helper.TargetApplicationBuilder.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoleApplicationController.class)
class RoleApplicationControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RoleApplicationService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getApplicationForUser_happyPath() throws Exception {
        when(service.retrieveAllApplicationsForUser(eq(APPLICATION_ID)))
                .thenReturn(new ApplicationsForUser(List.of(buildTargetApplication())));

        ResultActions resultActions = this.mvc.perform(get("/applications")
                .queryParam("userId", APPLICATION_ID.toString()))
                .andDo(print())
                .andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        ApplicationsForUser res = objectMapper.readValue(contentAsString, ApplicationsForUser.class);
        List<TargetApplication> apps = res.getApplicationInfoList();
        TargetApplication application = apps.get(0);
        assertApplication(application);
    }

    @Test
    void getApplicationForUser_internalServiceException() throws Exception {
        when(service.retrieveAllApplicationsForUser(eq(APPLICATION_ID)))
                .thenThrow(InternalServiceException.class);

        this.mvc.perform(get("/applications")
                .queryParam("userId", APPLICATION_ID.toString()))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getApplicationsForRm_happyPath() throws Exception {
        when(service.retrieveApplicationsForRm(eq(RM_ID)))
                .thenReturn(new ApplicationsForUser(List.of(buildTargetApplication())));

        ResultActions resultActions = this.mvc.perform(get("/applications")
                .queryParam("rmId", RM_ID.toString()))
                .andDo(print())
                .andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        ApplicationsForUser res = objectMapper.readValue(contentAsString, ApplicationsForUser.class);
        List<TargetApplication> apps = res.getApplicationInfoList();
        TargetApplication application = apps.get(0);
        assertApplication(application);
    }

    @Test
    void getApplicationsForRm_internalServiceException() throws Exception {
        when(service.retrieveApplicationsForRm(eq(RM_ID)))
                .thenThrow(InternalServiceException.class);

        this.mvc.perform(get("/applications")
                .queryParam("rmId", RM_ID.toString()))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getUserAlreadyApplied_happyPath() throws Exception {
        when(service.userAlreadyApplied(eq(ROLE_ID), eq(APPLICANT_ID)))
                .thenReturn(new HasUserAlreadyApplied(true));

        ResultActions resultActions = this.mvc.perform(
                get("/applications/" + ROLE_ID + "/userAlreadyApplied")
                        .queryParam("userId", APPLICANT_ID.toString()))
                .andDo(print())
                .andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        HasUserAlreadyApplied res = objectMapper.readValue(contentAsString, HasUserAlreadyApplied.class);
        assertTrue(res.isUserAlreadyApplied());
    }

    @Test
    void getUserAlreadyApplied_internalServiceException() throws Exception {
        when(service.userAlreadyApplied(eq(ROLE_ID), eq(APPLICANT_ID)))
                .thenThrow(InternalServiceException.class);

        this.mvc.perform(
                get("/applications/" + ROLE_ID + "/userAlreadyApplied")
                        .queryParam("userId", APPLICANT_ID.toString()))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }


    @Test
    void getUserByRoleIdAndUserId_happyPath() throws Exception {
        when(service.retrieveApplicationByRoleAndUser(eq(ROLE_ID), eq(APPLICANT_ID)))
                .thenReturn(buildTargetApplication());

        ResultActions resultActions = this.mvc.perform(
                get("/application/role/" + ROLE_ID + "/user/" + APPLICANT_ID))
                .andDo(print())
                .andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        TargetApplication res = objectMapper.readValue(contentAsString, TargetApplication.class);
        assertApplication(res);
    }

    @Test
    void getUserByRoleIdAndUserId_internalServiceException() throws Exception {
        when(service.retrieveApplicationByRoleAndUser(eq(ROLE_ID), eq(APPLICANT_ID)))
                .thenThrow(InternalServiceException.class);

        this.mvc.perform
                (get("/application/role/" + ROLE_ID + "/user/" + APPLICANT_ID))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getUserByRoleIdAndUserId_notFound() throws Exception {
        when(service.retrieveApplicationByRoleAndUser(eq(ROLE_ID), eq(APPLICANT_ID)))
                .thenThrow(NotFoundException.class);

        this.mvc.perform
                (get("/application/role/" + ROLE_ID + "/user/" + APPLICANT_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getNumberOfApplications_happyPath() throws Exception {
        when(service.retrieveNumOfApplicationsForUser(eq(APPLICANT_ID)))
                .thenReturn(new NumOfApps(1));

        ResultActions resultActions = this.mvc.perform(
                get("/applications/amount")
                        .queryParam("userId", APPLICANT_ID.toString()))
                .andDo(print())
                .andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        NumOfApps res = objectMapper.readValue(contentAsString, NumOfApps.class);
        assertEquals(1, res.getNumOfApplications());
    }

    @Test
    void getNumberOfApplications_internalServerError() throws Exception {
        when(service.retrieveNumOfApplicationsForUser(eq(APPLICANT_ID)))
                .thenThrow(InternalServiceException.class);

        this.mvc.perform(
                get("/applications/amount")
                        .queryParam("userId", APPLICANT_ID.toString()))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    void postNewApp_happyPath() throws Exception {

        AppCreateReq roleCreateReq = buildAppCreateReq();
        when(service.addNewApplication(eq(roleCreateReq)))
                .thenReturn(buildTargetApplication());

        ResultActions resultActions = this.mvc.perform(post("/application/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"applicantId\": \"3\"," +
                        " \"roleId\": \"2\"," +
                        " \"projectCode\": \"1\", " +
                        "\"accountNumber\": \"1\"}"))
                .andDo(print())
                .andExpect(status().isCreated());
        verify(service, times(1))
                .addNewApplication(eq(roleCreateReq));

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        TargetApplication res = objectMapper.readValue(contentAsString, TargetApplication.class);
        assertApplication(res);
    }

    @Test
    void postNewApp_internalServiceException() throws Exception {

        AppCreateReq roleCreateReq = buildAppCreateReq();
        when(service.addNewApplication(eq(roleCreateReq)))
                .thenThrow(InternalServiceException.class);

        this.mvc.perform(post("/application/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"applicantId\": \"3\"," +
                        " \"roleId\": \"2\"," +
                        " \"projectCode\": \"1\", " +
                        "\"accountNumber\": \"1\"}"))
                .andDo(print())
                .andExpect(status().isInternalServerError());
        verify(service, times(1))
                .addNewApplication(eq(roleCreateReq));
    }
}