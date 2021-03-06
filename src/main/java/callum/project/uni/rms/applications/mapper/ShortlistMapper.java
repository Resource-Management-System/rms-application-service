package callum.project.uni.rms.applications.mapper;

import callum.project.uni.rms.applications.model.source.Shortlist;
import callum.project.uni.rms.applications.model.target.TargetShortlistItem;
import org.springframework.stereotype.Service;


@Service
public class ShortlistMapper {

    public TargetShortlistItem mapSourceToTarget(Shortlist source) {
        return new TargetShortlistItem(source.getRoleId(), source.getUserId());
    }
}
