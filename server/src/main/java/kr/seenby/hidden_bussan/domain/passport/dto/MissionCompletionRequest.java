package kr.seenby.hidden_bussan.domain.passport.dto;

import jakarta.validation.constraints.NotBlank;

public record MissionCompletionRequest(
        @NotBlank String missionId,
        @NotBlank String contentId,
        @NotBlank String photoUrl,
        String stampEmoji,
        @NotBlank String memo
) {
}
