package kr.seenby.hidden_bussan.domain.achievement.repository;

import java.util.Optional;
import kr.seenby.hidden_bussan.domain.achievement.entity.Achievement;
import kr.seenby.hidden_bussan.domain.achievement.entity.UserAchievement;
import kr.seenby.hidden_bussan.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {

    Optional<UserAchievement> findByUserAndAchievement(User user, Achievement achievement);

    boolean existsByUserAndAchievement(User user, Achievement achievement);
}
