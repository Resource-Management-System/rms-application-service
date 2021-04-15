package callum.project.uni.rms.applications.model.target;

import callum.project.uni.rms.applications.model.target.AbstractServiceResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class HasUserAlreadyApplied extends AbstractServiceResponse {

    private boolean userAlreadyApplied;
}
