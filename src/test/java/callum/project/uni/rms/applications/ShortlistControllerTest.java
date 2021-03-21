package callum.project.uni.rms.applications;

import callum.project.uni.rms.applications.model.request.ShortlistCreateReq;
import callum.project.uni.rms.applications.model.source.Shortlist;
import callum.project.uni.rms.applications.model.target.ApplicationsForUser;
import callum.project.uni.rms.applications.model.target.TargetApplication;
import callum.project.uni.rms.applications.model.target.TargetShortlist;
import callum.project.uni.rms.applications.model.target.TargetShortlistItem;
import callum.project.uni.rms.applications.service.ShortlistService;
import callum.project.uni.rms.parent.exception.InternalServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.HibernateException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static callum.project.uni.rms.applications.helper.TargetApplicationBuilder.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

        ResultActions resultActions = this.mvc.perform(post("/shortlist/candidate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isCreated());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        TargetShortlistItem res = objectMapper.readValue(contentAsString, TargetShortlistItem.class);

        assertEquals(APPLICANT_ID, res.getUserId());
        assertEquals(ROLE_ID, res.getRoleId());
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

    @Test
    void deleteUserFromRoleShortlist_happyPath() throws Exception {

        this.mvc.perform(delete("/shortlist/reject/user/" + APPLICANT_ID + "/role/" + ROLE_ID + ""))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(shortlistService, times(1))
                .removeShortlistItemForRole(eq(ROLE_ID), eq(APPLICANT_ID));
    }

    @Test
    void deleteUserFromRoleShortlist_serverError() throws Exception {

        doThrow(new InternalServiceException("ex", new HibernateException("ex")))
                .when(shortlistService).removeShortlistItemForRole(eq(ROLE_ID), eq(APPLICANT_ID));

        this.mvc.perform(delete("/shortlist/reject/user/" + APPLICANT_ID + "/role/" + ROLE_ID + ""))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    void putConfirmApp_happyPath() throws Exception {

        String json = "{" +
                " \"roleId\": \"" + ROLE_ID.toString() + "\"," +
                " \"candidateId\": \"" + APPLICANT_ID.toString() + "\" " +
                "}";
        this.mvc.perform(put("/shortlist/confirm")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isNoContent());


        verify(shortlistService, times(1))
                .removeShortlistForRole(eq(ROLE_ID));
    }

    @Test
    void putConfirmApp_serverError() throws Exception {

        doThrow(new InternalServiceException("ex", new HibernateException("ex")))
                .when(shortlistService).removeShortlistItemForRole(eq(ROLE_ID), eq(APPLICANT_ID));

        String json = "{" +
                " \"roleId\": \"" + ROLE_ID.toString() + "\"," +
                " \"candidateId\": \"" + APPLICANT_ID.toString() + "\" " +
                "}";

        this.mvc.perform(put("/shortlist/confirm")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isNoContent());


        verify(shortlistService, times(1))
                .removeShortlistForRole(eq(ROLE_ID));
    }


    @Test
    void getCandidatesOnShortlist_happyPath() throws Exception {

        when(shortlistService.retrieveShortlistForRole(eq(ROLE_ID)))
                .thenReturn(
                        TargetShortlist.builder()
                                .shortlist(
                                        List.of(TargetShortlistItem.builder()
                                                .roleId(ROLE_ID)
                                                .userId(APPLICANT_ID)
                                                .build()))
                                .build());

        ResultActions resultActions = this.mvc.perform(get("/shortlist")
                .queryParam("roleId", ROLE_ID.toString()))
                .andDo(print())
                .andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        TargetShortlist res = objectMapper.readValue(contentAsString, TargetShortlist.class);
        List<TargetShortlistItem> shortlist = res.getShortlist();
        TargetShortlistItem shortlistItem = shortlist.get(0);
        assertEquals(ROLE_ID, shortlistItem.getRoleId());
        assertEquals(APPLICANT_ID, shortlistItem.getUserId());
    }

    @Test
    void getCandidatesOnShortlist_internalServiceException() throws Exception {
        when(shortlistService.retrieveShortlistForRole(eq(ROLE_ID)))
                .thenThrow(InternalServiceException.class);

        this.mvc.perform(get("/shortlist")
                .queryParam("roleId", ROLE_ID.toString()))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getShortlistsForUser_happyPath() throws Exception {

        when(shortlistService.retrieveShortlistForUser(eq(APPLICANT_ID)))
                .thenReturn(
                        TargetShortlist.builder()
                                .shortlist(
                                        List.of(TargetShortlistItem.builder()
                                                .roleId(ROLE_ID)
                                                .userId(APPLICANT_ID)
                                                .build()))
                                .build());

        ResultActions resultActions = this.mvc.perform(get("/shortlist")
                .queryParam("userId", APPLICANT_ID.toString()))
                .andDo(print())
                .andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        TargetShortlist res = objectMapper.readValue(contentAsString, TargetShortlist.class);
        List<TargetShortlistItem> shortlist = res.getShortlist();
        TargetShortlistItem shortlistItem = shortlist.get(0);
        assertEquals(ROLE_ID, shortlistItem.getRoleId());
        assertEquals(APPLICANT_ID, shortlistItem.getUserId());
    }

    @Test
    void getShortlistsForUser_internalServiceException() throws Exception {
        when(shortlistService.retrieveShortlistForUser(eq(APPLICANT_ID)))
                .thenThrow(InternalServiceException.class);

        this.mvc.perform(get("/shortlist")
                .queryParam("userId", APPLICANT_ID.toString()))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getShortlistsForUser_badRequest() throws Exception {
        this.mvc.perform(get("/shortlist"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}