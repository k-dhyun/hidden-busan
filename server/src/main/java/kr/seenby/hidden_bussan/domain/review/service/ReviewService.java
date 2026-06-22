package kr.seenby.hidden_bussan.domain.review.service;

import java.util.List;
import kr.seenby.hidden_bussan.domain.place.entity.Place;
import kr.seenby.hidden_bussan.domain.place.repository.PlaceRepository;
import kr.seenby.hidden_bussan.domain.review.dto.ReviewCreateRequest;
import kr.seenby.hidden_bussan.domain.review.dto.ReviewResponse;
import kr.seenby.hidden_bussan.domain.review.entity.Review;
import kr.seenby.hidden_bussan.domain.review.repository.ReviewRepository;
import kr.seenby.hidden_bussan.domain.user.entity.User;
import kr.seenby.hidden_bussan.domain.user.repository.UserRepository;
import kr.seenby.hidden_bussan.global.util.IdParser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    @Transactional(readOnly = true)
    public List<ReviewResponse> listReviews(String contentId) {
        Place place = getPlace(contentId);

        return reviewRepository.findAllByPlaceOrderByCreatedAtDesc(place).stream()
                .map(ReviewResponse::from)
                .toList();
    }

    @Transactional
    public ReviewResponse addReview(Long userId, String contentId, ReviewCreateRequest request) {
        if (request.contentId() != null && !request.contentId().isBlank() && !request.contentId().equals(contentId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Path contentId and body contentId do not match");
        }

        User user = getUser(userId);
        Place place = getPlace(contentId);
        Review review = reviewRepository.save(Review.create(user, place, request.rating(), request.comment()));

        return ReviewResponse.from(review);
    }

    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private Place getPlace(String id) {
        return placeRepository.findById(IdParser.parse(id, "contentId"))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Place not found"));
    }
}
