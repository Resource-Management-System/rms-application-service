package callum.project.uni.rms.applications.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class RoleAppUpdateStatusReq {

    @JsonProperty("applicationId")
    private Long applicationId;

    @JsonProperty("authorizerId")
    private Long authorizerId;

}
