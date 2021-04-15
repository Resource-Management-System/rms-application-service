package callum.project.uni.rms.applications.model.target;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TargetShortlist extends AbstractServiceResponse {
    
    List<TargetShortlistItem> shortlist;
}
