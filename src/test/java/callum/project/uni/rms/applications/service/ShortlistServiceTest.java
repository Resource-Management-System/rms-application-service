package callum.project.uni.rms.applications.service;

import callum.project.uni.rms.applications.mapper.ShortlistMapper;
import callum.project.uni.rms.applications.model.source.Shortlist;
import callum.project.uni.rms.applications.model.target.TargetShortlist;
import callum.project.uni.rms.applications.model.target.TargetShortlistItem;
import callum.project.uni.rms.applications.service.repository.ShortlistRepository;
import callum.project.uni.rms.parent.exception.InternalServiceException;
import org.hibernate.HibernateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static callum.project.uni.rms.applications.helper.TargetApplicationBuilder.APPLICANT_ID;
import static callum.project.uni.rms.applications.helper.TargetApplicationBuilder.ROLE_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ShortlistServiceTest {

    private ShortlistService shortlistService;
    private ShortlistRepository shortlistRepository;

    @BeforeEach
    void setUp() {
        shortlistRepository = mock(ShortlistRepository.class);
        shortlistService = new ShortlistService(shortlistRepository, new ShortlistMapper());
    }

    @Test
    void addUserToShortlist() {
        when(shortlistRepository.save(eq(buildSourceShortlist())))
                .thenReturn(buildSourceShortlist());
        TargetShortlistItem shortlist = shortlistService.addUserToShortlist(ROLE_ID, APPLICANT_ID);
        assertEquals(APPLICANT_ID, shortlist.getUserId());
        assertEquals(ROLE_ID, shortlist.getRoleId());
    }

    @Test
    void addUserToShortlist_serverError() {
        when(shortlistRepository.save(eq(buildSourceShortlist())))
                .thenThrow(HibernateException.class);

        assertThrows(InternalServiceException.class,
                () -> shortlistService.addUserToShortlist(ROLE_ID, APPLICANT_ID));
    }

    @Test
    void removeShortlistItemForRole() {
        shortlistService.removeShortlistItemForRole(ROLE_ID, APPLICANT_ID);
        verify(shortlistRepository).deleteByRoleIdAndUserId(eq(ROLE_ID), eq(APPLICANT_ID));
    }

    @Test
    void removeShortlistItemForRole_serverError() {

        doThrow(new HibernateException("error"))
                .when(shortlistRepository)
                .deleteByRoleIdAndUserId(eq(ROLE_ID), eq(APPLICANT_ID));

        assertThrows(InternalServiceException.class,
                () -> shortlistService.removeShortlistItemForRole(ROLE_ID, APPLICANT_ID));
    }

    @Test
    void retrieveShortlistForRole() {
        when(shortlistRepository.findAllByRoleId(eq(ROLE_ID)))
                .thenReturn(Collections.singletonList(buildSourceShortlist()));
        TargetShortlist res = shortlistService.retrieveShortlistForRole(ROLE_ID);
        assertEquals(1, res.getShortlist().size());
        TargetShortlistItem item = res.getShortlist().get(0);
        assertEquals(ROLE_ID, item.getRoleId());
        assertEquals(APPLICANT_ID, item.getUserId());
    }

    @Test
    void retrieveShortlistForRole_serverError() {
        when(shortlistRepository.findAllByRoleId(eq(ROLE_ID)))
                .thenThrow(HibernateException.class);
        assertThrows(InternalServiceException.class,
                () -> shortlistService.retrieveShortlistForRole(ROLE_ID));
    }

    @Test
    void removeShortlistForRole() {
        shortlistService.removeShortlistForRole(ROLE_ID);
        verify(shortlistRepository, times(1)).deleteAllByRoleId(eq(ROLE_ID));
    }

    @Test
    void removeShortlistForRole_serverError() {

        doThrow(new HibernateException("ex"))
                .when(shortlistRepository)
                .deleteAllByRoleId(eq(ROLE_ID));
        assertThrows(InternalServiceException.class, () -> shortlistService.removeShortlistForRole(ROLE_ID));
    }

    @Test
    void retrieveShortlistForUser() {
        when(shortlistRepository.findAllByUserId(eq(APPLICANT_ID)))
                .thenReturn(Collections.singletonList(buildSourceShortlist()));
        TargetShortlist res = shortlistService.retrieveShortlistForUser(APPLICANT_ID);
        assertEquals(1, res.getShortlist().size());
        TargetShortlistItem item = res.getShortlist().get(0);
        assertEquals(ROLE_ID, item.getRoleId());
        assertEquals(APPLICANT_ID, item.getUserId());
    }

    @Test
    void retrieveShortlistForUser_serverError() {
        when(shortlistRepository.findAllByRoleId(eq(APPLICANT_ID)))
                .thenThrow(HibernateException.class);
        assertThrows(InternalServiceException.class,
                () -> shortlistService.retrieveShortlistForRole(APPLICANT_ID));
    }

    private Shortlist buildSourceShortlist(){
        return Shortlist.builder()
                .userId(APPLICANT_ID)
                .roleId(ROLE_ID)
                .build();
    }
}