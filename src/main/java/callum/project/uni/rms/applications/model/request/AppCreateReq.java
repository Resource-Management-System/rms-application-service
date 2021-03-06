package callum.project.uni.rms.applications.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class AppCreateReq {

    @NonNull
    @JsonProperty("applicantId")
    private Long applicantId;

    @NonNull
    @JsonProperty("roleId")
    private Long roleId;

    @NonNull
    @JsonProperty("projectCode")
    private String projectCode;

    @NonNull
    @JsonProperty("accountNumber")
    private String accountNumber;
}
