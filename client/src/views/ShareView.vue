<script setup lang="ts">
import { Check, ChevronLeft, Copy, MapPin, Share2, Stamp } from 'lucide-vue-next';
import { computed, onMounted, ref } from 'vue';
import { useAuthStore } from '@/stores/auth';
import { useTravelStore } from '@/stores/travel';

const authStore = useAuthStore();
const travelStore = useTravelStore();
const copied = ref(false);

const shareUrl = computed(() => travelStore.share?.shareUrl ?? 'https://hiddenbusan.kr/share/preview');
const visitedPlaces = computed(() =>
  travelStore.completions
    .map((completion) => travelStore.getContentById(completion.contentId))
    .filter((place): place is NonNullable<typeof place> => Boolean(place)),
);

async function copyLink() {
  try {
    await navigator.clipboard.writeText(shareUrl.value);
  } catch {
    // Clipboard permissions vary by browser; the input still exposes the URL.
  }

  copied.value = true;
  window.setTimeout(() => {
    copied.value = false;
  }, 2200);
}

onMounted(async () => {
  if (authStore.user) {
    await travelStore.loadUserData(authStore.user.id);
    await travelStore.getOrCreateShare(authStore.user.id);
  }
});
</script>

<template>
  <main class="page-shell">
    <section class="container max-w-2xl py-10">
      <RouterLink to="/passport" class="mb-6 inline-flex items-center gap-1 text-sm text-[#4a6b8a] hover:text-[#0055b3]">
        <ChevronLeft class="h-4 w-4" /> 패스포트로 돌아가기
      </RouterLink>

      <div class="mb-8">
        <h1 class="text-3xl font-bold">나의 패스포트 공유</h1>
        <p class="mt-2 text-[#4a6b8a]">아래 링크로 나의 부산 여행 기록을 공유해보세요.</p>
      </div>

      <div class="mb-6 rounded-2xl border border-blue-100 bg-white p-5 shadow-sm">
        <p class="mb-3 flex items-center gap-2 text-sm font-semibold">
          <Share2 class="h-4 w-4 text-[#0055b3]" /> 공유 링크
        </p>
        <div class="flex gap-2">
          <input
            class="focus-ring min-w-0 flex-1 rounded-xl border border-blue-100 bg-[#f0f7ff] px-4 py-2.5 text-sm text-[#4a6b8a]"
            :value="shareUrl"
            readonly
          />
          <button
            class="inline-flex items-center gap-2 rounded-xl px-4 py-2.5 text-sm font-semibold text-white"
            :class="copied ? 'bg-green-500' : 'bg-[#0055b3] hover:bg-[#0044a0]'"
            @click="copyLink"
          >
            <Check v-if="copied" class="h-4 w-4" />
            <Copy v-else class="h-4 w-4" />
            {{ copied ? '복사됨' : '복사' }}
          </button>
        </div>
      </div>

      <div class="overflow-hidden rounded-2xl border border-blue-100 bg-white shadow-sm">
        <div class="bg-gradient-to-r from-[#0055b3] to-[#0099cc] p-6 text-white">
          <div class="mb-4 flex items-center gap-3">
            <img
              :src="authStore.user?.avatar"
              :alt="authStore.user?.nickname"
              class="h-12 w-12 rounded-full border-2 border-white/50 object-cover"
            />
            <div>
              <h2 class="font-bold">{{ authStore.user?.nickname }}</h2>
              <p class="text-xs text-blue-100">Hidden Busan 탐험가</p>
            </div>
          </div>
          <div class="flex gap-8">
            <div>
              <p class="text-2xl font-bold">{{ travelStore.completions.length }}</p>
              <p class="text-xs text-blue-100">완료 미션</p>
            </div>
            <div>
              <p class="text-2xl font-bold">{{ visitedPlaces.length }}</p>
              <p class="text-xs text-blue-100">방문 장소</p>
            </div>
          </div>
        </div>

        <div v-if="visitedPlaces.length" class="border-b border-blue-50 p-4">
          <p class="mb-2 flex items-center gap-1 text-xs text-[#4a6b8a]">
            <MapPin class="h-3 w-3" /> 방문한 장소
          </p>
          <div class="grid grid-cols-4 gap-2">
            <img
              v-for="place in visitedPlaces.slice(0, 4)"
              :key="place.id"
              :src="place.image"
              :alt="place.name"
              class="h-16 w-full rounded-lg object-cover"
            />
          </div>
        </div>

        <div class="border-b border-blue-50 p-4">
          <p class="mb-2 flex items-center gap-1 text-xs text-[#4a6b8a]">
            <Stamp class="h-3 w-3" /> 수집한 스탬프
          </p>
          <div class="flex flex-wrap gap-2">
            <div
              v-for="completion in travelStore.completions"
              :key="completion.id"
              class="grid h-9 w-9 place-items-center rounded-lg bg-[#e8f4fd] text-[#0055b3]"
              :title="travelStore.getContentById(completion.contentId)?.name"
            >
              <Stamp class="h-4 w-4" />
            </div>
            <p v-if="travelStore.completions.length === 0" class="text-xs text-[#4a6b8a]">아직 스탬프가 없어요.</p>
          </div>
        </div>

        <div v-if="travelStore.completions.some((completion) => completion.memo)" class="p-4">
          <p class="mb-2 text-xs text-[#4a6b8a]">한 줄 기록</p>
          <div class="space-y-2">
            <div v-for="completion in travelStore.completions.filter((item) => item.memo).slice(0, 3)" :key="completion.id" class="flex gap-2">
              <span class="shrink-0 text-xs text-[#0099cc]">{{ travelStore.getContentById(completion.contentId)?.name }}</span>
              <p class="text-xs italic text-[#0d1b2a]">{{ completion.memo }}</p>
            </div>
          </div>
        </div>
      </div>
    </section>
  </main>
</template>
