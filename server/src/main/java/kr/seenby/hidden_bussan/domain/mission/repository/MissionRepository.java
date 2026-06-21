package kr.seenby.hidden_bussan.domain.mission.repository;

import java.util.Optional;
import kr.seenby.hidden_bussan.domain.mission.entity.Mission;
import kr.seenby.hidden_bussan.domain.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionRepository extends JpaRepository<Mission, Long> {

    Optional<Mission> findByPlace(Place place);

    boolean existsByPlace(Place place);
}
