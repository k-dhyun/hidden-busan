package kr.seenby.hidden_bussan.domain.mission.service;

import java.util.List;
import kr.seenby.hidden_bussan.domain.mission.dto.MissionResponse;
import kr.seenby.hidden_bussan.domain.mission.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;

    @Transactional(readOnly = true)
    public List<MissionResponse> listMissions() {
        return missionRepository.findAll().stream()
                .map(MissionResponse::from)
                .toList();
    }
}
