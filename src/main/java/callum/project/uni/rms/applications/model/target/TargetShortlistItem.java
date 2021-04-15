package callum.project.uni.rms.applications.model.target;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class TargetShortlistItem {

    private Long roleId;
    private Long userId;
}
