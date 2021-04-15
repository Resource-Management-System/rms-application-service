package callum.project.uni.rms.applications.model.target;

import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NumOfApps extends AbstractServiceResponse {

    private Integer numOfApplications;
}
