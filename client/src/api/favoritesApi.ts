import { http, useRealApi } from './http';
import { mockDb } from './mockDb';
import type { Favorite } from '@/types/domain';

export const favoritesApi = {
  async listFavorites(userId: string): Promise<Favorite[]> {
    if (useRealApi) {
      const { data } = await http.get<Favorite[]>('/favorites', { params: { userId } });
      return data;
    }

    return mockDb.listFavorites(userId);
  },
  async toggleFavorite(userId: string, contentId: string): Promise<Favorite[]> {
    if (useRealApi) {
      const { data } = await http.post<Favorite[]>('/favorites/toggle', { userId, contentId });
      return data;
    }

    return mockDb.toggleFavorite(userId, contentId);
  },
};
