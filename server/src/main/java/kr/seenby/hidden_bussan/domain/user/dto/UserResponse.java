package kr.seenby.hidden_bussan.domain.user.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import kr.seenby.hidden_bussan.domain.user.entity.User;

public record UserResponse(
        String id,
        String nickname,
        String email,
        String avatar,
        String createdAt
) {

    public static UserResponse from(User user) {
        return new UserResponse(
                String.valueOf(user.getId()),
                user.getNickname(),
                user.getEmail(),
                user.getAvatarUrl(),
                formatDate(user.getCreatedAt())
        );
    }

    private static String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return LocalDate.now().toString();
        }

        return dateTime.toLocalDate().toString();
    }
}
