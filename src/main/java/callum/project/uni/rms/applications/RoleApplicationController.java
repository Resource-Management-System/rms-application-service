package callum.project.uni.rms.applications;

import callum.project.uni.rms.applications.model.request.AppCreateReq;
import callum.project.uni.rms.applications.model.target.AbstractServiceResponse;
import callum.project.uni.rms.applications.service.RoleApplicationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@Slf4j
@AllArgsConstructor
class RoleApplicationController {

    private final RoleApplicationService roleApplicationService;

    @GetMapping(value = "/applications", params = "userId")
    @ResponseStatus(HttpStatus.OK)
    public AbstractServiceResponse getApplicationForUser(@RequestParam @NonNull Long userId) {
        return roleApplicationService.retrieveAllApplicationsForUser(userId);
    }

    @GetMapping(value = "/applications", params = "rmId")
    @ResponseStatus(HttpStatus.OK)
    public AbstractServiceResponse getApplicationsForRm(@RequestParam @NonNull  Long rmId) {
        return roleApplicationService.retrieveApplicationsForRm(rmId);
    }

    @GetMapping("/applications/{roleId}/userAlreadyApplied")
    @ResponseStatus(HttpStatus.OK)
    public AbstractServiceResponse getUserAlreadyApplied(@RequestParam @NonNull Long userId,
                                                         @PathVariable @NonNull Long roleId) {
        return roleApplicationService.userAlreadyApplied(roleId, userId);
    }

    @GetMapping("/application/role/{roleId}/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public AbstractServiceResponse getUserByRoleIdAndUserId(@PathVariable @NonNull Long userId,
                                                            @PathVariable @NonNull Long roleId){
        return roleApplicationService.retrieveApplicationByRoleAndUser(roleId,userId);
    }

    @GetMapping("/applications/amount")
    @ResponseStatus(HttpStatus.OK)
    public AbstractServiceResponse getNumberOfApplications(@RequestParam String userId) {
        return roleApplicationService.retrieveNumOfApplicationsForUser(Long.parseLong(userId));
    }

    @PostMapping("/application/submit")
    @ResponseStatus(HttpStatus.CREATED)
    public AbstractServiceResponse postNewApp(@RequestBody @NonNull AppCreateReq request) {
        return roleApplicationService.addNewApplication(request);
    }
}
