package kr.seenby.hidden_bussan.domain.place.service;

import java.util.List;
import kr.seenby.hidden_bussan.domain.place.dto.ContentResponse;
import kr.seenby.hidden_bussan.domain.place.dto.DistrictResponse;
import kr.seenby.hidden_bussan.domain.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private static final List<DistrictResponse> DISTRICTS = List.of(
            new DistrictResponse("gangseo", "강서구", "강서구"),
            new DistrictResponse("sasang", "사상구", "사상구"),
            new DistrictResponse("buk", "북구", "북구"),
            new DistrictResponse("geumjeong", "금정구", "금정구"),
            new DistrictResponse("gijang", "기장군", "기장군"),
            new DistrictResponse("dongrae", "동래구", "동래구"),
            new DistrictResponse("yeonje", "연제구", "연제구"),
            new DistrictResponse("busanjin", "부산진구", "부산진구"),
            new DistrictResponse("haeundae", "해운대구", "해운대구"),
            new DistrictResponse("suyeong", "수영구", "수영구"),
            new DistrictResponse("nam", "남구", "남구"),
            new DistrictResponse("dong", "동구", "동구"),
            new DistrictResponse("saha", "사하구", "사하구"),
            new DistrictResponse("seo", "서구", "서구"),
            new DistrictResponse("jung", "중구", "중구"),
            new DistrictResponse("yeongdo", "영도구", "영도구")
    );

    private final PlaceRepository placeRepository;

    @Transactional(readOnly = true)
    public List<ContentResponse> listContents() {
        return placeRepository.findAll().stream()
                .map(ContentResponse::from)
                .toList();
    }

    public List<DistrictResponse> listDistricts() {
        return DISTRICTS;
    }
}
