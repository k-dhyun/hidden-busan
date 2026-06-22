package kr.seenby.hidden_bussan.domain.favorite.controller;

import jakarta.validation.Valid;
import java.util.List;
import kr.seenby.hidden_bussan.domain.favorite.dto.FavoriteResponse;
import kr.seenby.hidden_bussan.domain.favorite.dto.FavoriteToggleRequest;
import kr.seenby.hidden_bussan.domain.favorite.service.FavoriteService;
import kr.seenby.hidden_bussan.global.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping
    public List<FavoriteResponse> listFavorites(@AuthenticationPrincipal Jwt jwt) {
        return favoriteService.listFavorites(AuthenticatedUser.id(jwt));
    }

    @PostMapping("/toggle")
    public List<FavoriteResponse> toggleFavorite(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody FavoriteToggleRequest request
    ) {
        return favoriteService.toggleFavorite(AuthenticatedUser.id(jwt), request);
    }
}
