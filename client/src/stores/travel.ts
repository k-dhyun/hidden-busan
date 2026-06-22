import { defineStore } from 'pinia';
import { achievementsApi } from '@/api/achievementsApi';
import { contentsApi } from '@/api/contentsApi';
import { favoritesApi } from '@/api/favoritesApi';
import { missionsApi } from '@/api/missionsApi';
import { reviewsApi } from '@/api/reviewsApi';
import { sharesApi } from '@/api/sharesApi';
import type {
  Achievement,
  AchievementProgress,
  CollectionShare,
  Content,
  District,
  Favorite,
  Mission,
  MissionCompletion,
  Review,
} from '@/types/domain';

interface TravelState {
  contents: Content[];
  districts: District[];
  missions: Mission[];
  favorites: Favorite[];
  completions: MissionCompletion[];
  reviews: Review[];
  share: CollectionShare | null;
  achievements: Achievement[];
  loading: boolean;
}

export const useTravelStore = defineStore('travel', {
  state: (): TravelState => ({
    contents: [],
    districts: [],
    missions: [],
    favorites: [],
    completions: [],
    reviews: [],
    share: null,
    achievements: [],
    loading: false,
  }),
  getters: {
    completedContentIds: (state) => [...new Set(state.completions.map((completion) => completion.contentId))],
    inProgressMissions: (state) =>
      state.missions.filter(
        (mission) =>
          !state.completions.some((completion) => completion.missionId === mission.id),
      ),
  },
  actions: {
    async loadInitialData(userId?: string) {
      this.loading = true;

      try {
        const [contents, districts, missions, achievements] = await Promise.all([
          contentsApi.listContents(),
          contentsApi.listDistricts(),
          missionsApi.listMissions(),
          achievementsApi.listAchievements(),
        ]);

        this.contents = contents;
        this.districts = districts;
        this.missions = missions;
        this.achievements = achievements;

        if (userId) {
          await this.loadUserData(userId);
        }
      } finally {
        this.loading = false;
      }
    },
    async loadUserData(userId: string) {
      const [favorites, completions, share] = await Promise.all([
        favoritesApi.listFavorites(userId),
        missionsApi.listCompletions(userId),
        sharesApi.getOrCreateShare(userId),
      ]);

      this.favorites = favorites;
      this.completions = completions;
      this.share = share;
    },
    getContentById(id: string) {
      return this.contents.find((content) => content.id === id);
    },
    getMissionById(id: string) {
      return this.missions.find((mission) => mission.id === id);
    },
    getMissionByContentId(contentId: string) {
      return this.missions.find((mission) => mission.contentId === contentId);
    },
    getReviewsByContentId(contentId: string) {
      return this.reviews.filter((review) => review.contentId === contentId);
    },
    isFavorited(contentId: string) {
      return this.favorites.some((favorite) => favorite.contentId === contentId);
    },
    async loadReviewsForContent(contentId: string) {
      const loadedReviews = await reviewsApi.listReviews(contentId);
      const otherReviews = this.reviews.filter((review) => review.contentId !== contentId);
      this.reviews = [...otherReviews, ...loadedReviews];
    },
    async toggleFavorite(userId: string, contentId: string) {
      this.favorites = await favoritesApi.toggleFavorite(userId, contentId);
    },
    async addReview(userId: string, contentId: string, rating: number, comment: string) {
      const review = await reviewsApi.addReview({ userId, contentId, rating, comment });
      this.reviews = [review, ...this.reviews];
      return review;
    },
    async completeMission(input: {
      userId: string;
      missionId: string;
      contentId: string;
      photoUrl: string;
      stampEmoji?: string;
      memo: string;
    }) {
      const completion = await missionsApi.completeMission(input);
      this.completions = [
        completion,
        ...this.completions.filter((item) => item.id !== completion.id),
      ];
      return completion;
    },
    async getOrCreateShare(userId: string) {
      this.share = await sharesApi.getOrCreateShare(userId);
      return this.share;
    },
    getAchievementProgress(achievement: Achievement): AchievementProgress {
      const completedContentIds = new Set(this.completions.map((completion) => completion.contentId));

      if (achievement.condition.type === 'category') {
        const categoryContents = this.contents.filter(
          (content) => content.category === achievement.condition.value,
        );
        const current = categoryContents.filter((content) => completedContentIds.has(content.id)).length;
        const total = achievement.condition.total ?? categoryContents.length;
        return { current, total, unlocked: current >= total };
      }

      if (achievement.condition.type === 'region_any') {
        const regions = String(achievement.condition.value).split(',');
        const regionContents = this.contents.filter((content) => regions.includes(content.region));
        const current = regionContents.filter((content) => completedContentIds.has(content.id)).length;
        const total = achievement.condition.total ?? 1;
        return { current, total, unlocked: current >= total };
      }

      if (achievement.condition.type === 'difficulty') {
        const difficultyContents = this.contents.filter(
          (content) => content.difficulty === achievement.condition.value,
        );
        const current = difficultyContents.filter((content) => completedContentIds.has(content.id)).length;
        const total = achievement.condition.total ?? 1;
        return { current, total, unlocked: current >= total };
      }

      const current = completedContentIds.size;
      const total = Number(achievement.condition.value);
      return { current, total, unlocked: current >= total };
    },
  },
});
