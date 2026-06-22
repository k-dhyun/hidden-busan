package kr.seenby.hidden_bussan.domain.achievement.service;

import java.util.List;
import kr.seenby.hidden_bussan.domain.achievement.dto.AchievementResponse;
import kr.seenby.hidden_bussan.domain.achievement.repository.AchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AchievementService {

    private final AchievementRepository achievementRepository;

    @Transactional(readOnly = true)
    public List<AchievementResponse> listAchievements() {
        return achievementRepository.findAll().stream()
                .map(AchievementResponse::from)
                .toList();
    }
}
