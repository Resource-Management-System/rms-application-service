package callum.project.uni.rms.applications.model.source;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "shortlist")
@IdClass(ShortlistId.class)
public class Shortlist {

    @Id
    @Column(name = "role_id")
    private Long roleId;

    @Id
    @Column(name = "user_id")
    private Long userId;
}
