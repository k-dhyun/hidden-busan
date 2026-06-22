package kr.seenby.hidden_bussan.domain.mission.dto;

import kr.seenby.hidden_bussan.domain.mission.entity.Mission;

public record MissionResponse(
        String id,
        String contentId,
        String title,
        String description
) {

    public static MissionResponse from(Mission mission) {
        return new MissionResponse(
                String.valueOf(mission.getId()),
                String.valueOf(mission.getPlace().getId()),
                mission.getTitle(),
                mission.getDescription()
        );
    }
}
