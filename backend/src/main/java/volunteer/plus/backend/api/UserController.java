package volunteer.plus.backend.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import volunteer.plus.backend.domain.dto.RegistrationData;
import volunteer.plus.backend.domain.dto.UserInfo;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.service.general.UserService;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/user-info")
    public ResponseEntity<UserInfo> getUserInfo() throws AuthenticationException {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @PostMapping(value = "/user/general-info-update")
    @Operation(description = "Updating general fields of user")
    public ResponseEntity<UserInfo> generalInfoUpdate(@AuthenticationPrincipal final User user,
                                                      @RequestBody final RegistrationData registrationData) {
        return ResponseEntity.ok(userService.generalInfoUpdate(user, registrationData));
    }

    @PostMapping(value = "/user/upload-logo")
    @Operation(description = "Upload user logo")
    public ResponseEntity<UserInfo> uploadLogo(@AuthenticationPrincipal final User user,
                                               @RequestBody final MultipartFile file) {
        return ResponseEntity.ok(userService.uploadLogo(user, file));
    }

    @PostMapping(value = "/user/download-logo")
    @Operation(description = "Download user logo")
    public ResponseEntity<byte[]> downloadLogo(@AuthenticationPrincipal final User user) {
        return userService.downloadLogo(user);
    }

}
