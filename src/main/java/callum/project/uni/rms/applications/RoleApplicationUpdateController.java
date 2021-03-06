package callum.project.uni.rms.applications;

import callum.project.uni.rms.applications.model.request.RoleAppUpdateStatusReq;
import callum.project.uni.rms.applications.model.target.TargetApplication;
import callum.project.uni.rms.applications.service.RoleAppUpdateService;
import callum.project.uni.rms.applications.service.RoleApplicationService;
import callum.project.uni.rms.applications.service.ShortlistService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@Slf4j
@AllArgsConstructor
public class RoleApplicationUpdateController {

    private final RoleAppUpdateService roleAppUpdateService;
    private final RoleApplicationService roleApplicationService;
    private final ShortlistService shortlistService;

    @PutMapping("/application/shortlist")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void putApplicationShortlist(@RequestBody @NonNull RoleAppUpdateStatusReq req){
        TargetApplication app = roleApplicationService.retrieveApplicationById(req.getApplicationId());
        shortlistService.addUserToShortlist(app.getRoleId(), app.getApplicantId());
        roleAppUpdateService.shortlistApplication(app.getApplicationId());
    }

    @PutMapping("/application/reject")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void putRejectApp(@RequestBody @NonNull RoleAppUpdateStatusReq req) {
        roleAppUpdateService.rejectApplication(req.getApplicationId());
    }

    @PutMapping("/application/inReview")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void putAppAsInReview(@RequestBody @NonNull RoleAppUpdateStatusReq req) {
        roleAppUpdateService.markApplicationAsInReview(req.getApplicationId());
    }

    @PutMapping("/application/confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void putApplicationConfirm(@RequestBody @NonNull RoleAppUpdateStatusReq req){
        roleAppUpdateService.confirmApplication(req.getApplicationId());
    }
}
