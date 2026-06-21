package kr.seenby.hidden_bussan.domain.passport.repository;

import java.util.Optional;
import kr.seenby.hidden_bussan.domain.mission.entity.Mission;
import kr.seenby.hidden_bussan.domain.passport.entity.MissionCompletion;
import kr.seenby.hidden_bussan.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionCompletionRepository extends JpaRepository<MissionCompletion, Long> {

    Optional<MissionCompletion> findByUserAndMission(User user, Mission mission);

    boolean existsByUserAndMission(User user, Mission mission);
}
