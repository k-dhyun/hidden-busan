package kr.seenby.hidden_bussan.domain.share.controller;

import kr.seenby.hidden_bussan.domain.share.dto.CollectionShareResponse;
import kr.seenby.hidden_bussan.domain.share.service.ShareService;
import kr.seenby.hidden_bussan.global.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shares")
public class ShareController {

    private final ShareService shareService;

    @PostMapping
    public CollectionShareResponse getOrCreateShare(@AuthenticationPrincipal Jwt jwt) {
        return shareService.getOrCreateShare(AuthenticatedUser.id(jwt));
    }
}
