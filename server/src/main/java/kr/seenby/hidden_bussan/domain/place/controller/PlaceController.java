package kr.seenby.hidden_bussan.domain.place.controller;

import java.util.List;
import kr.seenby.hidden_bussan.domain.place.dto.ContentResponse;
import kr.seenby.hidden_bussan.domain.place.dto.DistrictResponse;
import kr.seenby.hidden_bussan.domain.place.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping("/contents")
    public List<ContentResponse> listContents() {
        return placeService.listContents();
    }

    @GetMapping("/districts")
    public List<DistrictResponse> listDistricts() {
        return placeService.listDistricts();
    }
}
