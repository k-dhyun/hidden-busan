package kr.seenby.hidden_bussan.domain.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReviewCreateRequest(
        String contentId,
        @NotNull @Min(1) @Max(5) Integer rating,
        @NotBlank String comment
) {
}
