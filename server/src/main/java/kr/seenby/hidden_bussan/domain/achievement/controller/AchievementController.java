package kr.seenby.hidden_bussan.domain.achievement.controller;

import java.util.List;
import kr.seenby.hidden_bussan.domain.achievement.dto.AchievementResponse;
import kr.seenby.hidden_bussan.domain.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/achievements")
public class AchievementController {

    private final AchievementService achievementService;

    @GetMapping
    public List<AchievementResponse> listAchievements() {
        return achievementService.listAchievements();
    }
}
