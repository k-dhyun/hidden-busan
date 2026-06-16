export type Category = '명소' | '축제' | '쇼핑' | '시장' | '맛집';
export type Difficulty = '쉬움' | '보통' | '어려움';
export type AchievementConditionType = 'category' | 'region_any' | 'difficulty' | 'count_total';

export interface User {
  id: string;
  nickname: string;
  email: string;
  avatar: string;
  createdAt: string;
}

export interface District {
  id: string;
  label: string;
  region: string;
  x: number;
  y: number;
}

export interface Content {
  id: string;
  name: string;
  region: string;
  category: Category;
  difficulty: Difficulty;
  description: string;
  address: string;
  image: string;
  duration: string;
  bestTime: string;
  lat: number;
  lng: number;
  mapX: number;
  mapY: number;
}

export interface Mission {
  id: string;
  contentId: string;
  title: string;
  description: string;
}

export interface Favorite {
  id: string;
  userId: string;
  contentId: string;
  createdAt: string;
}

export interface MissionCompletion {
  id: string;
  userId: string;
  missionId: string;
  contentId: string;
  photoUrl: string;
  memo: string;
  completedAt: string;
}

export interface Review {
  id: string;
  userId: string;
  contentId: string;
  rating: number;
  comment: string;
  createdAt: string;
}

export interface CollectionShare {
  id: string;
  userId: string;
  shareToken: string;
  shareUrl: string;
  createdAt: string;
}

export interface Achievement {
  id: string;
  title: string;
  description: string;
  icon: string;
  condition: {
    type: AchievementConditionType;
    value: string | number;
    total?: number;
  };
}

export interface LoginResult {
  user: User;
  token: string;
}

export interface AchievementProgress {
  current: number;
  total: number;
  unlocked: boolean;
}
