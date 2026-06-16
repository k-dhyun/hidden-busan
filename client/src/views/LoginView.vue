<script setup lang="ts">
import { AlertCircle, Eye, EyeOff, Lock, Mail } from 'lucide-vue-next';
import { ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import { useTravelStore } from '@/stores/travel';

const route = useRoute();
const router = useRouter();
const authStore = useAuthStore();
const travelStore = useTravelStore();

const email = ref('');
const password = ref('');
const showPassword = ref(false);
const error = ref('');

async function submit() {
  error.value = '';

  if (!email.value || !password.value) {
    error.value = '이메일과 비밀번호를 입력해주세요.';
    return;
  }

  try {
    await authStore.login(email.value, password.value);
    if (authStore.user) {
      await travelStore.loadUserData(authStore.user.id);
    }
    await router.push(String(route.query.redirect ?? '/'));
  } catch {
    error.value = '로그인에 실패했습니다.';
  }
}

async function demoLogin() {
  email.value = 'traveler@busan.kr';
  password.value = 'password';
  await submit();
}
</script>

<template>
  <main class="page-shell flex items-center justify-center px-4 py-10">
    <section class="w-full max-w-md">
      <div class="mb-8 text-center">
        <RouterLink to="/" class="mb-4 inline-flex items-center gap-2 font-bold text-xl">
          <span class="grid h-10 w-10 place-items-center rounded-xl bg-[#0055b3] text-white">HB</span>
          Hidden <span class="text-[#0055b3]">Busan</span>
        </RouterLink>
        <h1 class="text-2xl font-bold">로그인</h1>
        <p class="mt-1 text-sm text-[#4a6b8a]">부산 탐험을 이어가세요.</p>
      </div>

      <form class="rounded-2xl border border-blue-100 bg-white p-6 shadow-sm" @submit.prevent="submit">
        <label class="block">
          <span class="mb-1.5 block text-sm font-semibold">이메일</span>
          <span class="relative block">
            <Mail class="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-[#4a6b8a]" />
            <input
              v-model="email"
              class="focus-ring w-full rounded-xl border border-blue-100 bg-[#f0f7ff] py-3 pl-10 pr-4 text-sm"
              placeholder="traveler@busan.kr"
              type="email"
            />
          </span>
        </label>

        <label class="mt-4 block">
          <span class="mb-1.5 block text-sm font-semibold">비밀번호</span>
          <span class="relative block">
            <Lock class="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-[#4a6b8a]" />
            <input
              v-model="password"
              class="focus-ring w-full rounded-xl border border-blue-100 bg-[#f0f7ff] py-3 pl-10 pr-10 text-sm"
              placeholder="6자 이상 입력하세요"
              :type="showPassword ? 'text' : 'password'"
            />
            <button type="button" class="absolute right-3 top-1/2 -translate-y-1/2 text-[#4a6b8a]" @click="showPassword = !showPassword">
              <EyeOff v-if="showPassword" class="h-4 w-4" />
              <Eye v-else class="h-4 w-4" />
            </button>
          </span>
        </label>

        <p v-if="error" class="mt-4 flex items-center gap-2 text-sm text-red-500">
          <AlertCircle class="h-4 w-4" /> {{ error }}
        </p>

        <button
          class="mt-6 w-full rounded-xl bg-[#0055b3] py-3 text-sm font-semibold text-white hover:bg-[#0044a0] disabled:opacity-60"
          :disabled="authStore.loading"
          type="submit"
        >
          {{ authStore.loading ? '로그인 중...' : '로그인' }}
        </button>

        <button
          class="mt-3 w-full rounded-xl border border-blue-200 py-3 text-sm font-semibold text-[#0055b3] hover:bg-[#e8f4fd]"
          type="button"
          @click="demoLogin"
        >
          데모 계정으로 시작
        </button>

        <p class="mt-5 text-center text-sm text-[#4a6b8a]">
          계정이 없나요?
          <RouterLink to="/signup" class="font-semibold text-[#0055b3]">회원가입</RouterLink>
        </p>
      </form>
    </section>
  </main>
</template>
