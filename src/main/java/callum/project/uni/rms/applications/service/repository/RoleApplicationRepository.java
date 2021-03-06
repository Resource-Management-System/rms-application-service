package callum.project.uni.rms.applications.service.repository;

import callum.project.uni.rms.applications.model.source.RoleApplication;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoleApplicationRepository extends CrudRepository<RoleApplication, Long> {

    int countDistinctByApplicantId(Long applicantId);
    
    List<RoleApplication> findAllByApplicantId(Long applicantId);

    @Modifying(clearAutomatically = true)
    @Query(value = "update role_application ra set ra.application_status = :applicationStatus, ra.last_updated_date = :currentDate  where ra.application_id = :appId", nativeQuery = true)
    void updateApplicationStatus(@Param("appId") Long appId,
                                 @Param("currentDate") Date currentDate,
                                 @Param("applicationStatus") int applicationStatus);

    Optional<RoleApplication> findFirstByRoleIdAndApplicantId(Long roleId, Long applicantId);

    @Query(value = "SELECT ra.* FROM role_application ra inner join role r on r.id = ra.role_id  inner join resource_manager rm on rm.user_id = :rmId where r.business_unit_id = rm.business_unit_id", nativeQuery = true)
    List<RoleApplication> findAllByResourceManager(@Param("rmId") Long rmId);
    
    Optional<RoleApplication> findByRoleIdAndApplicantId(Long roleId, Long applicantId);
}
