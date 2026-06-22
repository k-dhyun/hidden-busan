package kr.seenby.hidden_bussan.domain.passport.controller;

import jakarta.validation.Valid;
import java.util.List;
import kr.seenby.hidden_bussan.domain.passport.dto.MissionCompletionRequest;
import kr.seenby.hidden_bussan.domain.passport.dto.MissionCompletionResponse;
import kr.seenby.hidden_bussan.domain.passport.service.MissionCompletionService;
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
@RequestMapping("/api/mission-completions")
public class MissionCompletionController {

    private final MissionCompletionService missionCompletionService;

    @GetMapping
    public List<MissionCompletionResponse> listCompletions(@AuthenticationPrincipal Jwt jwt) {
        return missionCompletionService.listCompletions(AuthenticatedUser.id(jwt));
    }

    @PostMapping
    public MissionCompletionResponse completeMission(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody MissionCompletionRequest request
    ) {
        return missionCompletionService.completeMission(AuthenticatedUser.id(jwt), request);
    }
}
