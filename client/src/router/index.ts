import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '@/views/HomeView.vue';
import LoginView from '@/views/LoginView.vue';
import MapView from '@/views/MapView.vue';
import MissionView from '@/views/MissionView.vue';
import PassportView from '@/views/PassportView.vue';
import PlaceDetailView from '@/views/PlaceDetailView.vue';
import PlacesView from '@/views/PlacesView.vue';
import ShareView from '@/views/ShareView.vue';
import SignupView from '@/views/SignupView.vue';
import { useAuthStore } from '@/stores/auth';

export const router = createRouter({
  history: createWebHistory(),
  scrollBehavior: () => ({ top: 0 }),
  routes: [
    { path: '/', name: 'home', component: HomeView },
    { path: '/map', name: 'map', component: MapView },
    { path: '/places', name: 'places', component: PlacesView },
    { path: '/places/:id', name: 'place-detail', component: PlaceDetailView },
    { path: '/login', name: 'login', component: LoginView },
    { path: '/signup', name: 'signup', component: SignupView },
    { path: '/mission/:id', name: 'mission', component: MissionView, meta: { requiresAuth: true } },
    { path: '/passport', name: 'passport', component: PassportView, meta: { requiresAuth: true } },
    { path: '/share', name: 'share', component: ShareView, meta: { requiresAuth: true } },
    { path: '/:pathMatch(.*)*', redirect: '/' },
  ],
});

router.beforeEach((to) => {
  const authStore = useAuthStore();

  if (to.meta.requiresAuth && !authStore.isLoggedIn) {
    return {
      name: 'login',
      query: {
        redirect: to.fullPath,
      },
    };
  }

  return true;
});
