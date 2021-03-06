package callum.project.uni.rms.applications.model.source;

import callum.project.uni.rms.applications.model.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "role_application", indexes = {
        @Index(name = "id_index", columnList = "application_id")
})
public class RoleApplication {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Long applicationId;

    @Column(name = "applicant_id")
    private Long applicantId;
    
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "project_id")
    private String projectId;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "application_date")
    private Date applicationDate;

    @Column(name = "last_updated_date")
    private Date lastUpdatedDate;

    @Column(name = "application_status")
    private ApplicationStatus applicationStatus;
}
