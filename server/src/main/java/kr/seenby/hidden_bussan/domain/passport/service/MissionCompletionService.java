package kr.seenby.hidden_bussan.domain.passport.service;

import java.time.LocalDate;
import java.util.List;
import kr.seenby.hidden_bussan.domain.mission.entity.Mission;
import kr.seenby.hidden_bussan.domain.mission.repository.MissionRepository;
import kr.seenby.hidden_bussan.domain.passport.dto.MissionCompletionRequest;
import kr.seenby.hidden_bussan.domain.passport.dto.MissionCompletionResponse;
import kr.seenby.hidden_bussan.domain.passport.entity.MissionCompletion;
import kr.seenby.hidden_bussan.domain.passport.repository.MissionCompletionRepository;
import kr.seenby.hidden_bussan.domain.user.entity.User;
import kr.seenby.hidden_bussan.domain.user.repository.UserRepository;
import kr.seenby.hidden_bussan.global.util.IdParser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class MissionCompletionService {

    private static final String DEFAULT_STAMP_EMOJI = "🏆";

    private final MissionCompletionRepository missionCompletionRepository;
    private final UserRepository userRepository;
    private final MissionRepository missionRepository;

    @Transactional(readOnly = true)
    public List<MissionCompletionResponse> listCompletions(Long userId) {
        User user = getUser(userId);

        return missionCompletionRepository.findAllByUserOrderByCompletedAtDesc(user).stream()
                .map(MissionCompletionResponse::from)
                .toList();
    }

    @Transactional
    public MissionCompletionResponse completeMission(Long userId, MissionCompletionRequest request) {
        User user = getUser(userId);
        Mission mission = getMission(request.missionId());
        Long contentId = IdParser.parse(request.contentId(), "contentId");

        if (!mission.getPlace().getId().equals(contentId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "contentId does not match mission place");
        }

        String stampEmoji = normalizeStampEmoji(request.stampEmoji());
        LocalDate completedAt = LocalDate.now();

        MissionCompletion completion = missionCompletionRepository.findByUserAndMission(user, mission)
                .map(existing -> {
                    existing.updateCompletion(request.photoUrl(), stampEmoji, request.memo(), completedAt);
                    return existing;
                })
                .orElseGet(() -> missionCompletionRepository.save(MissionCompletion.create(
                        user,
                        mission,
                        null,
                        request.photoUrl(),
                        stampEmoji,
                        request.memo(),
                        completedAt
                )));

        return MissionCompletionResponse.from(completion);
    }

    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private Mission getMission(String id) {
        return missionRepository.findById(IdParser.parse(id, "missionId"))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mission not found"));
    }

    private String normalizeStampEmoji(String stampEmoji) {
        if (stampEmoji == null || stampEmoji.isBlank()) {
            return DEFAULT_STAMP_EMOJI;
        }

        return stampEmoji;
    }
}
