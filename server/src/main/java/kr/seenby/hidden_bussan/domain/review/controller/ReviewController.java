package kr.seenby.hidden_bussan.domain.review.controller;

import jakarta.validation.Valid;
import java.util.List;
import kr.seenby.hidden_bussan.domain.review.dto.ReviewCreateRequest;
import kr.seenby.hidden_bussan.domain.review.dto.ReviewResponse;
import kr.seenby.hidden_bussan.domain.review.service.ReviewService;
import kr.seenby.hidden_bussan.global.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contents/{contentId}/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public List<ReviewResponse> listReviews(@PathVariable String contentId) {
        return reviewService.listReviews(contentId);
    }

    @PostMapping
    public ReviewResponse addReview(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String contentId,
            @Valid @RequestBody ReviewCreateRequest request
    ) {
        return reviewService.addReview(AuthenticatedUser.id(jwt), contentId, request);
    }
}
