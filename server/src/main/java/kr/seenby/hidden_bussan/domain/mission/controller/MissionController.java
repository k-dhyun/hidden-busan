package kr.seenby.hidden_bussan.domain.mission.controller;

import java.util.List;
import kr.seenby.hidden_bussan.domain.mission.dto.MissionResponse;
import kr.seenby.hidden_bussan.domain.mission.service.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/missions")
public class MissionController {

    private final MissionService missionService;

    @GetMapping
    public List<MissionResponse> listMissions() {
        return missionService.listMissions();
    }
}
