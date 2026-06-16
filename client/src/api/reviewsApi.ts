import { http, useRealApi } from './http';
import { mockDb } from './mockDb';
import type { Review } from '@/types/domain';

export const reviewsApi = {
  async listReviews(contentId: string): Promise<Review[]> {
    if (useRealApi) {
      const { data } = await http.get<Review[]>(`/contents/${contentId}/reviews`);
      return data;
    }

    return mockDb.listReviews(contentId);
  },
  async addReview(input: {
    userId: string;
    contentId: string;
    rating: number;
    comment: string;
  }): Promise<Review> {
    if (useRealApi) {
      const { data } = await http.post<Review>(`/contents/${input.contentId}/reviews`, input);
      return data;
    }

    return mockDb.addReview(input.userId, input.contentId, input.rating, input.comment);
  },
};
