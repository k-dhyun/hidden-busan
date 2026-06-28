import axios from 'axios';

let authTokenGetter: () => string | null = () => null;

export function setAuthTokenGetter(getter: () => string | null) {
  authTokenGetter = getter;
}

export const http = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080/api',
  timeout: 180000,
});

http.interceptors.request.use((config) => {
  const token = authTokenGetter();

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

http.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error?.response?.status === 401) {
      window.dispatchEvent(new CustomEvent('hidden-busan:unauthorized'));
    }

    return Promise.reject(error);
  },
);

export const useRealApi = import.meta.env.VITE_USE_REAL_API === 'true';
