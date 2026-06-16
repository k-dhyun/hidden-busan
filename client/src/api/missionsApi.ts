import { http, useRealApi } from './http';
import { mockDb } from './mockDb';
import type { Mission, MissionCompletion } from '@/types/domain';

export const missionsApi = {
  async listMissions(): Promise<Mission[]> {
    if (useRealApi) {
      const { data } = await http.get<Mission[]>('/missions');
      return data;
    }

    return mockDb.listMissions();
  },
  async listCompletions(userId: string): Promise<MissionCompletion[]> {
    if (useRealApi) {
      const { data } = await http.get<MissionCompletion[]>('/mission-completions', {
        params: { userId },
      });
      return data;
    }

    return mockDb.listCompletions(userId);
  },
  async completeMission(input: {
    userId: string;
    missionId: string;
    contentId: string;
    photoUrl: string;
    memo: string;
  }): Promise<MissionCompletion> {
    if (useRealApi) {
      const { data } = await http.post<MissionCompletion>('/mission-completions', input);
      return data;
    }

    return mockDb.completeMission(
      input.userId,
      input.missionId,
      input.contentId,
      input.photoUrl,
      input.memo,
    );
  },
};
