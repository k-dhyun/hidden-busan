package kr.seenby.hidden_bussan.domain.passport.controller;

import kr.seenby.hidden_bussan.domain.passport.dto.MissionPhotoUploadResponse;
import kr.seenby.hidden_bussan.domain.passport.service.MissionPhotoStorageService;
import kr.seenby.hidden_bussan.global.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mission-completions/photos")
public class MissionPhotoController {

    private final MissionPhotoStorageService missionPhotoStorageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MissionPhotoUploadResponse uploadPhoto(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam("file") MultipartFile file
    ) {
        AuthenticatedUser.id(jwt);
        return missionPhotoStorageService.store(file);
    }
}
