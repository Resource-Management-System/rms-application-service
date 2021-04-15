package callum.project.uni.rms.applications.model.target;

import callum.project.uni.rms.applications.model.target.AbstractServiceResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class ApplicationSubmitted extends AbstractServiceResponse {

    public boolean appSubmitted;
}
