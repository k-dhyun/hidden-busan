import { http, useRealApi } from './http';
import { mockDb } from './mockDb';
import type { Content, District } from '@/types/domain';

export const contentsApi = {
  async listContents(): Promise<Content[]> {
    if (useRealApi) {
      const { data } = await http.get<Content[]>('/contents');
      return data;
    }

    return mockDb.listContents();
  },
  async listDistricts(): Promise<District[]> {
    if (useRealApi) {
      const { data } = await http.get<District[]>('/districts');
      return data;
    }

    return mockDb.listDistricts();
  },
};
