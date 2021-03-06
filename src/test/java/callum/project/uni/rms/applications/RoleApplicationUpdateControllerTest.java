package callum.project.uni.rms.applications;

import callum.project.uni.rms.applications.service.RoleAppUpdateService;
import callum.project.uni.rms.applications.service.RoleApplicationService;
import callum.project.uni.rms.applications.service.ShortlistService;
import callum.project.uni.rms.parent.exception.InternalServiceException;
import callum.project.uni.rms.parent.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.HibernateException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static callum.project.uni.rms.applications.helper.TargetApplicationBuilder.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoleApplicationUpdateController.class)
class RoleApplicationUpdateControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RoleApplicationService roleApplicationService;

    @MockBean
    private ShortlistService shortlistService;

    @MockBean
    private RoleAppUpdateService roleAppUpdateService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void putApplicationShortlist_happyPath() throws Exception {

        when(roleApplicationService.retrieveApplicationById(eq(APPLICATION_ID)))
                .thenReturn(buildTargetApplication());

        this.mvc.perform(put("/application/shortlist")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"applicationId\": \"" + APPLICATION_ID + "\", \"authorizerId\": \"1\"}"))
                .andDo(print())
                .andExpect(status().isNoContent());
        verify(shortlistService, times(1))
                .addUserToShortlist(eq(ROLE_ID), eq(APPLICANT_ID));
        verify(roleAppUpdateService, times(1)).shortlistApplication(eq(APPLICATION_ID));
    }

    @Test
    void putApplicationShortlist_notFound() throws Exception {

        when(roleApplicationService.retrieveApplicationById(eq(APPLICATION_ID)))
                .thenThrow(NotFoundException.class);

        this.mvc.perform(put("/application/shortlist")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"applicationId\": \"" + APPLICATION_ID + "\", \"authorizerId\": \"1\"}"))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(shortlistService, times(0))
                .addUserToShortlist(any(), any());
        verify(roleAppUpdateService, times(0)).shortlistApplication(any());
    }


    @Test
    void putApplicationShortlist_internalServiceException() throws Exception {

        when(roleApplicationService.retrieveApplicationById(eq(APPLICATION_ID)))
                .thenThrow(InternalServiceException.class);

        this.mvc.perform(put("/application/shortlist")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"applicationId\": \"" + APPLICATION_ID + "\", \"authorizerId\": \"1\"}"))
                .andDo(print())
                .andExpect(status().isInternalServerError());
        verify(shortlistService, times(0))
                .addUserToShortlist(any(), any());
        verify(roleAppUpdateService, times(0)).shortlistApplication(any());
    }

    @Test
    void putRejectApp_happyPath() throws Exception {

        this.mvc.perform(put("/application/reject")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"applicationId\": \"" + APPLICATION_ID + "\", \"authorizerId\": \"1\"}"))
                .andDo(print())
                .andExpect(status().isNoContent());
        verify(roleAppUpdateService, times(1))
                .rejectApplication(eq(APPLICATION_ID));
    }

    @Test
    void putRejectApp_internalServiceError() throws Exception {

        doThrow(new InternalServiceException("ex", new HibernateException("ex")))
                .when(roleAppUpdateService).rejectApplication(eq(APPLICATION_ID));

        this.mvc.perform(put("/application/reject")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"applicationId\": \"" + APPLICATION_ID + "\", \"authorizerId\": \"1\"}"))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }


    @Test
    void putAppAsInReview_happyPath() throws Exception {

        this.mvc.perform(put("/application/inReview")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"applicationId\": \"" + APPLICATION_ID + "\", \"authorizerId\": \"1\"}"))
                .andDo(print())
                .andExpect(status().isNoContent());
        verify(roleAppUpdateService, times(1))
                .markApplicationAsInReview(eq(APPLICATION_ID));
    }

    @Test
    void putAppAsInReview_internalServiceError() throws Exception {

        doThrow(new InternalServiceException("ex", new HibernateException("ex")))
                .when(roleAppUpdateService).markApplicationAsInReview(eq(APPLICATION_ID));

        this.mvc.perform(put("/application/inReview")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"applicationId\": \"" + APPLICATION_ID + "\", \"authorizerId\": \"1\"}"))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    void putApplicationConfirm_happyPath() throws Exception {

        this.mvc.perform(put("/application/confirm")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"applicationId\": \"" + APPLICATION_ID + "\", \"authorizerId\": \"1\"}"))
                .andDo(print())
                .andExpect(status().isNoContent());
        verify(roleAppUpdateService, times(1))
                .confirmApplication(eq(APPLICATION_ID));
    }

    @Test
    void putApplicationConfirm_internalServiceError() throws Exception {

        doThrow(new InternalServiceException("ex", new HibernateException("ex")))
                .when(roleAppUpdateService).confirmApplication(eq(APPLICATION_ID));

        this.mvc.perform(put("/application/confirm")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"applicationId\": \"" + APPLICATION_ID + "\", \"authorizerId\": \"1\"}"))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }
}