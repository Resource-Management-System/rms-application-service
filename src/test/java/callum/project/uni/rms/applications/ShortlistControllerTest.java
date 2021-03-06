package callum.project.uni.rms.applications;

import callum.project.uni.rms.applications.model.target.TargetShortlistItem;
import callum.project.uni.rms.applications.service.ShortlistService;
import callum.project.uni.rms.parent.exception.InternalServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.HibernateException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static callum.project.uni.rms.applications.helper.TargetApplicationBuilder.APPLICANT_ID;
import static callum.project.uni.rms.applications.helper.TargetApplicationBuilder.ROLE_ID;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShortlistController.class)
class ShortlistControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ShortlistService shortlistService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void postCandidateToShortlist_happyPath() throws Exception {
        String json = "{" +
                " \"roleId\": \"" + ROLE_ID.toString() + "\"," +
                " \"candidateId\": \"" + APPLICANT_ID.toString() + "\" " +
                "}";

        when(shortlistService.addUserToShortlist(eq(ROLE_ID), eq(APPLICANT_ID)))
                .thenReturn(TargetShortlistItem.builder()
                        .roleId(ROLE_ID)
                        .userId(APPLICANT_ID)
                        .build());

        this.mvc.perform(post("/shortlist/candidate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isCreated());
        verify(shortlistService, times(1))
                .addUserToShortlist(eq(ROLE_ID), eq(APPLICANT_ID));
    }

    @Test
    void postCandidateToShortlist_internalServerError() throws Exception {
        String json = "{" +
                " \"roleId\": \"" + ROLE_ID.toString() + "\"," +
                " \"candidateId\": \"" + APPLICANT_ID.toString() + "\" " +
                "}";

        doThrow(new InternalServiceException("ex", new HibernateException("ex")))
                .when(shortlistService).addUserToShortlist(eq(ROLE_ID), eq(APPLICANT_ID));

        this.mvc.perform(post("/shortlist/candidate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isInternalServerError());
        verify(shortlistService, times(1))
                .addUserToShortlist(eq(ROLE_ID), eq(APPLICANT_ID));
    }

}