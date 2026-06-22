package kr.seenby.hidden_bussan.domain.favorite.dto;

import jakarta.validation.constraints.NotBlank;

public record FavoriteToggleRequest(
        @NotBlank String contentId
) {
}
