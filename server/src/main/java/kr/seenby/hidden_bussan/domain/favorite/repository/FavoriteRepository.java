package kr.seenby.hidden_bussan.domain.favorite.repository;

import java.util.Optional;
import kr.seenby.hidden_bussan.domain.favorite.entity.Favorite;
import kr.seenby.hidden_bussan.domain.place.entity.Place;
import kr.seenby.hidden_bussan.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findByUserAndPlace(User user, Place place);

    boolean existsByUserAndPlace(User user, Place place);
}
