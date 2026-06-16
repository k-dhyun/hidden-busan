import { http, useRealApi } from './http';
import { mockDb } from './mockDb';
import type { LoginResult } from '@/types/domain';

export const authApi = {
  async login(email: string, password: string): Promise<LoginResult> {
    if (useRealApi) {
      const { data } = await http.post<LoginResult>('/auth/login', { email, password });
      return data;
    }

    return mockDb.login(email, password);
  },
  async signup(nickname: string, email: string, password: string): Promise<LoginResult> {
    if (useRealApi) {
      const { data } = await http.post<LoginResult>('/auth/signup', { nickname, email, password });
      return data;
    }

    return mockDb.signup(nickname, email, password);
  },
};
