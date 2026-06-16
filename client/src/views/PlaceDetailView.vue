<script setup lang="ts">
import { CheckCircle, ChevronLeft, Clock, Heart, Lock, MapPin, Send, Star, Sun, Target } from 'lucide-vue-next';
import { computed, onMounted, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import { useTravelStore } from '@/stores/travel';
import type { Difficulty } from '@/types/domain';

const route = useRoute();
const router = useRouter();
const authStore = useAuthStore();
const travelStore = useTravelStore();

const reviewRating = ref(5);
const reviewText = ref('');
const submitted = ref(false);

const placeId = computed(() => String(route.params.id ?? ''));
const place = computed(() => travelStore.getContentById(placeId.value));
const mission = computed(() => travelStore.getMissionByContentId(placeId.value));
const reviews = computed(() => travelStore.getReviewsByContentId(placeId.value));

function difficultyClass(difficulty: Difficulty) {
  return {
    쉬움: 'bg-green-100 text-green-700',
    보통: 'bg-amber-100 text-amber-700',
    어려움: 'bg-red-100 text-red-700',
  }[difficulty];
}

async function loadReviews() {
  if (placeId.value) {
    await travelStore.loadReviewsForContent(placeId.value);
  }
}

async function toggleFavorite() {
  if (!authStore.user) {
    await router.push({ name: 'login', query: { redirect: route.fullPath } });
    return;
  }

  await travelStore.toggleFavorite(authStore.user.id, placeId.value);
}

async function submitReview() {
  if (!authStore.user) {
    await router.push({ name: 'login', query: { redirect: route.fullPath } });
    return;
  }

  if (!reviewText.value.trim()) {
    return;
  }

  await travelStore.addReview(authStore.user.id, placeId.value, reviewRating.value, reviewText.value.trim());
  reviewText.value = '';
  reviewRating.value = 5;
  submitted.value = true;
  window.setTimeout(() => {
    submitted.value = false;
  }, 2500);
}

onMounted(loadReviews);
watch(placeId, loadReviews);
</script>

<template>
  <main class="page-shell">
    <section v-if="place" class="bg-[#f0f7ff]">
      <div class="relative h-72 overflow-hidden sm:h-96">
        <img :src="place.image" :alt="place.name" class="h-full w-full object-cover" />
        <div class="absolute inset-0 bg-gradient-to-t from-[#0d1b2a]/80 via-transparent to-[#0d1b2a]/20"></div>
        <button
          class="absolute left-4 top-4 inline-flex items-center gap-1 rounded-lg bg-white/90 px-3 py-2 text-sm text-[#0d1b2a]"
          @click="router.back()"
        >
          <ChevronLeft class="h-4 w-4" /> 뒤로
        </button>
        <div class="absolute bottom-6 left-6 right-6">
          <div class="mb-2 flex flex-wrap gap-2">
            <span class="rounded-full bg-[#0055b3] px-2 py-1 text-xs text-white">{{ place.category }}</span>
            <span class="rounded-full bg-white/90 px-2 py-1 text-xs text-[#0d1b2a]">{{ place.region }}</span>
          </div>
          <h1 class="text-3xl font-bold text-white">{{ place.name }}</h1>
          <p class="mt-2 max-w-2xl text-sm leading-6 text-blue-100">{{ place.description }}</p>
        </div>
      </div>

      <div class="container py-8">
        <div class="rounded-2xl border border-blue-100 bg-white p-6 shadow-sm">
          <div class="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
            <div>
              <p class="flex items-center gap-1.5 text-xs text-[#4a6b8a]"><MapPin class="h-4 w-4" /> 주소</p>
              <p class="mt-1 text-sm">{{ place.address }}</p>
            </div>
            <div>
              <p class="flex items-center gap-1.5 text-xs text-[#4a6b8a]"><Target class="h-4 w-4" /> 난이도</p>
              <span class="mt-1 inline-block rounded-full px-2 py-1 text-xs" :class="difficultyClass(place.difficulty)">
                {{ place.difficulty }}
              </span>
            </div>
            <div>
              <p class="flex items-center gap-1.5 text-xs text-[#4a6b8a]"><Clock class="h-4 w-4" /> 소요 시간</p>
              <p class="mt-1 text-sm">{{ place.duration }}</p>
            </div>
            <div>
              <p class="flex items-center gap-1.5 text-xs text-[#4a6b8a]"><Sun class="h-4 w-4" /> 추천 시간</p>
              <p class="mt-1 text-sm">{{ place.bestTime }}</p>
            </div>
          </div>

          <div class="mt-6 flex flex-col gap-3 sm:flex-row">
            <button
              class="inline-flex flex-1 items-center justify-center gap-2 rounded-xl border py-3 text-sm font-semibold"
              :class="
                travelStore.isFavorited(place.id) && authStore.isLoggedIn
                  ? 'border-red-200 bg-red-50 text-red-500'
                  : 'border-blue-200 text-[#4a6b8a] hover:border-red-300 hover:text-red-400'
              "
              @click="toggleFavorite"
            >
              <Heart class="h-4 w-4" :class="travelStore.isFavorited(place.id) ? 'fill-current' : ''" />
              {{ travelStore.isFavorited(place.id) && authStore.isLoggedIn ? '찜 완료' : '찜하기' }}
              <Lock v-if="!authStore.isLoggedIn" class="h-3.5 w-3.5 opacity-60" />
            </button>

            <RouterLink
              v-if="mission"
              :to="authStore.isLoggedIn ? `/mission/${mission.id}` : { name: 'login', query: { redirect: `/places/${place.id}` } }"
              class="inline-flex flex-1 items-center justify-center gap-2 rounded-xl bg-[#0055b3] px-6 py-3 text-sm font-semibold text-white hover:bg-[#0044a0] sm:flex-none"
            >
              <Target class="h-4 w-4" /> 미션 완료하기
            </RouterLink>
          </div>
        </div>

        <div v-if="mission" class="mt-6 rounded-2xl bg-gradient-to-r from-[#0055b3] to-[#0099cc] p-6 text-white shadow-sm">
          <p class="mb-2 flex items-center gap-2 text-sm text-blue-100"><Target class="h-4 w-4" /> 도전 미션</p>
          <h2 class="text-xl font-bold">{{ mission.title }}</h2>
          <p class="mt-2 text-sm leading-6 text-blue-100">{{ mission.description }}</p>
        </div>

        <div class="mt-6 rounded-2xl border border-blue-100 bg-white p-6 shadow-sm">
          <div class="mb-5 flex items-center justify-between">
            <div>
              <h2 class="text-xl font-bold">리뷰</h2>
              <p class="mt-1 text-sm text-[#4a6b8a]">{{ reviews.length }}개의 리뷰가 있습니다.</p>
            </div>
          </div>

          <div class="mb-6 rounded-xl bg-[#f0f7ff] p-4">
            <div class="mb-3 flex items-center gap-2">
              <button
                v-for="rating in [1, 2, 3, 4, 5]"
                :key="rating"
                class="text-amber-400"
                @click="reviewRating = rating"
              >
                <Star class="h-5 w-5" :class="rating <= reviewRating ? 'fill-current' : ''" />
              </button>
            </div>
            <textarea
              v-model="reviewText"
              class="focus-ring min-h-24 w-full resize-none rounded-xl border border-blue-100 bg-white p-3 text-sm"
              placeholder="이 장소에 대한 경험을 남겨보세요."
            ></textarea>
            <div class="mt-3 flex items-center justify-between">
              <p v-if="submitted" class="flex items-center gap-1 text-sm text-green-600">
                <CheckCircle class="h-4 w-4" /> 리뷰가 등록되었습니다.
              </p>
              <span v-else></span>
              <button
                class="inline-flex items-center gap-2 rounded-xl bg-[#0055b3] px-4 py-2 text-sm font-semibold text-white"
                @click="submitReview"
              >
                <Send class="h-4 w-4" /> 등록
              </button>
            </div>
          </div>

          <div class="space-y-4">
            <article v-for="review in reviews" :key="review.id" class="rounded-xl border border-blue-100 p-4">
              <div class="mb-2 flex items-center justify-between gap-3">
                <div class="flex gap-1 text-amber-400">
                  <Star v-for="rating in 5" :key="rating" class="h-4 w-4" :class="rating <= review.rating ? 'fill-current' : ''" />
                </div>
                <span class="text-xs text-[#4a6b8a]">{{ review.createdAt }}</span>
              </div>
              <p class="text-sm leading-6 text-[#0d1b2a]">{{ review.comment }}</p>
            </article>
          </div>
        </div>
      </div>
    </section>

    <section v-else class="container py-20 text-center">
      <p class="text-[#4a6b8a]">장소를 찾을 수 없어요.</p>
      <RouterLink to="/places" class="mt-4 inline-block text-[#0055b3] hover:underline">목록으로 돌아가기</RouterLink>
    </section>
  </main>
</template>
