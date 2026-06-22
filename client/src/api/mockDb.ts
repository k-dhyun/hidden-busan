import {
  achievements,
  collectionShares,
  contents,
  currentUser,
  districts,
  favorites,
  missionCompletions,
  missions,
  reviews,
} from '@/data/mockData';
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

interface MockDbState {
  users: User[];
  contents: Content[];
  districts: District[];
  missions: Mission[];
  favorites: Favorite[];
  missionCompletions: MissionCompletion[];
  reviews: Review[];
  collectionShares: CollectionShare[];
  achievements: Achievement[];
}

const STORAGE_KEY = 'hidden-busan:mock-db';

function clone<T>(value: T): T {
  return JSON.parse(JSON.stringify(value)) as T;
}

function mergeById<T extends { id: string }>(baseItems: T[], savedItems?: T[]): T[] {
  if (!savedItems) {
    return clone(baseItems);
  }

  const savedById = new Map(savedItems.map((item) => [item.id, item]));
  const mergedItems = baseItems.map((item) => ({
    ...item,
    ...savedById.get(item.id),
  }));
  const newSavedItems = savedItems.filter(
    (savedItem) => !baseItems.some((baseItem) => baseItem.id === savedItem.id),
  );

  return clone([...mergedItems, ...newSavedItems]);
}

function initialState(): MockDbState {
  return {
    users: [currentUser],
    contents: clone(contents),
    districts: clone(districts),
    missions: clone(missions),
    favorites: clone(favorites),
    missionCompletions: clone(missionCompletions),
    reviews: clone(reviews),
    collectionShares: clone(collectionShares),
    achievements: clone(achievements),
  };
}

function readState(): MockDbState {
  if (typeof window === 'undefined') {
    return initialState();
  }

  const raw = window.localStorage.getItem(STORAGE_KEY);
  if (!raw) {
    return initialState();
  }

  try {
    const base = initialState();
    const saved = JSON.parse(raw) as Partial<MockDbState>;

    return {
      ...base,
      ...saved,
      users: mergeById(base.users, saved.users),
      contents: mergeById(base.contents, saved.contents),
      districts: mergeById(base.districts, saved.districts),
      missions: mergeById(base.missions, saved.missions),
      achievements: mergeById(base.achievements, saved.achievements),
    };
  } catch {
    return initialState();
  }
}

let state = readState();

function persist() {
  if (typeof window !== 'undefined') {
    window.localStorage.setItem(STORAGE_KEY, JSON.stringify(state));
  }
}

function today() {
  return new Date().toISOString().slice(0, 10);
}

function id(prefix: string) {
  return `${prefix}-${Date.now()}-${Math.random().toString(16).slice(2, 8)}`;
}

export const mockDb = {
  listContents: async () => clone(state.contents),
  listDistricts: async () => clone(state.districts),
  listMissions: async () => clone(state.missions),
  listAchievements: async () => clone(state.achievements),
  listReviews: async (contentId: string) =>
    clone(state.reviews.filter((review) => review.contentId === contentId)),
  listFavorites: async (userId: string) =>
    clone(state.favorites.filter((favorite) => favorite.userId === userId)),
  listCompletions: async (userId: string) =>
    clone(
      state.missionCompletions
        .filter((completion) => completion.userId === userId)
        .sort((a, b) => b.completedAt.localeCompare(a.completedAt)),
    ),
  login: async (email: string, _password: string) => {
    const existingUser = state.users.find((user) => user.email === email) ?? currentUser;

    return {
      user: clone(existingUser),
      token: `mock-jwt-${Date.now()}`,
    };
  },
  signup: async (nickname: string, email: string, password: string) => {
    const existingUser = state.users.find((user) => user.email === email);
    if (existingUser) {
      return mockDb.login(email, password);
    }

    const user: User = {
      id: id('user'),
      nickname,
      email,
      avatar:
        'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=80&h=80&fit=crop&auto=format',
      createdAt: today(),
    };

    state.users.push(user);
    persist();

    return {
      user: clone(user),
      token: `mock-jwt-${Date.now()}`,
    };
  },
  toggleFavorite: async (userId: string, contentId: string) => {
    const index = state.favorites.findIndex(
      (favorite) => favorite.userId === userId && favorite.contentId === contentId,
    );

    if (index >= 0) {
      state.favorites.splice(index, 1);
      persist();
      return clone(state.favorites.filter((favorite) => favorite.userId === userId));
    }

    state.favorites.push({
      id: id('fav'),
      userId,
      contentId,
      createdAt: today(),
    });
    persist();

    return clone(state.favorites.filter((favorite) => favorite.userId === userId));
  },
  addReview: async (userId: string, contentId: string, rating: number, comment: string) => {
    const review: Review = {
      id: id('review'),
      userId,
      contentId,
      rating,
      comment,
      createdAt: today(),
    };

    state.reviews = [review, ...state.reviews];
    persist();

    return clone(review);
  },
  completeMission: async (
    userId: string,
    missionId: string,
    contentId: string,
    photoUrl: string,
    stampEmoji: string | undefined,
    memo: string,
  ) => {
    const nextStampEmoji = stampEmoji ?? '🏆';
    const existing = state.missionCompletions.find(
      (completion) => completion.userId === userId && completion.missionId === missionId,
    );

    if (existing) {
      existing.photoUrl = photoUrl;
      existing.stampEmoji = nextStampEmoji;
      existing.memo = memo;
      existing.completedAt = today();
      persist();
      return clone(existing);
    }

    const completion: MissionCompletion = {
      id: id('completion'),
      userId,
      missionId,
      contentId,
      photoUrl,
      stampEmoji: nextStampEmoji,
      memo,
      completedAt: today(),
    };

    state.missionCompletions = [completion, ...state.missionCompletions];
    persist();

    return clone(completion);
  },
  getOrCreateShare: async (userId: string) => {
    const existing = state.collectionShares.find((share) => share.userId === userId);
    if (existing) {
      return clone(existing);
    }

    const token = `${userId}-passport-${Math.random().toString(16).slice(2, 8)}`;
    const share: CollectionShare = {
      id: id('share'),
      userId,
      shareToken: token,
      shareUrl: `https://hiddenbusan.kr/share/${token}`,
      createdAt: today(),
    };

    state.collectionShares.push(share);
    persist();

    return clone(share);
  },
};
