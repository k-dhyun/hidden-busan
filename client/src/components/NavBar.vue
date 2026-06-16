<script setup lang="ts">
import { Menu, UserRound, X } from 'lucide-vue-next';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';

const router = useRouter();
const authStore = useAuthStore();
const open = ref(false);

const links = [
  { label: '홈', to: '/' },
  { label: '지도', to: '/map' },
  { label: '장소', to: '/places' },
  { label: '패스포트', to: '/passport' },
];

function logout() {
  authStore.logout();
  open.value = false;
  void router.push('/');
}
</script>

<template>
  <header class="fixed inset-x-0 top-0 z-40 border-b border-blue-100 bg-white/90 backdrop-blur">
    <nav class="container flex h-16 items-center justify-between">
      <RouterLink to="/" class="flex items-center gap-2 font-bold tracking-tight">
        <span class="grid h-9 w-9 place-items-center rounded-xl bg-[#0055b3] text-white">HB</span>
        <span>Hidden <span class="text-[#0055b3]">Busan</span></span>
      </RouterLink>

      <div class="hidden items-center gap-1 md:flex">
        <RouterLink
          v-for="link in links"
          :key="link.to"
          :to="link.to"
          class="rounded-lg px-3 py-2 text-sm text-[#4a6b8a] transition hover:bg-[#e8f4fd] hover:text-[#0055b3]"
          active-class="bg-[#e8f4fd] text-[#0055b3]"
        >
          {{ link.label }}
        </RouterLink>
      </div>

      <div class="hidden items-center gap-2 md:flex">
        <template v-if="authStore.isLoggedIn && authStore.user">
          <RouterLink to="/passport" class="flex items-center gap-2 rounded-xl bg-[#f0f7ff] px-3 py-2 text-sm">
            <img :src="authStore.user.avatar" :alt="authStore.user.nickname" class="h-7 w-7 rounded-full object-cover" />
            {{ authStore.user.nickname }}
          </RouterLink>
          <button class="rounded-xl px-3 py-2 text-sm text-[#4a6b8a] hover:bg-[#e8f4fd]" @click="logout">
            로그아웃
          </button>
        </template>
        <template v-else>
          <RouterLink to="/login" class="rounded-xl px-4 py-2 text-sm text-[#0055b3] hover:bg-[#e8f4fd]">
            로그인
          </RouterLink>
          <RouterLink to="/signup" class="rounded-xl bg-[#0055b3] px-4 py-2 text-sm text-white hover:bg-[#0044a0]">
            회원가입
          </RouterLink>
        </template>
      </div>

      <button class="rounded-xl p-2 md:hidden" aria-label="메뉴 열기" @click="open = !open">
        <X v-if="open" class="h-5 w-5" />
        <Menu v-else class="h-5 w-5" />
      </button>
    </nav>

    <div v-if="open" class="border-t border-blue-100 bg-white md:hidden">
      <div class="container py-3">
        <RouterLink
          v-for="link in links"
          :key="link.to"
          :to="link.to"
          class="block rounded-lg px-3 py-3 text-sm text-[#4a6b8a]"
          active-class="bg-[#e8f4fd] text-[#0055b3]"
          @click="open = false"
        >
          {{ link.label }}
        </RouterLink>
        <div class="mt-2 border-t border-blue-50 pt-2">
          <template v-if="authStore.isLoggedIn && authStore.user">
            <div class="flex items-center gap-2 px-3 py-2 text-sm">
              <UserRound class="h-4 w-4 text-[#0055b3]" />
              {{ authStore.user.nickname }}
            </div>
            <button class="block w-full rounded-lg px-3 py-3 text-left text-sm text-[#4a6b8a]" @click="logout">
              로그아웃
            </button>
          </template>
          <template v-else>
            <RouterLink to="/login" class="block rounded-lg px-3 py-3 text-sm text-[#0055b3]" @click="open = false">
              로그인
            </RouterLink>
            <RouterLink to="/signup" class="block rounded-lg px-3 py-3 text-sm text-[#0055b3]" @click="open = false">
              회원가입
            </RouterLink>
          </template>
        </div>
      </div>
    </div>
  </header>
</template>
