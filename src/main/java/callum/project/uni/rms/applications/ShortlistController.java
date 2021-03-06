package callum.project.uni.rms.applications;

import callum.project.uni.rms.applications.model.request.ShortlistCreateReq;
import callum.project.uni.rms.applications.model.target.TargetShortlist;
import callum.project.uni.rms.applications.model.target.TargetShortlistItem;
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
public class ShortlistController {

    private final ShortlistService shortlistService;

    @PostMapping("/shortlist/candidate")
    @ResponseStatus(HttpStatus.CREATED)
    public TargetShortlistItem postCandidateToShortlist(@RequestBody @NonNull ShortlistCreateReq req) {
        return shortlistService.addUserToShortlist(req.getRoleId(), req.getCandidateId());
    }

    @DeleteMapping("/shortlist/reject/user/{userId}/role/{roleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserFromRoleShortlist(@PathVariable("userId") @NonNull Long userId,
                                            @PathVariable("roleId") @NonNull Long roleId) {
        shortlistService.removeShortlistItemForRole(roleId, userId);
    }

    @PutMapping("/shortlist/confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void putConfirmApp(@RequestBody @NonNull ShortlistCreateReq req){
        shortlistService.removeShortlistForRole(req.getRoleId());
    }

    @GetMapping(value = "/shortlist", params = {"roleId"})
    @ResponseStatus(HttpStatus.OK)
    public TargetShortlist getCandidatesOnShortlist(@NonNull @RequestParam Long roleId) {
        return shortlistService.retrieveShortlistForRole(roleId);
    }


    @GetMapping(value = "/shortlist", params = {"userId"})
    @ResponseStatus(HttpStatus.OK)
    public TargetShortlist getShortlistsForUser(@NonNull @RequestParam Long userId) {
        return shortlistService.retrieveShortlistForUser(userId);
    }

}
