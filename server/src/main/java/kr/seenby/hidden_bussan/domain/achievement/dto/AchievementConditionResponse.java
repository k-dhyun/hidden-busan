package kr.seenby.hidden_bussan.domain.achievement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import kr.seenby.hidden_bussan.domain.achievement.entity.Achievement;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AchievementConditionResponse(
        String type,
        Object value,
        Integer total
) {

    public static AchievementConditionResponse from(Achievement achievement) {
        return new AchievementConditionResponse(
                achievement.getConditionType(),
                conditionValue(achievement),
                achievement.getConditionTotal()
        );
    }

    private static Object conditionValue(Achievement achievement) {
        if ("count_total".equals(achievement.getConditionType())) {
            try {
                return Integer.valueOf(achievement.getConditionValue());
            } catch (NumberFormatException ignored) {
                return achievement.getConditionValue();
            }
        }

        return achievement.getConditionValue();
    }
}
