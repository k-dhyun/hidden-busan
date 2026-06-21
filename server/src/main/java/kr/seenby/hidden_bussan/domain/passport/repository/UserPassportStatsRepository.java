package kr.seenby.hidden_bussan.domain.passport.repository;

import kr.seenby.hidden_bussan.domain.passport.entity.UserPassportStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPassportStatsRepository extends JpaRepository<UserPassportStats, Long> {
}
