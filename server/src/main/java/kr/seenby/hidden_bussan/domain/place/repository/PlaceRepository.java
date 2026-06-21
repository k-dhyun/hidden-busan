package kr.seenby.hidden_bussan.domain.place.repository;

import kr.seenby.hidden_bussan.domain.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
}
