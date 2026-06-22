package kr.seenby.hidden_bussan.domain.user.controller;

import jakarta.validation.Valid;
import kr.seenby.hidden_bussan.domain.user.dto.AuthResponse;
import kr.seenby.hidden_bussan.domain.user.dto.LoginRequest;
import kr.seenby.hidden_bussan.domain.user.dto.SignupRequest;
import kr.seenby.hidden_bussan.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public AuthResponse signup(@Valid @RequestBody SignupRequest request) {
        return authService.signup(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
