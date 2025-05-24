package volunteer.plus.backend.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import volunteer.plus.backend.domain.dto.Send2faResponseDto;
import volunteer.plus.backend.domain.dto.User2faConfigDto;
import volunteer.plus.backend.domain.dto.User2faSettingsDto;
import volunteer.plus.backend.domain.dto.Verify2faRequestDto;
import volunteer.plus.backend.domain.entity.User;
import volunteer.plus.backend.repository.User2faRepository;
import volunteer.plus.backend.service.security.impl.User2faService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class User2faController {

    private final User2faService user2faService;
    private final User2faRepository user2faRepository;

    @PutMapping("/2fa/settings")
    public ResponseEntity<User2faSettingsDto> updateSettings(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid User2faSettingsDto dto) {

        User2faSettingsDto updated = user2faService.updateSettings(user, dto);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/2fa/config")
    public ResponseEntity<User2faConfigDto> configure(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid User2faConfigDto dto) {

        User2faConfigDto updated = user2faService.configure2fa(user, dto);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/no-auth/2fa/send")
    @Transactional
    public ResponseEntity<Send2faResponseDto> sendCode(@RequestParam String code) {
        var user2fa = user2faRepository.findByVerificationId(code)
                .orElseThrow(() -> new RuntimeException("Cannot find token"));

        return ResponseEntity.ok(
                user2faService.send2faCode(user2fa.getUser().getUsername()));
    }

    @PostMapping("/no-auth/2fa/verify")
    public ResponseEntity<Void> verifyCode(
            @RequestBody @Valid Verify2faRequestDto dto) {

        user2faService.verify2faCode(dto);
        return ResponseEntity.ok().build();
    }

}
