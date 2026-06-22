package kr.seenby.hidden_bussan.global.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kr.seenby.hidden_bussan.domain.achievement.entity.Achievement;
import kr.seenby.hidden_bussan.domain.achievement.repository.AchievementRepository;
import kr.seenby.hidden_bussan.domain.mission.entity.Mission;
import kr.seenby.hidden_bussan.domain.mission.repository.MissionRepository;
import kr.seenby.hidden_bussan.domain.place.entity.Place;
import kr.seenby.hidden_bussan.domain.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MasterDataSeeder implements ApplicationRunner {

    private static final String VISIT_BUSAN = "https://www.visitbusan.net";

    private final PlaceRepository placeRepository;
    private final MissionRepository missionRepository;
    private final AchievementRepository achievementRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        seedPlacesAndMissions();
        seedAchievements();
    }

    private void seedPlacesAndMissions() {
        seedPlaces().forEach(this::upsertPlaceAndMission);
    }

    private void upsertPlaceAndMission(SeedPlace seed) {
        Place place = placeRepository.findFirstByNameIn(seed.lookupNames())
                .map(existing -> {
                    existing.update(
                            seed.region(),
                            seed.name(),
                            seed.category(),
                            seed.difficulty(),
                            seed.description(),
                            seed.address(),
                            seed.imageUrl(),
                            seed.duration(),
                            seed.bestTime()
                    );
                    return existing;
                })
                .orElseGet(() -> placeRepository.save(Place.create(
                        seed.region(),
                        seed.name(),
                        seed.category(),
                        seed.difficulty(),
                        seed.description(),
                        seed.address(),
                        seed.imageUrl(),
                        seed.duration(),
                        seed.bestTime()
                )));

        missionRepository.findByPlace(place)
                .ifPresentOrElse(
                        mission -> mission.update(seed.missionTitle(), seed.missionDescription()),
                        () -> missionRepository.save(Mission.create(place, seed.missionTitle(), seed.missionDescription()))
                );
    }

    private void seedAchievements() {
        seedAchievementData().forEach(seed -> achievementRepository.findByTitle(seed.title())
                .ifPresentOrElse(
                        achievement -> achievement.update(
                                seed.description(),
                                seed.icon(),
                                seed.conditionType(),
                                seed.conditionValue(),
                                seed.conditionTotal()
                        ),
                        () -> achievementRepository.save(Achievement.create(
                                seed.title(),
                                seed.description(),
                                seed.icon(),
                                seed.conditionType(),
                                seed.conditionValue(),
                                seed.conditionTotal()
                        ))
                ));
    }

    private List<SeedPlace> seedPlaces() {
        return List.of(
                place("남구", "이기대 해상산책로", "명소", "보통", "해안 절벽과 바다 전망을 따라 걷는 부산 대표 해안 산책로", "부산광역시 남구 용호동 산197-5", "https://upload.wikimedia.org/wikipedia/commons/3/37/Igidae_Trail_in_Busan.jpg", "약 2시간", "일출, 일몰", "해안 절경 포착", "바다가 가장 잘 보이는 포인트를 사진으로 기록하기"),
                place("사하구", "감천문화마을", "명소", "쉬움", "알록달록한 집과 골목길이 이어지는 부산의 대표 산복마을", "부산광역시 사하구 감내2로 203", "https://upload.wikimedia.org/wikipedia/commons/thumb/e/ee/Gamcheon_Houses%2C_2024.jpg/1280px-Gamcheon_Houses%2C_2024.jpg", "약 1.5시간", "오전 10시~오후 4시", "골목 탐험가", "가장 마음에 드는 골목과 벽화 앞에서 인증 사진 남기기"),
                place("중구", "자갈치시장", "시장", "쉬움", "부산의 바다 식문화를 가까이 볼 수 있는 대표 수산시장", "부산광역시 중구 자갈치해안로 52", "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Jagalchi_Market_20200523_019.jpg/1280px-Jagalchi_Market_20200523_019.jpg", "약 1시간", "오전 7시~오후 6시", "수산시장 탐방", "시장 풍경을 사진으로 남기고 한 줄 기록 작성하기"),
                place("해운대구", "해운대 블루라인파크", "명소", "쉬움", "해운대 해변과 동해남부선 폐선부지를 따라 이어지는 해안 관광 코스", "부산광역시 해운대구 청사포로 116", "https://upload.wikimedia.org/wikipedia/commons/thumb/e/ea/Haeundae_Beach_May_2024.jpg/1280px-Haeundae_Beach_May_2024.jpg", "약 1시간", "오후 2시~오후 6시", "스카이라인 수집", "해안열차나 스카이캡슐 풍경을 사진으로 남기기"),
                place("영도구", "태종대", "명소", "보통", "기암절벽과 등대, 해안 숲길이 어우러진 영도의 대표 해안 명소", "부산광역시 영도구 전망로 24", vb("/uploadImgs/files/cntnts/20191222180829962_thumbL"), "약 2시간", "오전 9시~오후 5시", "등대지기 체험", "태종대 등대와 바다를 함께 담은 사진 남기기", "태종대 유원지"),
                place("중구", "BIFF 광장", "문화", "쉬움", "부산국제영화제의 흔적과 먹거리 골목이 함께 있는 원도심 명소", "부산광역시 중구 비프광장로 36", "https://upload.wikimedia.org/wikipedia/commons/thumb/5/54/Busan_Cinema_Center_BIFF_2023.jpg/1280px-Busan_Cinema_Center_BIFF_2023.jpg", "약 1시간", "오후 1시~오후 9시", "영화 거리 인증", "광장 표식이나 핸드프린팅을 찾아 사진으로 기록하기", "BIFF"),
                place("중구", "부산국제시장", "시장", "쉬움", "피난민의 역사와 다양한 상점이 남아 있는 부산 원도심 시장", "부산광역시 중구 국제시장4길 10", "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8a/Gukje_Market%2C_October_2019_03.jpg/1280px-Gukje_Market%2C_October_2019_03.jpg", "약 1.5시간", "오전 10시~오후 7시", "시장 골목 기록", "시장 골목에서 기억에 남는 간판이나 상점을 사진으로 남기기"),
                place("수영구", "광안리 어방축제", "축제", "쉬움", "광안리 해변 일대에서 열리는 전통 어업 문화 축제", "부산광역시 수영구 광안해변로 219", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/17/Gwangalli_Beach.jpg/1280px-Gwangalli_Beach.jpg", "하루 종일", "4월~5월", "축제의 순간", "광안리 바다와 축제 분위기가 함께 보이는 사진 남기기"),
                place("부산진구", "서면 쇼핑거리", "쇼핑", "쉬움", "쇼핑, 음식, 카페가 밀집한 부산 대표 도심 상권", "부산광역시 부산진구 서면로 56", "https://upload.wikimedia.org/wikipedia/commons/thumb/8/84/Busan-IMGP7037.jpg/1280px-Busan-IMGP7037.jpg", "약 2시간", "낮 12시~오후 10시", "서면 쇼핑 인증", "서면 거리에서 발견한 취향의 장소를 사진으로 남기기"),
                place("서구", "암남공원", "명소", "보통", "송도 해안과 숲길을 함께 즐길 수 있는 서구의 해안 공원", "부산광역시 서구 암남공원로 185", vb("/uploadImgs/files/cntnts/20191227112720050_thumbL"), "약 1.5시간", "오전 10시~오후 5시", "해안 숲길 산책", "암남공원 산책로에서 바다가 보이는 지점을 사진으로 남기기", "해운대 암남공원 해녀촌"),
                place("동래구", "동래파전 거리", "맛집", "쉬움", "동래파전과 전통 음식점이 모여 있는 동래 대표 먹거리 거리", "부산광역시 동래구 명륜로 일대", vb("/uploadImgs/files/cntnts/20191230181126685_thumbL"), "약 1시간", "점심 11시~오후 5시", "동래의 맛 기록", "동래에서 맛본 음식이나 거리 풍경을 사진으로 남기기"),
                place("기장군", "기장 대변항 멸치축제", "축제", "쉬움", "기장 대변항에서 열리는 봄철 멸치 풍어 축제", "부산광역시 기장군 기장읍 대변리", vb("/uploadImgs/files/cntnts/20191222190823385_thumbL"), "하루 종일", "4월~5월", "항구 축제 기록", "대변항의 바다와 축제 분위기를 사진으로 남기기"),
                place("중구", "남포동 씨앗호떡", "맛집", "쉬움", "남포동과 BIFF 광장 일대에서 맛볼 수 있는 부산 대표 길거리 간식", "부산광역시 중구 남포동 광복로 일대", "https://upload.wikimedia.org/wikipedia/commons/5/54/Food_alley_in_Nampo-dong%2C_Busan_in_2003.jpg", "30분", "오전 10시~오후 9시", "달콤한 골목 기록", "남포동 먹거리 골목에서 간식 인증 사진 남기기"),
                place("금정구", "금정산성 막걸리 마을", "맛집", "보통", "금정산성 인근에서 산성막걸리와 향토 음식을 즐길 수 있는 마을", "부산광역시 금정구 산성마을 일대", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1f/Geumjeong_Fortress_North_Gate.jpg/1280px-Geumjeong_Fortress_North_Gate.jpg", "약 2시간", "주말 오전 11시~오후 4시", "산성마을 한 줄 기록", "산성마을에서 만난 풍경이나 음식을 사진으로 남기기"),
                place("사하구", "다대포 해수욕장", "명소", "쉬움", "낙동강 하구와 넓은 백사장이 만나는 부산 서쪽의 낙조 명소", "부산광역시 사하구 다대동", vb("/uploadImgs/files/cntnts/20191229143230630_thumbL"), "약 2시간", "일몰 시간", "낙조 수집", "다대포의 일몰이나 해변 풍경을 사진으로 남기기", "사하구 다대포 해수욕장"),
                place("영도구", "깡깡이예술마을", "명소", "쉬움", "수리조선소의 역사와 예술 골목이 남아 있는 영도 마을", "부산광역시 영도구 대평로27번길 일대", "https://upload.wikimedia.org/wikipedia/commons/0/03/Huinnyeoul_culture_village%2C_Busan_on_October_25th%2C_2019.jpg", "약 1.5시간", "오전 10시~오후 5시", "골목의 흔적 찾기", "영도의 골목 풍경과 바다를 함께 기록하기", "영도 깡깡이 마을"),
                place("부산진구", "부산시민공원", "공원", "쉬움", "옛 하야리아 부지에 조성된 부산 도심 대표 공원", "부산광역시 부산진구 시민공원로 73", vb("/uploadImgs/files/cntnts/20191227194942971_thumbL"), "약 2시간", "오전 9시~오후 6시", "도심 공원 산책", "공원에서 가장 좋았던 산책 지점을 사진으로 남기기", "부산진구 부산시민공원"),

                place("강서구", "맥도생태공원", "공원", "쉬움", "낙동강변을 따라 벚꽃길, 메타세쿼이아길, 수생식물원이 이어지는 생태공원", "부산광역시 강서구 공항로 500", vb("/uploadImgs/files/cntnts/20230713150615959_thumbL"), "약 1.5시간", "봄, 가을 오전", "생태길 산책", "맥도생태공원에서 가장 마음에 드는 산책길 사진 남기기"),
                place("강서구", "가덕도", "명소", "보통", "부산 서남단의 섬으로 바다 전망과 어촌 풍경, 연대봉 코스가 매력적인 지역", "부산광역시 강서구 가덕도 일대", vb("/uploadImgs/files/cntnts/20191227161822663_thumbL"), "반나절", "맑은 날 오전", "섬 풍경 수집", "가덕도에서 바다와 마을이 함께 보이는 사진 남기기"),
                place("사하구", "을숙도", "명소", "쉬움", "낙동강 하구의 철새와 문화공간을 함께 만날 수 있는 섬", "부산광역시 사하구 낙동남로 1240", vb("/uploadImgs/files/cntnts/20191226143325464_thumbL"), "약 2시간", "오전, 늦은 오후", "철새의 섬 기록", "을숙도에서 자연이나 문화공간을 사진으로 남기기"),
                place("사하구", "장림포구", "명소", "쉬움", "알록달록한 건물과 포토존으로 알려진 부네치아 포구", "부산광역시 사하구 장림로93번길 일대", vb("/uploadImgs/files/cntnts/20191227160751479_thumbL"), "약 1시간", "오후", "부네치아 컬러 찾기", "장림포구의 색감이 잘 보이는 사진 남기기"),
                place("사상구", "삼락생태공원", "공원", "쉬움", "낙동강 둔치에 조성된 넓은 생태공원과 계절별 꽃길 명소", "부산광역시 사상구 삼락동 29-46", vb("/uploadImgs/files/cntnts/20230602100722403_thumbL"), "약 2시간", "봄, 가을", "강변 공원 산책", "삼락생태공원의 계절 풍경을 사진으로 남기기"),
                place("북구", "석불사", "역사", "보통", "금정산 암벽에 새겨진 불상과 조용한 산사 분위기가 인상적인 사찰", "부산광역시 북구 만덕고개길 143-79", vb("/uploadImgs/files/cntnts/20191231102223413_thumbL"), "약 1.5시간", "오전", "암벽 사찰 기록", "석불사의 암벽 불상이나 산길 풍경을 사진으로 남기기"),
                place("북구", "구포만세테마거리", "역사", "쉬움", "구포장터 3.1 만세운동의 역사를 따라 걸을 수 있는 거리", "부산광역시 북구 구포시장 일대", "https://upload.wikimedia.org/wikipedia/commons/d/d5/Busan_subway_Line3_Gupo_station_200612.jpg", "약 1시간", "오전 10시~오후 5시", "만세 거리 탐방", "거리에서 역사 표식이나 시장 풍경을 사진으로 남기기", "구포만세거리", "구포장터"),
                place("남구", "두도", "명소", "쉬움", "남구 해안에서 바라볼 수 있는 작은 무인도와 바다 풍경", "부산광역시 남구 용호동 두도 일대", "https://upload.wikimedia.org/wikipedia/commons/5/54/Busan-Dudo.jpg", "약 1시간", "맑은 날 오후", "작은 섬 찾기", "두도가 보이는 해안 풍경을 사진으로 남기기"),
                place("서구", "송도해수욕장", "명소", "쉬움", "부산 최초의 공설 해수욕장으로 케이블카와 해안 산책로를 함께 즐길 수 있는 곳", "부산광역시 서구 송도해변로 100", vb("/uploadImgs/files/cntnts/20191225175459392_thumbL"), "약 2시간", "오후, 일몰", "송도 바다 인증", "송도해수욕장과 해상케이블카가 보이는 사진 남기기"),
                place("중구", "남포동 포장마차거리", "맛집", "쉬움", "남포동 밤거리와 길거리 먹거리를 즐길 수 있는 원도심 먹거리 골목", "부산광역시 중구 남포동 일대", "https://upload.wikimedia.org/wikipedia/commons/5/54/Food_alley_in_Nampo-dong%2C_Busan_in_2003.jpg", "약 1시간", "저녁~야간", "야시장 한 줄 기록", "포장마차 거리의 분위기나 먹거리를 사진으로 남기기"),
                place("중구", "부평깡통시장", "시장", "쉬움", "야시장과 다양한 먹거리로 유명한 부산 원도심 시장", "부산광역시 중구 부평1길 48", vb("/uploadImgs/files/cntnts/20230602095512309_thumbL"), "약 1시간", "저녁", "깡통시장 먹거리", "시장 안에서 가장 기억에 남은 먹거리를 사진으로 남기기"),
                place("영도구", "흰여울문화마을", "명소", "쉬움", "절벽 위 골목과 바다 전망이 이어지는 영도의 대표 문화마을", "부산광역시 영도구 흰여울길 379", vb("/uploadImgs/files/cntnts/20191222164810529_thumbL"), "약 1.5시간", "오전, 오후", "흰여울 골목 산책", "바다와 골목이 함께 보이는 지점을 사진으로 남기기"),
                place("영도구", "영도대교", "역사", "쉬움", "피란민의 역사와 도개교의 상징성을 간직한 영도 연결 다리", "부산광역시 영도구 대교동1가", vb("/uploadImgs/files/cntnts/20191229150331682_thumbL"), "약 30분", "낮", "다리 위 약속", "영도대교의 구조나 주변 항구 풍경을 사진으로 남기기"),
                place("동구", "충혼탑", "역사", "보통", "중앙공원 일대에서 부산항과 원도심을 내려다볼 수 있는 현충 시설", "부산광역시 동구 초량동 중앙공원 일대", vb("/uploadImgs/files/cntnts/20191229150845004_thumbL"), "약 1시간", "오전", "원도심 전망 기록", "충혼탑 주변에서 보이는 부산항 풍경을 사진으로 남기기"),
                place("동구", "산복도로", "명소", "보통", "부산의 언덕 마을과 항구 풍경을 따라 이어지는 원도심 도로", "부산광역시 동구 망양로 일대", vb("/uploadImgs/files/cntnts/20240923115623200_thumbL"), "약 2시간", "오후, 야간", "산복도로 전망", "산복도로에서 내려다본 부산 풍경을 사진으로 남기기"),
                place("동구", "북항친수공원", "공원", "쉬움", "부산항 북항 재개발지에 조성된 바다 전망 친수공원", "부산광역시 동구 초량동 45-39 일대", vb("/uploadImgs/files/cntnts/20240911140423764_thumbL"), "약 1시간", "일몰~야간", "북항 야경 수집", "북항친수공원에서 바다와 야경을 사진으로 남기기", "북항 친수공원"),
                place("부산진구", "부산진성", "역사", "쉬움", "임진왜란과 조선통신사 역사 흔적을 품은 도심 속 역사 공간", "부산광역시 부산진구 자성로 일대", vb("/uploadImgs/files/cntnts/20191225151358656_thumbL"), "약 1시간", "오전 10시~오후 5시", "도심 역사 탐방", "부산진성 주변 역사 표식을 사진으로 남기기", "자성대"),
                place("남구", "유엔평화공원", "공원", "쉬움", "유엔기념공원과 함께 평화의 의미를 되새길 수 있는 남구 공원", "부산광역시 남구 대연동 779-1", vb("/uploadImgs/files/cntnts/20231026100047318_thumbL"), "약 1시간", "오전", "평화의 길 산책", "공원에서 평화를 상징하는 장면을 사진으로 남기기"),
                place("남구", "오륙도", "명소", "보통", "바다 위로 늘어선 섬과 스카이워크 전망이 유명한 남구 해안 명소", "부산광역시 남구 오륙도로 137", vb("/uploadImgs/files/cntnts/20191225164921130_thumbL"), "약 1.5시간", "맑은 날 오전", "오륙도 전망 기록", "오륙도와 바다가 함께 담기는 사진 남기기"),
                place("수영구", "광안리해수욕장", "명소", "쉬움", "광안대교 야경과 해변 문화가 어우러진 부산 대표 해수욕장", "부산광역시 수영구 광안해변로 219", vb("/uploadImgs/files/cntnts/20191229160529389_thumbL"), "약 2시간", "일몰~야간", "광안대교 야경", "광안대교와 해변이 함께 보이는 사진 남기기", "광안리 해수욕장"),
                place("수영구", "민락수변공원", "공원", "쉬움", "광안리 바다와 광안대교를 가까이 즐길 수 있는 수변 공원", "부산광역시 수영구 민락수변로 129", vb("/uploadImgs/files/cntnts/20191225174730014_thumbL"), "약 1시간", "저녁", "수변공원 바다 기록", "민락수변공원에서 바다와 도시 풍경을 사진으로 남기기"),
                place("연제구", "온천천", "공원", "쉬움", "도심을 따라 산책로와 카페, 벚꽃길이 이어지는 하천 산책 명소", "부산광역시 연제구 온천천 일대", "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2d/Reflection_in_Busan_Onchonchon_River.jpg/1280px-Reflection_in_Busan_Onchonchon_River.jpg", "약 1시간", "봄, 저녁", "도심 하천 산책", "온천천 산책로에서 계절감이 보이는 사진 남기기"),
                place("연제구", "연산동 고분군", "역사", "쉬움", "삼국시대 무덤군이 남아 있는 연제구의 역사 유적", "부산광역시 연제구 연산동 산70 일대", "https://upload.wikimedia.org/wikipedia/commons/thumb/8/84/Busan-IMGP7037.jpg/1280px-Busan-IMGP7037.jpg", "약 1시간", "오전 10시~오후 5시", "고분군 역사 산책", "연산동 고분군의 유적 표식이나 언덕 풍경을 사진으로 남기기", "연산군 고분군"),
                place("동래구", "허심청", "문화", "쉬움", "동래온천을 대표하는 대형 온천 시설", "부산광역시 동래구 금강공원로20번길 23", vb("/uploadImgs/files/cntnts/20191230181126685_thumbL"), "약 2시간", "오전, 저녁", "동래온천 기록", "동래온천 거리나 허심청 외관을 사진으로 남기기"),
                place("동래구", "모모스커피", "맛집", "쉬움", "부산 스페셜티 커피 문화를 대표하는 온천장 인근 카페", "부산광역시 동래구 금강공원로20번길 23 일대", "https://images.unsplash.com/photo-1509042239860-f550ce710b93?w=800&h=500&fit=crop&auto=format", "약 1시간", "오전 10시~오후 5시", "커피 한 잔 기록", "카페에서 마신 커피나 공간 분위기를 사진으로 남기기"),
                place("해운대구", "달맞이길", "명소", "쉬움", "해운대와 청사포를 잇는 언덕길로 바다 전망과 카페가 이어지는 산책길", "부산광역시 해운대구 달맞이길 일대", vb("/uploadImgs/files/cntnts/20191227190325222_thumbL"), "약 1.5시간", "오후, 일몰", "달맞이 언덕 산책", "달맞이길에서 바다가 보이는 지점을 사진으로 남기기"),
                place("해운대구", "해리단길", "문화", "쉬움", "해운대역 뒤편 주택가에 카페와 편집숍이 모인 골목", "부산광역시 해운대구 우동 해리단길 일대", vb("/uploadImgs/files/cntnts/20191227190325222_thumbL"), "약 1시간", "오후", "골목 감성 기록", "해리단길에서 마음에 드는 가게나 골목을 사진으로 남기기"),
                place("기장군", "이케아 동부산", "쇼핑", "쉬움", "오시리아 관광단지 인근에 위치한 대형 라이프스타일 쇼핑 공간", "부산광역시 기장군 기장읍 동부산관광3로 17", vb("/uploadImgs/files/cntnts/20241107133504063_thumbL"), "약 1.5시간", "오전 11시~오후 8시", "동부산 쇼핑 기록", "이케아 동부산에서 인상적인 공간이나 소품을 사진으로 남기기", "이케아"),
                place("기장군", "국립부산과학관", "문화", "쉬움", "체험형 전시와 과학 교육 프로그램을 갖춘 부산 대표 과학관", "부산광역시 기장군 기장읍 동부산관광6로 59", vb("/uploadImgs/files/cntnts/20230901132543063_thumbL"), "약 2시간", "오전 10시~오후 5시", "과학 체험 기록", "가장 흥미로웠던 전시나 체험을 사진으로 남기기", "국산부산과학관"),
                place("기장군", "해동용궁사", "역사", "보통", "바다와 맞닿은 절경으로 유명한 기장 해안 사찰", "부산광역시 기장군 기장읍 용궁길 86", vb("/uploadImgs/files/cntnts/20191222190823385_thumbL"), "약 1.5시간", "오전", "바다 사찰 기록", "해동용궁사와 바다가 함께 보이는 사진 남기기"),
                place("금정구", "금정산성", "역사", "보통", "금정산 능선을 따라 이어지는 산성과 성문을 만날 수 있는 역사 산행지", "부산광역시 금정구 금성동 일대", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1f/Geumjeong_Fortress_North_Gate.jpg/1280px-Geumjeong_Fortress_North_Gate.jpg", "약 2시간", "오전", "산성길 걷기", "금정산성 성문이나 성곽길을 사진으로 남기기"),
                place("금정구", "오륜대", "명소", "보통", "회동수원지와 산책길이 어우러진 금정구의 조용한 자연 명소", "부산광역시 금정구 오륜동 일대", vb("/uploadImgs/files/cntnts/20240401133553180_thumbL"), "약 1.5시간", "오전, 오후", "호수 둘레길 기록", "오륜대 주변 호수나 숲길 풍경을 사진으로 남기기")
        );
    }

    private SeedPlace place(
            String region,
            String name,
            String category,
            String difficulty,
            String description,
            String address,
            String imageUrl,
            String duration,
            String bestTime,
            String missionTitle,
            String missionDescription,
            String... aliases
    ) {
        return new SeedPlace(
                region,
                name,
                category,
                difficulty,
                description,
                address,
                imageUrl,
                duration,
                bestTime,
                missionTitle,
                missionDescription,
                Arrays.asList(aliases)
        );
    }

    private static String vb(String imagePath) {
        return VISIT_BUSAN + imagePath;
    }

    private List<SeedAchievement> seedAchievementData() {
        return List.of(
                new SeedAchievement("부산 시장 마스터", "부산의 숨은 시장을 모두 방문하기", "fish", "category", "시장", 2),
                new SeedAchievement("해안선 수호자", "해안 지역 장소 3곳 방문하기", "waves", "region_any", "해운대구,수영구,남구,영도구,서구", 3),
                new SeedAchievement("부산 탐험가", "3곳 이상의 장소에서 미션 완료하기", "map", "count_total", "3", null),
                new SeedAchievement("축제 마니아", "부산의 축제 방문하기", "party", "category", "축제", 1),
                new SeedAchievement("쇼핑 달인", "부산의 쇼핑 명소 방문하기", "shopping", "category", "쇼핑", 1),
                new SeedAchievement("역사 산책자", "역사 명소 미션 2개 이상 완료하기", "landmark", "category", "역사", 2),
                new SeedAchievement("부산 완전 정복", "모든 미션을 완료하기", "sailboat", "count_total", "49", null)
        );
    }

    private record SeedPlace(
            String region,
            String name,
            String category,
            String difficulty,
            String description,
            String address,
            String imageUrl,
            String duration,
            String bestTime,
            String missionTitle,
            String missionDescription,
            List<String> aliases
    ) {

        private List<String> lookupNames() {
            if (aliases.isEmpty()) {
                return List.of(name);
            }

            List<String> names = new ArrayList<>();
            names.add(name);
            names.addAll(aliases);
            return names;
        }
    }

    private record SeedAchievement(
            String title,
            String description,
            String icon,
            String conditionType,
            String conditionValue,
            Integer conditionTotal
    ) {
    }
}
