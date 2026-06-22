import { createPinia } from 'pinia';
import { createApp } from 'vue';
import App from './App.vue';
import { router } from './router';
import { setAuthTokenGetter } from './api/http';
import { useAuthStore } from './stores/auth';
import { useTravelStore } from './stores/travel';
import './styles/main.css';

const app = createApp(App);
const pinia = createPinia();

app.use(pinia);

const authStore = useAuthStore();
authStore.restoreSession();
setAuthTokenGetter(() => authStore.token);

const travelStore = useTravelStore();
void travelStore.loadInitialData(authStore.user?.id);

app.use(router);

window.addEventListener('hidden-busan:unauthorized', () => {
  authStore.logout();

  if (router.currentRoute.value.name !== 'login') {
    void router.push({
      name: 'login',
      query: {
        redirect: router.currentRoute.value.fullPath,
      },
    });
  }
});

app.mount('#app');
