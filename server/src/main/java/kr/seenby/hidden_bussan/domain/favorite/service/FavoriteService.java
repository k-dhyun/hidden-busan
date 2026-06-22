package kr.seenby.hidden_bussan.domain.favorite.service;

import java.util.List;
import kr.seenby.hidden_bussan.domain.favorite.dto.FavoriteResponse;
import kr.seenby.hidden_bussan.domain.favorite.dto.FavoriteToggleRequest;
import kr.seenby.hidden_bussan.domain.favorite.entity.Favorite;
import kr.seenby.hidden_bussan.domain.favorite.repository.FavoriteRepository;
import kr.seenby.hidden_bussan.domain.place.entity.Place;
import kr.seenby.hidden_bussan.domain.place.repository.PlaceRepository;
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
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    @Transactional(readOnly = true)
    public List<FavoriteResponse> listFavorites(Long userId) {
        User user = getUser(userId);

        return favoriteRepository.findAllByUserOrderByCreatedAtDesc(user).stream()
                .map(FavoriteResponse::from)
                .toList();
    }

    @Transactional
    public List<FavoriteResponse> toggleFavorite(Long userId, FavoriteToggleRequest request) {
        User user = getUser(userId);
        Place place = getPlace(request.contentId());

        favoriteRepository.findByUserAndPlace(user, place)
                .ifPresentOrElse(
                        favoriteRepository::delete,
                        () -> favoriteRepository.save(Favorite.create(user, place))
                );

        return favoriteRepository.findAllByUserOrderByCreatedAtDesc(user).stream()
                .map(FavoriteResponse::from)
                .toList();
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
