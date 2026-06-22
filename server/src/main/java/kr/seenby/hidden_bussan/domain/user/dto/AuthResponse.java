package kr.seenby.hidden_bussan.domain.user.dto;

public record AuthResponse(
        UserResponse user,
        String token
) {
}
