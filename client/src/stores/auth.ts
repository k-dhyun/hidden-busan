import { defineStore } from 'pinia';
import { authApi } from '@/api/authApi';
import type { User } from '@/types/domain';

interface AuthState {
  user: User | null;
  token: string | null;
  loading: boolean;
}

const STORAGE_KEY = 'hidden-busan:auth';

export const useAuthStore = defineStore('auth', {
  state: (): AuthState => ({
    user: null,
    token: null,
    loading: false,
  }),
  getters: {
    isLoggedIn: (state) => Boolean(state.token && state.user),
  },
  actions: {
    restoreSession() {
      if (typeof window === 'undefined') {
        return;
      }

      const raw = window.localStorage.getItem(STORAGE_KEY);
      if (!raw) {
        return;
      }

      try {
        const session = JSON.parse(raw) as Pick<AuthState, 'user' | 'token'>;
        this.user = session.user;
        this.token = session.token;
      } catch {
        window.localStorage.removeItem(STORAGE_KEY);
      }
    },
    persistSession() {
      if (typeof window !== 'undefined') {
        window.localStorage.setItem(
          STORAGE_KEY,
          JSON.stringify({
            user: this.user,
            token: this.token,
          }),
        );
      }
    },
    async login(email: string, password: string) {
      this.loading = true;

      try {
        const result = await authApi.login(email, password);
        this.user = result.user;
        this.token = result.token;
        this.persistSession();
        return true;
      } finally {
        this.loading = false;
      }
    },
    async signup(nickname: string, email: string, password: string) {
      this.loading = true;

      try {
        const result = await authApi.signup(nickname, email, password);
        this.user = result.user;
        this.token = result.token;
        this.persistSession();
        return true;
      } finally {
        this.loading = false;
      }
    },
    logout() {
      this.user = null;
      this.token = null;

      if (typeof window !== 'undefined') {
        window.localStorage.removeItem(STORAGE_KEY);
      }
    },
  },
});
