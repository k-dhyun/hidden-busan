package kr.seenby.hidden_bussan.domain.achievement.dto;

import kr.seenby.hidden_bussan.domain.achievement.entity.Achievement;

public record AchievementResponse(
        String id,
        String title,
        String description,
        String icon,
        AchievementConditionResponse condition
) {

    public static AchievementResponse from(Achievement achievement) {
        return new AchievementResponse(
                String.valueOf(achievement.getId()),
                achievement.getTitle(),
                achievement.getDescription(),
                achievement.getIcon(),
                AchievementConditionResponse.from(achievement)
        );
    }
}
