<script setup lang="ts">
import { ArrowRight, MapPin, Share2, Stamp, Target, Waves } from 'lucide-vue-next';
import { computed } from 'vue';
import { useTravelStore } from '@/stores/travel';

const travelStore = useTravelStore();

const featuredPlaces = computed(() => travelStore.contents.slice(0, 3));
const completedCount = computed(() => travelStore.completions.length);
const heroImage = computed(() => featuredPlaces.value[0]?.image);
const heroBackground = computed(() => {
  const overlay = 'linear-gradient(180deg, rgba(9, 55, 96, 0.64) 0%, rgba(0, 85, 179, 0.72) 48%, rgba(8, 32, 58, 0.86) 100%)';

  if (!heroImage.value) {
    return 'linear-gradient(135deg, #0055b3 0%, #0099cc 100%)';
  }

  return `${overlay}, url("${heroImage.value}")`;
});

const heroStats = computed(() => [
  {
    value: travelStore.contents.length ? `${travelStore.contents.length}+` : '0',
    label: '숨은 장소',
  },
  {
    value: `${travelStore.missions.length}개`,
    label: '도전 미션',
  },
  {
    value: `${completedCount.value}개`,
    label: '완료된 미션',
  },
]);

const usageSteps = [
  {
    step: 'STEP 1',
    title: '지도에서 장소 찾기',
    description: '부산 곳곳에 숨겨진 로컬 명소를 인터랙티브 지도 위에서 발견하세요.',
    icon: MapPin,
    iconClass: 'bg-[#f0f7ff] text-[#0055b3]',
    to: '/map',
  },
  {
    step: 'STEP 2',
    title: '도전 미션 수행',
    description: '각 장소마다 준비된 특별한 미션을 완료하고 사진과 기록을 남겨보세요.',
    icon: Target,
    iconClass: 'bg-[#e8fbff] text-[#0099cc]',
    to: '/places',
  },
  {
    step: 'STEP 3',
    title: '패스포트 스탬프 모으기',
    description: '미션을 완료할 때마다 스탬프가 쌓여 나만의 부산 패스포트가 완성돼요.',
    icon: Stamp,
    iconClass: 'bg-[#edf8ff] text-[#0077aa]',
    to: '/passport',
  },
  {
    step: 'STEP 4',
    title: '공유 링크 만들기',
    description: '완성된 패스포트를 친구들과 공유하고 부산 여행을 함께 즐겨보세요.',
    icon: Share2,
    iconClass: 'bg-[#eef2ff] text-[#3158c9]',
    to: '/share',
  },
];
</script>

<template>
  <main class="page-shell bg-[#f0f7ff]">
    <section
      class="relative isolate overflow-hidden bg-[#0055b3] bg-cover bg-center text-white"
      :style="{ backgroundImage: heroBackground }"
    >
      <div class="container relative z-10 flex min-h-[calc(100vh-5rem)] flex-col items-center justify-center py-16 text-center sm:py-20">
        <div class="inline-flex items-center gap-2 rounded-full border border-white/35 bg-white/15 px-5 py-3 text-sm font-semibold text-white backdrop-blur sm:text-base">
          <Waves class="h-5 w-5" />
          부산의 숨겨진 이야기를 발견하세요
        </div>

        <h1 class="mt-8 max-w-5xl text-4xl font-extrabold leading-tight text-white sm:text-6xl lg:text-7xl">
          숨겨진 부산을 발견하는
          <span class="mt-1 block text-[#69d7ff]">특별한 여행</span>
        </h1>

        <p class="mt-7 max-w-2xl text-lg leading-8 text-blue-50 sm:text-xl">
          부산의 숨은 명소를 탐험하고, 도전 미션을 완료해 나만의 부산 패스포트를 완성해보세요.
        </p>

        <div class="mt-10 flex w-full max-w-2xl flex-col gap-4 sm:flex-row sm:justify-center">
          <RouterLink
            to="/map"
            class="inline-flex min-h-14 items-center justify-center gap-3 rounded-xl bg-[#0055b3] px-6 py-4 text-base font-bold text-white shadow-lg shadow-blue-950/20 transition hover:bg-[#0044a0]"
          >
            <MapPin class="h-6 w-6" />
            부산 지도에서 탐색하기
          </RouterLink>
          <RouterLink
            to="/passport"
            class="inline-flex min-h-14 items-center justify-center gap-3 rounded-xl border border-white/45 bg-white/15 px-6 py-4 text-base font-bold text-white backdrop-blur transition hover:bg-white/25"
          >
            <Stamp class="h-6 w-6" />
            내 패스포트 보기
          </RouterLink>
        </div>

        <div class="mt-12 grid w-full max-w-lg grid-cols-3 gap-4 sm:max-w-xl">
          <div v-for="stat in heroStats" :key="stat.label" class="text-center">
            <p class="text-3xl font-extrabold leading-none text-white sm:text-4xl">{{ stat.value }}</p>
            <p class="mt-2 text-sm font-medium text-blue-100 sm:text-base">{{ stat.label }}</p>
          </div>
        </div>
      </div>

      <div class="pointer-events-none absolute -bottom-10 left-1/2 h-20 w-[120%] -translate-x-1/2 rounded-[50%] bg-[#f0f7ff]" />
    </section>

    <section class="bg-[#f0f7ff] py-16 sm:py-20">
      <div class="container">
        <div class="mx-auto max-w-3xl text-center">
          <h2 class="text-3xl font-extrabold text-[#0d1b2a] sm:text-4xl">
            Hidden Busan 사용법
          </h2>
          <p class="mt-4 text-lg font-medium text-[#4a6b8a]">
            4단계로 나만의 부산 패스포트를 완성해보세요
          </p>
        </div>

        <div class="mt-12 grid gap-6 lg:grid-cols-2">
          <article
            v-for="step in usageSteps"
            :key="step.step"
            class="rounded-2xl border border-blue-100 bg-white p-6 shadow-sm transition hover:-translate-y-0.5 hover:shadow-md sm:p-8"
          >
            <div class="flex items-center gap-4">
              <div class="grid h-14 w-14 shrink-0 place-items-center rounded-2xl" :class="step.iconClass">
                <component :is="step.icon" class="h-7 w-7" />
              </div>
              <span class="rounded-full bg-[#eef6ff] px-4 py-2 text-sm font-bold text-[#4a6b8a]">
                {{ step.step }}
              </span>
            </div>

            <h3 class="mt-8 text-2xl font-extrabold text-[#0d1b2a]">{{ step.title }}</h3>
            <p class="mt-4 min-h-14 text-base leading-7 text-[#4a6b8a]">{{ step.description }}</p>
            <RouterLink
              :to="step.to"
              class="mt-7 inline-flex items-center gap-2 text-lg font-bold text-[#0055b3] hover:text-[#0044a0]"
            >
              시작하기
              <ArrowRight class="h-5 w-5" />
            </RouterLink>
          </article>
        </div>
      </div>
    </section>

    <section class="bg-white py-16 sm:py-20">
      <div class="container">
        <div class="mb-7 flex flex-col gap-4 sm:flex-row sm:items-end sm:justify-between">
          <div>
            <h2 class="text-3xl font-extrabold text-[#0d1b2a]">추천 장소</h2>
            <p class="mt-2 text-base text-[#4a6b8a]">지금 바로 도전할 수 있는 부산의 로컬 명소를 확인해보세요.</p>
          </div>
          <RouterLink to="/places" class="inline-flex items-center gap-1 text-sm font-bold text-[#0055b3]">
            전체 보기 <ArrowRight class="h-4 w-4" />
          </RouterLink>
        </div>

        <div class="grid gap-6 md:grid-cols-3">
          <RouterLink
            v-for="place in featuredPlaces"
            :key="place.id"
            :to="`/places/${place.id}`"
            class="overflow-hidden rounded-2xl border border-blue-100 bg-white shadow-sm transition hover:-translate-y-0.5 hover:shadow-md"
          >
            <img :src="place.image" :alt="place.name" class="h-56 w-full object-cover" />
            <div class="p-5">
              <div class="mb-3 flex flex-wrap gap-2">
                <span class="rounded-full bg-[#e8f4fd] px-2.5 py-1 text-xs font-semibold text-[#0055b3]">{{ place.category }}</span>
                <span class="rounded-full bg-blue-50 px-2.5 py-1 text-xs font-semibold text-[#4a6b8a]">{{ place.region }}</span>
              </div>
              <h3 class="text-lg font-extrabold text-[#0d1b2a]">{{ place.name }}</h3>
              <p class="mt-3 line-clamp-2 text-sm leading-6 text-[#4a6b8a]">{{ place.description }}</p>
              <div class="mt-4 flex items-center justify-between gap-3 text-sm font-semibold">
                <span class="text-amber-500">난이도: {{ place.difficulty }}</span>
                <span class="text-[#0055b3]">{{ place.duration }}</span>
              </div>
            </div>
          </RouterLink>
        </div>

        <div class="mt-16 rounded-2xl bg-gradient-to-r from-[#0055b3] to-[#0099cc] px-6 py-14 text-center text-white shadow-sm sm:px-10 sm:py-16">
          <h2 class="text-3xl font-extrabold leading-tight sm:text-5xl">
            지금 바로 부산 탐험을 시작해보세요
          </h2>
          <p class="mx-auto mt-6 max-w-2xl text-lg font-medium leading-8 text-blue-50">
            미션을 완료하고 스탬프를 모아 나만의 부산 패스포트를 완성하세요.
          </p>
          <RouterLink
            to="/map"
            class="mt-10 inline-flex min-h-16 items-center justify-center gap-3 rounded-xl bg-white px-8 py-4 text-lg font-extrabold !text-[#0055b3] shadow-sm transition hover:bg-blue-50"
          >
            <MapPin class="h-6 w-6 text-[#0055b3]" />
            지도에서 탐색 시작하기
          </RouterLink>
        </div>
      </div>
    </section>
  </main>
</template>
