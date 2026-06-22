package kr.seenby.hidden_bussan.domain.place.repository;

import java.util.Collection;
import java.util.Optional;
import kr.seenby.hidden_bussan.domain.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    Optional<Place> findFirstByNameIn(Collection<String> names);
}
