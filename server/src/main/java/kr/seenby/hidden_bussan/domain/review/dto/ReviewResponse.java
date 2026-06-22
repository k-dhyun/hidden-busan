package kr.seenby.hidden_bussan.domain.review.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import kr.seenby.hidden_bussan.domain.review.entity.Review;

public record ReviewResponse(
        String id,
        String userId,
        String contentId,
        int rating,
        String comment,
        String createdAt
) {

    public static ReviewResponse from(Review review) {
        return new ReviewResponse(
                String.valueOf(review.getId()),
                String.valueOf(review.getUser().getId()),
                String.valueOf(review.getPlace().getId()),
                review.getRating(),
                review.getComment(),
                formatDate(review.getCreatedAt())
        );
    }

    private static String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return LocalDate.now().toString();
        }

        return dateTime.toLocalDate().toString();
    }
}
