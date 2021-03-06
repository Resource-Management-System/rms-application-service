package callum.project.uni.rms.applications.model.source;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ShortlistId implements Serializable {

    private Long roleId;
    private Long userId;
}
