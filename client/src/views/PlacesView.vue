<script setup lang="ts">
import { Clock, Heart, Lock, MapPin, Search } from 'lucide-vue-next';
import { computed, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import { useTravelStore } from '@/stores/travel';
import type { Category, Difficulty } from '@/types/domain';

const router = useRouter();
const authStore = useAuthStore();
const travelStore = useTravelStore();

const categories: Array<Category | '전체'> = ['전체', '명소', '축제', '쇼핑', '시장', '맛집'];
const activeCategory = ref<Category | '전체'>('전체');
const search = ref('');

const filteredPlaces = computed(() =>
  travelStore.contents.filter((place) => {
    const categoryMatched = activeCategory.value === '전체' || place.category === activeCategory.value;
    const searchMatched =
      !search.value ||
      place.name.includes(search.value) ||
      place.region.includes(search.value) ||
      place.description.includes(search.value);

    return categoryMatched && searchMatched;
  }),
);

function difficultyClass(difficulty: Difficulty) {
  return {
    쉬움: 'bg-green-100 text-green-700',
    보통: 'bg-amber-100 text-amber-700',
    어려움: 'bg-red-100 text-red-700',
  }[difficulty];
}

async function toggleFavorite(contentId: string) {
  if (!authStore.user) {
    await router.push({ name: 'login', query: { redirect: '/places' } });
    return;
  }

  await travelStore.toggleFavorite(authStore.user.id, contentId);
}
</script>

<template>
  <main class="page-shell">
    <section class="container py-10">
      <div class="mb-8">
        <h1 class="text-3xl font-bold">숨은 장소</h1>
        <p class="mt-2 text-[#4a6b8a]">부산의 숨겨진 매력을 찾아보세요.</p>
      </div>

      <div class="mb-8 rounded-2xl border border-blue-100 bg-white p-4 shadow-sm">
        <label class="relative block">
          <Search class="absolute left-3 top-1/2 h-5 w-5 -translate-y-1/2 text-[#4a6b8a]" />
          <input
            v-model="search"
            class="focus-ring w-full rounded-xl border border-blue-100 bg-[#f0f7ff] py-3 pl-10 pr-4 text-sm"
            placeholder="장소명, 지역, 설명을 검색하세요"
            type="search"
          />
        </label>

        <div class="mt-4 flex flex-wrap gap-2">
          <button
            v-for="category in categories"
            :key="category"
            class="rounded-full px-4 py-2 text-sm transition"
            :class="
              activeCategory === category
                ? 'bg-[#0055b3] text-white shadow-sm'
                : 'bg-[#f0f7ff] text-[#4a6b8a] hover:bg-[#e8f4fd] hover:text-[#0055b3]'
            "
            @click="activeCategory = category"
          >
            {{ category }}
          </button>
        </div>
      </div>

      <p class="mb-4 text-sm text-[#4a6b8a]">{{ filteredPlaces.length }}개의 장소</p>

      <div class="grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
        <article
          v-for="place in filteredPlaces"
          :key="place.id"
          class="overflow-hidden rounded-2xl border border-blue-100 bg-white shadow-sm transition hover:-translate-y-0.5 hover:shadow-md"
        >
          <div class="relative h-52 overflow-hidden">
            <img :src="place.image" :alt="place.name" class="h-full w-full object-cover transition duration-500 hover:scale-105" />
            <button
              class="absolute right-3 top-3 grid h-10 w-10 place-items-center rounded-full bg-white/90 text-[#4a6b8a] shadow-sm"
              :class="travelStore.isFavorited(place.id) ? 'text-red-500' : ''"
              :aria-label="`${place.name} 찜하기`"
              @click="toggleFavorite(place.id)"
            >
              <Heart class="h-5 w-5" :class="travelStore.isFavorited(place.id) ? 'fill-current' : ''" />
            </button>
          </div>

          <div class="p-5">
            <div class="mb-3 flex flex-wrap gap-2">
              <span class="rounded-full bg-[#e8f4fd] px-2 py-1 text-xs text-[#0055b3]">{{ place.category }}</span>
              <span class="rounded-full px-2 py-1 text-xs" :class="difficultyClass(place.difficulty)">
                {{ place.difficulty }}
              </span>
            </div>

            <h2 class="text-lg font-bold">{{ place.name }}</h2>
            <p class="mt-2 line-clamp-2 text-sm leading-6 text-[#4a6b8a]">{{ place.description }}</p>

            <div class="mt-4 space-y-2 text-sm text-[#4a6b8a]">
              <p class="flex items-center gap-2"><MapPin class="h-4 w-4" /> {{ place.region }}</p>
              <p class="flex items-center gap-2"><Clock class="h-4 w-4" /> {{ place.duration }}</p>
            </div>

            <div class="mt-5 flex items-center gap-2">
              <RouterLink
                :to="`/places/${place.id}`"
                class="flex-1 rounded-xl bg-[#0055b3] px-4 py-3 text-center text-sm font-semibold text-white hover:bg-[#0044a0]"
              >
                상세 보기
              </RouterLink>
              <Lock v-if="!authStore.isLoggedIn" class="h-4 w-4 text-[#4a6b8a]" />
            </div>
          </div>
        </article>
      </div>
    </section>
  </main>
</template>
