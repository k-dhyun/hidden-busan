package kr.seenby.hidden_bussan.domain.achievement.repository;

import java.util.Optional;
import kr.seenby.hidden_bussan.domain.achievement.entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {

    Optional<Achievement> findByTitle(String title);
}
