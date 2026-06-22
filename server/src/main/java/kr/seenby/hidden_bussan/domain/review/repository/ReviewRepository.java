package kr.seenby.hidden_bussan.domain.review.repository;

import java.util.List;
import kr.seenby.hidden_bussan.domain.place.entity.Place;
import kr.seenby.hidden_bussan.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByPlaceOrderByCreatedAtDesc(Place place);
}
