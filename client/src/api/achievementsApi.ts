import { http, useRealApi } from './http';
import { mockDb } from './mockDb';
import type { Achievement } from '@/types/domain';

export const achievementsApi = {
  async listAchievements(): Promise<Achievement[]> {
    if (useRealApi) {
      const { data } = await http.get<Achievement[]>('/achievements');
      return data;
    }

    return mockDb.listAchievements();
  },
};
