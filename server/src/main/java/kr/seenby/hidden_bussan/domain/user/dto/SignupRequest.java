package kr.seenby.hidden_bussan.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignupRequest(
        @NotBlank String nickname,
        @Email @NotBlank String email,
        @NotBlank String password
) {
}
