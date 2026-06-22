package kr.seenby.hidden_bussan.domain.favorite.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import kr.seenby.hidden_bussan.domain.favorite.entity.Favorite;

public record FavoriteResponse(
        String id,
        String userId,
        String contentId,
        String createdAt
) {

    public static FavoriteResponse from(Favorite favorite) {
        return new FavoriteResponse(
                String.valueOf(favorite.getId()),
                String.valueOf(favorite.getUser().getId()),
                String.valueOf(favorite.getPlace().getId()),
                formatDate(favorite.getCreatedAt())
        );
    }

    private static String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return LocalDate.now().toString();
        }

        return dateTime.toLocalDate().toString();
    }
}
