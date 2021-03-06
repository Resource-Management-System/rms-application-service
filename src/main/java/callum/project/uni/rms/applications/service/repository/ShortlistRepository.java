package callum.project.uni.rms.applications.service.repository;

import callum.project.uni.rms.applications.model.source.Shortlist;
import callum.project.uni.rms.applications.model.source.ShortlistId;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShortlistRepository extends CrudRepository<Shortlist, ShortlistId> {

    List<Shortlist> findAllByRoleId(Long roleId);

    @Modifying(clearAutomatically = true)
    void deleteAllByRoleId(Long roleId);

    @Modifying(clearAutomatically = true)
    void deleteByRoleIdAndUserId(Long roleId, Long userId);

    List<Shortlist> findAllByUserId(Long userId);
}
