package kr.seenby.hidden_bussan.domain.share.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import kr.seenby.hidden_bussan.domain.share.entity.CollectionShare;

public record CollectionShareResponse(
        String id,
        String userId,
        String shareToken,
        String shareUrl,
        String createdAt
) {

    public static CollectionShareResponse from(CollectionShare share) {
        return new CollectionShareResponse(
                String.valueOf(share.getId()),
                String.valueOf(share.getUser().getId()),
                share.getShareToken(),
                share.getShareUrl(),
                formatDate(share.getCreatedAt())
        );
    }

    private static String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return LocalDate.now().toString();
        }

        return dateTime.toLocalDate().toString();
    }
}
