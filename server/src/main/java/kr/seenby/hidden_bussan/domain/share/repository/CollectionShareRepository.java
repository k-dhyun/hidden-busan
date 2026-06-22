package kr.seenby.hidden_bussan.domain.share.repository;

import java.util.Optional;
import kr.seenby.hidden_bussan.domain.share.entity.CollectionShare;
import kr.seenby.hidden_bussan.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionShareRepository extends JpaRepository<CollectionShare, Long> {

    Optional<CollectionShare> findFirstByUserOrderByCreatedAtDesc(User user);

    Optional<CollectionShare> findByUser(User user);

    Optional<CollectionShare> findByShareToken(String shareToken);

    boolean existsByShareToken(String shareToken);
}
