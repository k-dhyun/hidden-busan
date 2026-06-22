import type {
  Achievement,
  CollectionShare,
  Content,
  District,
  Favorite,
  Mission,
  MissionCompletion,
  Review,
  User,
} from '@/types/domain';

export const currentUser: User = {
  id: 'user-1',
  nickname: '부산탐험가',
  email: 'traveler@busan.kr',
  avatar:
    'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=80&h=80&fit=crop&auto=format',
  createdAt: '2024-01-15',
};

export const districts: District[] = [
  { id: 'gangseo', label: '강서구', region: '강서구' },
  { id: 'sasang', label: '사상구', region: '사상구' },
  { id: 'buk', label: '북구', region: '북구' },
  { id: 'geumjeong', label: '금정구', region: '금정구' },
  { id: 'gijang', label: '기장군', region: '기장군' },
  { id: 'dongrae', label: '동래구', region: '동래구' },
  { id: 'yeonje', label: '연제구', region: '연제구' },
  { id: 'busanjin', label: '부산진구', region: '부산진구' },
  { id: 'haeundae', label: '해운대구', region: '해운대구' },
  { id: 'suyeong', label: '수영구', region: '수영구' },
  { id: 'nam', label: '남구', region: '남구' },
  { id: 'dong', label: '동구', region: '동구' },
  { id: 'saha', label: '사하구', region: '사하구' },
  { id: 'seo', label: '서구', region: '서구' },
  { id: 'jung', label: '중구', region: '중구' },
  { id: 'yeongdo', label: '영도구', region: '영도구' },
];

export const contents: Content[] = [
  {
    id: 'c1',
    name: '이기대 해상산책로',
    region: '남구',
    category: '명소',
    difficulty: '보통',
    description: '탁 트인 바다와 해안 절경을 함께 즐길 수 있는 산책로',
    address: '부산광역시 남구 용호동 산197-5',
    image:
      'https://images.unsplash.com/photo-1648799515682-997b312074e0?w=800&h=500&fit=crop&auto=format',
    duration: '약 2시간',
    bestTime: '일출, 일몰',
  },
  {
    id: 'c2',
    name: '감천문화마을',
    region: '사하구',
    category: '명소',
    difficulty: '쉬움',
    description: '알록달록 벽화와 미로 골목이 가득한 부산의 마추픽추',
    address: '부산광역시 사하구 감내2로 203',
    image:
      'https://images.unsplash.com/photo-1647834235255-c9a039e769e2?w=800&h=500&fit=crop&auto=format',
    duration: '약 1.5시간',
    bestTime: '오전 10시~오후 4시',
  },
  {
    id: 'c3',
    name: '자갈치시장',
    region: '중구',
    category: '시장',
    difficulty: '쉬움',
    description: '부산 최대 수산물 시장, 신선한 해산물과 활기찬 상인들의 삶',
    address: '부산광역시 중구 자갈치해안로 52',
    image:
      'https://images.unsplash.com/photo-1697635573508-6436964a2042?w=800&h=500&fit=crop&auto=format',
    duration: '약 1시간',
    bestTime: '오전 7시~오후 6시',
  },
  {
    id: 'c4',
    name: '해운대 블루라인파크',
    region: '해운대구',
    category: '명소',
    difficulty: '쉬움',
    description: '해변을 따라 달리는 해상 케이블카와 스카이캡슐 체험',
    address: '부산광역시 해운대구 달맞이길 62번길',
    image:
      'https://images.unsplash.com/photo-1650698055751-664bb87c8639?w=800&h=500&fit=crop&auto=format',
    duration: '약 1시간',
    bestTime: '오후 2시~오후 6시',
  },
  {
    id: 'c5',
    name: '태종대 유원지',
    region: '영도구',
    category: '명소',
    difficulty: '보통',
    description: '기암괴석과 울창한 숲, 등대가 어우러진 천혜의 절경',
    address: '부산광역시 영도구 전망로 24',
    image:
      'https://images.unsplash.com/photo-1650698054997-9dfe72fbe3ea?w=800&h=500&fit=crop&auto=format',
    duration: '약 2시간',
    bestTime: '오전 9시~오후 5시',
  },
  {
    id: 'c6',
    name: 'BIFF 광장',
    region: '중구',
    category: '명소',
    difficulty: '쉬움',
    description: '부산국제영화제 본거지, 핸드프린팅과 영화의 역사가 담긴 거리',
    address: '부산광역시 중구 비프광장로 36',
    image:
      'https://images.unsplash.com/photo-1697635596622-fc0ceed14704?w=800&h=500&fit=crop&auto=format',
    duration: '약 1시간',
    bestTime: '오후 1시~오후 9시',
  },
  {
    id: 'c7',
    name: '부산국제시장',
    region: '중구',
    category: '시장',
    difficulty: '쉬움',
    description: '6.25 전쟁 피난민이 세운 역사적인 시장, 다양한 먹거리와 기념품',
    address: '부산광역시 중구 국제시장4길 10',
    image:
      'https://images.unsplash.com/photo-1650698055725-84bba9266c15?w=800&h=500&fit=crop&auto=format',
    duration: '약 1.5시간',
    bestTime: '오전 10시~오후 7시',
  },
  {
    id: 'c8',
    name: '광안리 어방축제',
    region: '수영구',
    category: '축제',
    difficulty: '쉬움',
    description: '광안리 해변에서 펼쳐지는 봄철 전통 어업 문화 축제',
    address: '부산광역시 수영구 광안해변로 219',
    image:
      'https://images.unsplash.com/photo-1650698055741-ccca5f5bff88?w=800&h=500&fit=crop&auto=format',
    duration: '하루 종일',
    bestTime: '4월~5월',
  },
  {
    id: 'c9',
    name: '서면 쇼핑거리',
    region: '부산진구',
    category: '쇼핑',
    difficulty: '쉬움',
    description: '부산 최대 상업 지구, 패션·뷰티·맛집이 모두 모인 핫플',
    address: '부산광역시 부산진구 서면로 56',
    image:
      'https://images.unsplash.com/photo-1747472892019-2eb7a930f4d3?w=800&h=500&fit=crop&auto=format',
    duration: '약 2시간',
    bestTime: '낮 12시~오후 10시',
  },
  {
    id: 'c10',
    name: '해운대 암남공원 해녀촌',
    region: '해운대구',
    category: '맛집',
    difficulty: '쉬움',
    description: '해녀가 직접 잡은 해산물을 맛볼 수 있는 해운대 로컬 맛집',
    address: '부산광역시 해운대구 중동 해변로',
    image:
      'https://images.unsplash.com/photo-1535399831218-d5bd36d1a6b3?w=800&h=500&fit=crop&auto=format',
    duration: '약 1시간',
    bestTime: '오전 11시~오후 3시',
  },
  {
    id: 'c11',
    name: '동래파전 거리',
    region: '동래구',
    category: '맛집',
    difficulty: '쉬움',
    description: '부산 전통 파전 골목, 100년 넘은 파전 맛집들이 즐비한 거리',
    address: '부산광역시 동래구 동래로 100',
    image:
      'https://images.unsplash.com/photo-1590301157890-4810ed352733?w=800&h=500&fit=crop&auto=format',
    duration: '약 1시간',
    bestTime: '점심 11시~오후 5시',
  },
  {
    id: 'c12',
    name: '기장 대변항 멸치축제',
    region: '기장군',
    category: '축제',
    difficulty: '쉬움',
    description: '매년 봄 기장 대변항에서 열리는 멸치 풍어를 기원하는 전통 축제',
    address: '부산광역시 기장군 기장읍 대변리',
    image:
      'https://images.unsplash.com/photo-1533777857889-4be7c70b33f7?w=800&h=500&fit=crop&auto=format',
    duration: '하루 종일',
    bestTime: '4월~5월',
  },
  {
    id: 'c13',
    name: '남포동 씨앗호떡',
    region: '중구',
    category: '맛집',
    difficulty: '쉬움',
    description: '부산 중구 남포동 명물 씨앗호떡, 관광객과 현지인 모두 즐겨 찾는 간식',
    address: '부산광역시 중구 남포동 광복로',
    image:
      'https://images.unsplash.com/photo-1567620905732-2d1ec7ab7445?w=800&h=500&fit=crop&auto=format',
    duration: '30분',
    bestTime: '오전 10시~오후 9시',
  },
  {
    id: 'c14',
    name: '금정산성 막걸리 마을',
    region: '금정구',
    category: '맛집',
    difficulty: '보통',
    description: '금정산 산성마을에서 전통 방식으로 빚은 산성막걸리와 파전 즐기기',
    address: '부산광역시 금정구 산성마을',
    image:
      'https://images.unsplash.com/photo-1516594798947-e65505dbb29d?w=800&h=500&fit=crop&auto=format',
    duration: '약 2시간',
    bestTime: '주말 오전 11시~오후 4시',
  },
  {
    id: 'c15',
    name: '광안리 해수욕장',
    region: '수영구',
    category: '명소',
    difficulty: '쉬움',
    description: '광안대교를 배경으로 한 부산의 대표 해수욕장, 야경이 특히 아름다운 곳',
    address: '부산광역시 수영구 광안해변로 219',
    image:
      'https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=800&h=500&fit=crop&auto=format',
    duration: '약 2시간',
    bestTime: '일몰~야간',
  },
  {
    id: 'c16',
    name: '사하구 다대포 해수욕장',
    region: '사하구',
    category: '명소',
    difficulty: '쉬움',
    description: '부산 서쪽 끝 낙조로 유명한 해수욕장, 혼잡하지 않아 여유롭게 즐길 수 있는 곳',
    address: '부산광역시 사하구 다대동',
    image:
      'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=800&h=500&fit=crop&auto=format',
    duration: '약 2시간',
    bestTime: '일몰 시간',
  },
  {
    id: 'c17',
    name: '영도 깡깡이 마을',
    region: '영도구',
    category: '명소',
    difficulty: '쉬움',
    description: '수리조선소 배경의 벽화마을, 부산 근대 역사가 담긴 독특한 골목길',
    address: '부산광역시 영도구 대평로 27번길',
    image:
      'https://images.unsplash.com/photo-1584464491033-06628f3a6b7b?w=800&h=500&fit=crop&auto=format',
    duration: '약 1.5시간',
    bestTime: '오전 10시~오후 5시',
  },
  {
    id: 'c18',
    name: '부산진구 부산시민공원',
    region: '부산진구',
    category: '명소',
    difficulty: '쉬움',
    description: '옛 미군 기지 터에 조성된 대규모 도심 공원, 부산 시민들의 휴식 공간',
    address: '부산광역시 부산진구 시민공원로 73',
    image:
      'https://images.unsplash.com/photo-1585320806297-9794b3e4eeae?w=800&h=500&fit=crop&auto=format',
    duration: '약 2시간',
    bestTime: '오전 9시~오후 6시',
  },
];

export const missions: Mission[] = [
  { id: 'm1', contentId: 'c1', title: '해안 절경 포착', description: '바다가 가장 잘 보이는 포인트를 사진으로 기록하기' },
  { id: 'm2', contentId: 'c2', title: '골목 탐험가', description: '가장 마음에 드는 벽화 앞에서 인증샷 찍기' },
  { id: 'm3', contentId: 'c3', title: '새벽 수산시장 탐방', description: '활어회 한 접시를 주문하고 영수증 사진 찍기' },
  { id: 'm4', contentId: 'c4', title: '스카이라인 수집', description: '해상 케이블카 탑승 후 해운대 전경 사진 남기기' },
  { id: 'm5', contentId: 'c5', title: '등대지기 체험', description: '태종대 등대 앞에서 바다를 배경으로 사진 찍기' },
  { id: 'm6', contentId: 'c6', title: '내 발자국 남기기', description: '핸드프린팅 광장에서 좋아하는 배우 핸드프린팅 찾기' },
  { id: 'm7', contentId: 'c7', title: '피난민의 밥상', description: '국제시장 명물 비빔당면 맛보고 한 줄 후기 남기기' },
  { id: 'm8', contentId: 'c8', title: '불꽃 아래 셀카', description: '광안대교 야경을 배경으로 셀카 찍기' },
  { id: 'm9', contentId: 'c9', title: '서면 쇼핑 인증', description: '서면 지하상가에서 기념품 하나 구매하고 영수증 사진 찍기' },
];

export const favorites: Favorite[] = [
  { id: 'f1', userId: 'user-1', contentId: 'c1', createdAt: '2024-03-10' },
  { id: 'f2', userId: 'user-1', contentId: 'c4', createdAt: '2024-03-12' },
  { id: 'f3', userId: 'user-1', contentId: 'c5', createdAt: '2024-03-15' },
];

export const missionCompletions: MissionCompletion[] = [
  {
    id: 'mc1',
    userId: 'user-1',
    missionId: 'm1',
    contentId: 'c1',
    photoUrl:
      'https://images.unsplash.com/photo-1648799515682-997b312074e0?w=400&h=300&fit=crop&auto=format',
    stampEmoji: '🌊',
    memo: '일몰 때 오니 황금빛 바다가 너무 아름다웠어요!',
    completedAt: '2024-03-10',
  },
  {
    id: 'mc2',
    userId: 'user-1',
    missionId: 'm2',
    contentId: 'c2',
    photoUrl:
      'https://images.unsplash.com/photo-1647834235255-c9a039e769e2?w=400&h=300&fit=crop&auto=format',
    stampEmoji: '🎨',
    memo: '어묵 고양이 벽화 앞에서 찍었는데 너무 귀여워요',
    completedAt: '2024-03-12',
  },
  {
    id: 'mc3',
    userId: 'user-1',
    missionId: 'm5',
    contentId: 'c5',
    photoUrl:
      'https://images.unsplash.com/photo-1650698054997-9dfe72fbe3ea?w=400&h=300&fit=crop&auto=format',
    stampEmoji: '🏝️',
    memo: '등대 아래 파도 소리가 정말 장관이었어요',
    completedAt: '2024-03-15',
  },
];

export const reviews: Review[] = [
  {
    id: 'r1',
    userId: 'user-1',
    contentId: 'c1',
    rating: 5,
    comment: '부산에서 꼭 가봐야 할 곳! 파도 소리와 절경이 최고예요.',
    createdAt: '2024-03-10',
  },
  {
    id: 'r2',
    userId: 'user-2',
    contentId: 'c1',
    rating: 4,
    comment: '경치는 훌륭한데 주말엔 사람이 많아요.',
    createdAt: '2024-03-08',
  },
  {
    id: 'r3',
    userId: 'user-3',
    contentId: 'c1',
    rating: 5,
    comment: '일몰 시간에 맞춰 가면 황금빛 바다를 볼 수 있어요!',
    createdAt: '2024-03-05',
  },
];

export const collectionShares: CollectionShare[] = [
  {
    id: 'cs1',
    userId: 'user-1',
    shareToken: 'busan-explorer-abc123',
    shareUrl: 'https://hiddenbusan.kr/share/busan-explorer-abc123',
    createdAt: '2024-03-20',
  },
];

export const achievements: Achievement[] = [
  {
    id: 'ach1',
    title: '부산 시장 마스터',
    description: '부산의 숨은 시장을 모두 방문하기',
    icon: 'fish',
    condition: { type: 'category', value: '시장', total: 2 },
  },
  {
    id: 'ach2',
    title: '해안선 수호자',
    description: '해안 지역 장소 3곳 방문하기',
    icon: 'waves',
    condition: { type: 'region_any', value: '해운대구,수영구,남구,영도구', total: 3 },
  },
  {
    id: 'ach3',
    title: '부산 탐험가',
    description: '3곳 이상의 장소에서 미션 완료하기',
    icon: 'map',
    condition: { type: 'count_total', value: 3 },
  },
  {
    id: 'ach4',
    title: '축제 마니아',
    description: '부산의 축제 방문하기',
    icon: 'party',
    condition: { type: 'category', value: '축제', total: 1 },
  },
  {
    id: 'ach5',
    title: '쇼핑 달인',
    description: '부산의 쇼핑 명소 방문하기',
    icon: 'shopping',
    condition: { type: 'category', value: '쇼핑', total: 1 },
  },
  {
    id: 'ach6',
    title: '새벽 여행자',
    description: '난이도 어려움 미션 1개 이상 완료하기',
    icon: 'sunrise',
    condition: { type: 'difficulty', value: '어려움', total: 1 },
  },
  {
    id: 'ach7',
    title: '부산 완전 정복',
    description: '모든 미션 9개를 완료하기',
    icon: 'sailboat',
    condition: { type: 'count_total', value: 9 },
  },
];
