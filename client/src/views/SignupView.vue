<script setup lang="ts">
import { AlertCircle, CheckCircle, Eye, EyeOff, Lock, Mail, User } from 'lucide-vue-next';
import { computed, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import { useTravelStore } from '@/stores/travel';

const router = useRouter();
const authStore = useAuthStore();
const travelStore = useTravelStore();

const nickname = ref('');
const email = ref('');
const password = ref('');
const showPassword = ref(false);
const error = ref('');

const passwordHint = computed(() => {
  if (!password.value) {
    return '';
  }

  return password.value.length >= 6 ? '사용 가능한 비밀번호예요' : `${6 - password.value.length}자 더 입력해주세요`;
});

async function submit() {
  error.value = '';

  if (!nickname.value || !email.value || !password.value) {
    error.value = '모든 항목을 입력해주세요.';
    return;
  }

  if (password.value.length < 6) {
    error.value = '비밀번호는 6자 이상이어야 해요.';
    return;
  }

  try {
    await authStore.signup(nickname.value, email.value, password.value);
    if (authStore.user) {
      await travelStore.loadUserData(authStore.user.id);
    }
    await router.push('/');
  } catch {
    error.value = '회원가입에 실패했습니다.';
  }
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
        <h1 class="text-2xl font-bold">회원가입</h1>
        <p class="mt-1 text-sm text-[#4a6b8a]">부산 탐험을 시작해보세요.</p>
      </div>

      <form class="rounded-2xl border border-blue-100 bg-white p-6 shadow-sm" @submit.prevent="submit">
        <label class="block">
          <span class="mb-1.5 block text-sm font-semibold">닉네임</span>
          <span class="relative block">
            <User class="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-[#4a6b8a]" />
            <input
              v-model="nickname"
              class="focus-ring w-full rounded-xl border border-blue-100 bg-[#f0f7ff] py-3 pl-10 pr-4 text-sm"
              placeholder="부산탐험가"
              type="text"
            />
          </span>
        </label>

        <label class="mt-4 block">
          <span class="mb-1.5 block text-sm font-semibold">이메일</span>
          <span class="relative block">
            <Mail class="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-[#4a6b8a]" />
            <input
              v-model="email"
              class="focus-ring w-full rounded-xl border border-blue-100 bg-[#f0f7ff] py-3 pl-10 pr-4 text-sm"
              placeholder="me@example.com"
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

        <p v-if="passwordHint" class="mt-2 flex items-center gap-1 text-xs" :class="password.length >= 6 ? 'text-green-600' : 'text-[#4a6b8a]'">
          <CheckCircle class="h-3 w-3" /> {{ passwordHint }}
        </p>

        <p v-if="error" class="mt-4 flex items-center gap-2 text-sm text-red-500">
          <AlertCircle class="h-4 w-4" /> {{ error }}
        </p>

        <button
          class="mt-6 w-full rounded-xl bg-[#0055b3] py-3 text-sm font-semibold text-white hover:bg-[#0044a0] disabled:opacity-60"
          :disabled="authStore.loading"
          type="submit"
        >
          {{ authStore.loading ? '가입 중...' : '회원가입' }}
        </button>

        <p class="mt-5 text-center text-sm text-[#4a6b8a]">
          이미 계정이 있나요?
          <RouterLink to="/login" class="font-semibold text-[#0055b3]">로그인</RouterLink>
        </p>
      </form>
    </section>
  </main>
</template>
