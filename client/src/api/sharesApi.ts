import { http, useRealApi } from './http';
import { mockDb } from './mockDb';
import type { CollectionShare } from '@/types/domain';

export const sharesApi = {
  async getOrCreateShare(userId: string): Promise<CollectionShare> {
    if (useRealApi) {
      const { data } = await http.post<CollectionShare>('/shares', { userId });
      return data;
    }

    return mockDb.getOrCreateShare(userId);
  },
};
