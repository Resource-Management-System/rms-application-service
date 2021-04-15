package callum.project.uni.rms.applications.model.target;

import lombok.*;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationsForUser extends AbstractServiceResponse {
    
    private List<TargetApplication> applicationInfoList;
}
