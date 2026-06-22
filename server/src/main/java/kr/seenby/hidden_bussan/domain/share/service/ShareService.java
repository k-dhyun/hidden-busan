package kr.seenby.hidden_bussan.domain.share.service;

import java.util.UUID;
import kr.seenby.hidden_bussan.domain.share.dto.CollectionShareResponse;
import kr.seenby.hidden_bussan.domain.share.entity.CollectionShare;
import kr.seenby.hidden_bussan.domain.share.repository.CollectionShareRepository;
import kr.seenby.hidden_bussan.domain.user.entity.User;
import kr.seenby.hidden_bussan.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ShareService {

    private static final String SHARE_URL_PREFIX = "https://hiddenbusan.kr/share/";

    private final CollectionShareRepository collectionShareRepository;
    private final UserRepository userRepository;

    @Transactional
    public CollectionShareResponse getOrCreateShare(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        CollectionShare share = collectionShareRepository.findFirstByUserOrderByCreatedAtDesc(user)
                .orElseGet(() -> collectionShareRepository.save(createShare(user)));

        return CollectionShareResponse.from(share);
    }

    private CollectionShare createShare(User user) {
        String token = "passport-" + user.getId() + "-" + UUID.randomUUID().toString().substring(0, 8);
        return CollectionShare.create(user, token, SHARE_URL_PREFIX + token, true);
    }
}
