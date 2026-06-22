package kr.seenby.hidden_bussan.domain.passport.dto;

import kr.seenby.hidden_bussan.domain.passport.entity.MissionCompletion;

public record MissionCompletionResponse(
        String id,
        String userId,
        String missionId,
        String contentId,
        String photoUrl,
        String stampEmoji,
        String memo,
        String completedAt
) {

    public static MissionCompletionResponse from(MissionCompletion completion) {
        return new MissionCompletionResponse(
                String.valueOf(completion.getId()),
                String.valueOf(completion.getUser().getId()),
                String.valueOf(completion.getMission().getId()),
                String.valueOf(completion.getMission().getPlace().getId()),
                completion.getPhotoUrl(),
                completion.getStampEmoji(),
                completion.getMemo(),
                completion.getCompletedAt().toString()
        );
    }
}
