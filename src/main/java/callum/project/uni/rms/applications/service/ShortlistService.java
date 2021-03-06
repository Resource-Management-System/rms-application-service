package callum.project.uni.rms.applications.service;

import callum.project.uni.rms.applications.mapper.ShortlistMapper;
import callum.project.uni.rms.applications.model.source.Shortlist;
import callum.project.uni.rms.applications.model.target.TargetShortlist;
import callum.project.uni.rms.applications.model.target.TargetShortlistItem;
import callum.project.uni.rms.applications.service.repository.ShortlistRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class ShortlistService {

    private final ShortlistRepository shortlistRepository;
    private final ShortlistMapper shortlistMapper;

    public TargetShortlistItem addUserToShortlist(Long roleId, Long userId) {
        try {
            Shortlist shortlist = new Shortlist(roleId, userId);
            return shortlistMapper.mapSourceToTarget(shortlistRepository.save(shortlist));
        } catch (HibernateException e) {
            log.error(e.getMessage());
            throw new ServiceException("Error saving shortlist", e);
        }
    }

    public void removeShortlistItemForRole(Long roleId, Long userId) {
        try {
            shortlistRepository.deleteByRoleIdAndUserId(roleId, userId);
        } catch (HibernateException e) {
            log.error(e.getMessage());
            throw new ServiceException("Error retrieving candidates", e);
        }
    }

    public TargetShortlist retrieveShortlistForRole(Long roleId) {
        try {
            List<Shortlist> shortlist = shortlistRepository.findAllByRoleId(roleId);

            return buildTargetShortlist(shortlist);
        } catch (HibernateException e) {
            log.error(e.getMessage());
            throw new ServiceException("Error retrieving candidates", e);
        }
    }

    public void removeShortlistForRole(Long roleId) {
        try {
            shortlistRepository.findAllByRoleId(roleId);

        } catch (HibernateException e) {
            log.error(e.getMessage());
            throw new ServiceException("Error retrieving candidates", e);
        }
    }

    public TargetShortlist retrieveShortlistForUser(Long userId) {

        try {
            List<Shortlist> shortlist = shortlistRepository.findAllByUserId(userId);

            return buildTargetShortlist(shortlist);
        } catch (
                HibernateException e) {
            log.error(e.getMessage());
            throw new ServiceException("Error retrieving candidates", e);

        }
    }

    private TargetShortlist buildTargetShortlist(List<Shortlist> shortlist){
        return TargetShortlist.builder()
                .shortlist(shortlist.stream()
                        .map(shortlistMapper::mapSourceToTarget)
                        .collect(Collectors.toList()))
                .build();
    }
}
