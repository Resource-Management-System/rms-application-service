package callum.project.uni.rms.applications.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class ShortlistCreateReq {

    @JsonProperty("roleId")
    private Long roleId;

    @JsonProperty("candidateId")
    private Long candidateId;
}
