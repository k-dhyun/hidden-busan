package kr.seenby.hidden_bussan.domain.user.service;

import kr.seenby.hidden_bussan.domain.user.dto.AuthResponse;
import kr.seenby.hidden_bussan.domain.user.dto.LoginRequest;
import kr.seenby.hidden_bussan.domain.user.dto.SignupRequest;
import kr.seenby.hidden_bussan.domain.user.dto.UserResponse;
import kr.seenby.hidden_bussan.domain.user.entity.User;
import kr.seenby.hidden_bussan.domain.user.repository.UserRepository;
import kr.seenby.hidden_bussan.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String DEFAULT_AVATAR_URL =
            "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=80&h=80&fit=crop&auto=format";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        User user = userRepository.save(User.create(
                request.email(),
                passwordEncoder.encode(request.password()),
                request.nickname(),
                DEFAULT_AVATAR_URL
        ));

        return new AuthResponse(UserResponse.from(user), jwtTokenProvider.createAccessToken(user));
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

        if (!passwordMatches(request.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        return new AuthResponse(UserResponse.from(user), jwtTokenProvider.createAccessToken(user));
    }

    private boolean passwordMatches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword)
                || rawPassword.equals(encodedPassword);
    }
}
