<script setup lang="ts">
import { CheckCircle, Clock, Share2, Trophy } from 'lucide-vue-next';
import { computed, onMounted, ref } from 'vue';
import { useAuthStore } from '@/stores/auth';
import { useTravelStore } from '@/stores/travel';
import type { Achievement, Content } from '@/types/domain';

type Tab = '전체' | '완료' | '진행중' | '도전과제';

const authStore = useAuthStore();
const travelStore = useTravelStore();
const activeTab = ref<Tab>('전체');

const achievementEmojiMap: Record<string, string> = {
  fish: '🐟',
  waves: '🌊',
  map: '🗺️',
  party: '🎭',
  shopping: '🛍️',
  sunrise: '🌅',
  sailboat: '⛵',
};

const unlockedAchievements = computed(
  () => travelStore.achievements.filter((achievement) => travelStore.getAchievementProgress(achievement).unlocked).length,
);

const achievementBadges = computed(() =>
  travelStore.achievements.map((achievement) => ({
    achievement,
    emoji: achievementEmoji(achievement),
    unlocked: travelStore.getAchievementProgress(achievement).unlocked,
  })),
);

const collectedStampSlots = computed(() => [
  ...travelStore.completions.map((completion) => {
    const badge = primaryAchievementBadgeForContent(completion.contentId);
    const content = travelStore.getContentById(completion.contentId);

    return {
      type: 'stamp' as const,
      id: completion.id,
      emoji: badge?.emoji ?? '🏆',
      title: badge?.achievement.title ?? content?.name ?? '수집한 스탬프',
    };
  }),
  {
    type: 'empty' as const,
    id: 'next-stamp-slot',
    emoji: '',
    title: '다음 스탬프 자리',
  },
]);

const tabs = computed(() => [
  { label: '전체' as Tab, count: travelStore.completions.length + travelStore.inProgressMissions.length },
  { label: '완료' as Tab, count: travelStore.completions.length },
  { label: '진행중' as Tab, count: travelStore.inProgressMissions.length },
  { label: '도전과제' as Tab, count: unlockedAchievements.value },
]);

const displayItems = computed(() => {
  if (activeTab.value === '완료') {
    return travelStore.completions.map((completion) => ({ type: 'completed' as const, completion }));
  }

  if (activeTab.value === '진행중') {
    return travelStore.inProgressMissions.map((mission) => ({ type: 'inProgress' as const, mission }));
  }

  if (activeTab.value === '도전과제') {
    return [];
  }

  return [
    ...travelStore.completions.map((completion) => ({ type: 'completed' as const, completion })),
    ...travelStore.inProgressMissions.map((mission) => ({ type: 'inProgress' as const, mission })),
  ];
});

onMounted(async () => {
  if (authStore.user) {
    await travelStore.loadUserData(authStore.user.id);
  }
});

function achievementEmoji(achievement: Achievement) {
  return achievementEmojiMap[achievement.icon] ?? '🏆';
}

function contentMatchesAchievement(content: Content, achievement: Achievement) {
  const conditionValue = String(achievement.condition.value);

  if (achievement.condition.type === 'category') {
    return content.category === conditionValue;
  }

  if (achievement.condition.type === 'region_any') {
    return conditionValue.split(',').includes(content.region);
  }

  if (achievement.condition.type === 'difficulty') {
    return content.difficulty === conditionValue;
  }

  return true;
}

function achievementBadgesForContent(contentId: string) {
  const content = travelStore.getContentById(contentId);

  if (!content) {
    return [];
  }

  const specificAchievements = travelStore.achievements.filter(
    (achievement) => achievement.condition.type !== 'count_total' && contentMatchesAchievement(content, achievement),
  );

  const fallbackAchievements = travelStore.achievements.filter(
    (achievement) => achievement.condition.type === 'count_total',
  );

  return (specificAchievements.length ? specificAchievements : fallbackAchievements).map((achievement) => ({
    achievement,
    emoji: achievementEmoji(achievement),
  }));
}

function primaryAchievementBadgeForContent(contentId: string) {
  return achievementBadgesForContent(contentId)[0];
}
</script>

<template>
  <main class="page-shell">
    <section class="container py-10">
      <div class="mb-8 flex flex-col gap-4 sm:flex-row sm:items-end sm:justify-between">
        <div>
          <h1 class="text-3xl font-bold">마이 패스포트</h1>
          <p class="mt-2 text-[#4a6b8a]">나만의 부산 여행 기록을 확인해보세요.</p>
        </div>
        <RouterLink
          to="/share"
          class="inline-flex items-center justify-center gap-2 rounded-xl bg-[#0055b3] px-5 py-3 text-sm font-semibold text-white hover:bg-[#0044a0]"
        >
          <Share2 class="h-4 w-4" /> 공유 링크 만들기
        </RouterLink>
      </div>

      <div class="mb-6 overflow-hidden rounded-2xl bg-gradient-to-r from-[#0055b3] to-[#0099cc] p-6 text-white shadow-sm">
        <div class="flex items-center gap-4">
          <img
            :src="authStore.user?.avatar"
            :alt="authStore.user?.nickname"
            class="h-14 w-14 rounded-full border-2 border-white/50 object-cover"
          />
          <div>
            <h2 class="text-xl font-bold">{{ authStore.user?.nickname }}</h2>
            <p class="text-sm text-blue-100">부산 탐험 중 · {{ authStore.user?.createdAt }} 가입</p>
          </div>
        </div>

        <div class="mt-5">
          <p class="mb-3 text-sm font-semibold text-blue-100">수집한 스탬프</p>
          <div class="flex max-w-[59.5rem] flex-wrap gap-2">
            <div
              v-for="slot in collectedStampSlots"
              :key="slot.id"
              class="grid h-14 w-14 place-items-center rounded-xl border text-2xl transition"
              :class="
                slot.type === 'stamp'
                  ? 'border-white/35 bg-white/20 shadow-sm'
                  : 'border-dashed border-white/30 bg-white/10'
              "
              :title="slot.title"
            >
              <span v-if="slot.type === 'stamp'">{{ slot.emoji }}</span>
            </div>
          </div>
        </div>

        <div class="mt-7 border-t border-white/20 pt-6">
          <div class="mb-2 flex items-center justify-between gap-3">
            <p class="text-xs text-blue-200">획득한 업적</p>
            <p class="text-xs text-blue-100">{{ unlockedAchievements }} / {{ travelStore.achievements.length }}</p>
          </div>
          <div class="flex flex-wrap gap-2">
            <div
              v-for="badge in achievementBadges"
              :key="badge.achievement.id"
              class="grid h-11 w-11 place-items-center rounded-xl border text-xl transition"
              :class="
                badge.unlocked
                  ? 'border-amber-200 bg-amber-400 shadow-sm'
                  : 'border-white/20 bg-white/10 opacity-45'
              "
              :title="badge.achievement.title"
            >
              {{ badge.emoji }}
            </div>
          </div>
        </div>
      </div>

      <div class="mb-6 grid grid-cols-3 gap-4">
        <div class="rounded-2xl border border-blue-100 bg-white p-4 text-center shadow-sm">
          <CheckCircle class="mx-auto mb-2 h-8 w-8 rounded-xl bg-blue-50 p-1.5 text-[#0055b3]" />
          <p class="text-2xl font-bold">{{ travelStore.completions.length }}</p>
          <p class="text-xs text-[#4a6b8a]">완료한 미션</p>
        </div>
        <div class="rounded-2xl border border-blue-100 bg-white p-4 text-center shadow-sm">
          <Clock class="mx-auto mb-2 h-8 w-8 rounded-xl bg-cyan-50 p-1.5 text-[#0099cc]" />
          <p class="text-2xl font-bold">{{ travelStore.inProgressMissions.length }}</p>
          <p class="text-xs text-[#4a6b8a]">진행중 미션</p>
        </div>
        <div class="rounded-2xl border border-blue-100 bg-white p-4 text-center shadow-sm">
          <Trophy class="mx-auto mb-2 h-8 w-8 rounded-xl bg-amber-50 p-1.5 text-amber-500" />
          <p class="text-2xl font-bold">{{ unlockedAchievements }}</p>
          <p class="text-xs text-[#4a6b8a]">획득한 업적</p>
        </div>
      </div>

      <div class="mb-6 flex gap-1 rounded-xl border border-blue-100 bg-white p-1 shadow-sm">
        <button
          v-for="tab in tabs"
          :key="tab.label"
          class="flex-1 rounded-lg py-2 text-xs transition sm:text-sm"
          :class="activeTab === tab.label ? 'bg-[#0055b3] font-semibold text-white' : 'text-[#4a6b8a] hover:text-[#0055b3]'"
          @click="activeTab = tab.label"
        >
          {{ tab.label }} <span class="opacity-70">({{ tab.count }})</span>
        </button>
      </div>

      <div v-if="activeTab === '도전과제'" class="grid gap-4 sm:grid-cols-2">
        <article
          v-for="achievement in travelStore.achievements"
          :key="achievement.id"
          class="rounded-2xl border-2 bg-white p-5 shadow-sm"
          :class="travelStore.getAchievementProgress(achievement).unlocked ? 'border-amber-400 bg-amber-50/50' : 'border-blue-100'"
        >
          <div class="flex items-start gap-4">
            <div
              class="grid h-14 w-14 shrink-0 place-items-center rounded-xl text-2xl"
              :class="travelStore.getAchievementProgress(achievement).unlocked ? 'bg-amber-400' : 'bg-[#f0f7ff] opacity-70'"
            >
              {{ achievementEmoji(achievement) }}
            </div>
            <div class="min-w-0 flex-1">
              <div class="mb-1 flex flex-wrap items-center gap-2">
                <h3 class="font-bold">{{ achievement.title }}</h3>
                <span v-if="travelStore.getAchievementProgress(achievement).unlocked" class="rounded-full bg-amber-400 px-2 py-0.5 text-xs text-white">
                  달성
                </span>
              </div>
              <p class="text-sm leading-6 text-[#4a6b8a]">{{ achievement.description }}</p>
              <div class="mt-3">
                <div class="mb-1 flex justify-between text-xs text-[#4a6b8a]">
                  <span>
                    {{ travelStore.getAchievementProgress(achievement).current }} /
                    {{ travelStore.getAchievementProgress(achievement).total }}
                  </span>
                  <span>
                    {{
                      Math.round(
                        (travelStore.getAchievementProgress(achievement).current /
                          travelStore.getAchievementProgress(achievement).total) *
                          100,
                      )
                    }}%
                  </span>
                </div>
                <div class="h-1.5 overflow-hidden rounded-full bg-[#e8f4fd]">
                  <div
                    class="h-full rounded-full"
                    :class="travelStore.getAchievementProgress(achievement).unlocked ? 'bg-amber-400' : 'bg-[#0055b3]'"
                    :style="{
                      width: `${Math.min(
                        100,
                        (travelStore.getAchievementProgress(achievement).current /
                          travelStore.getAchievementProgress(achievement).total) *
                          100,
                      )}%`,
                    }"
                  ></div>
                </div>
              </div>
            </div>
          </div>
        </article>
      </div>

      <div v-else class="space-y-4">
        <article
          v-for="item in displayItems"
          :key="item.type === 'completed' ? item.completion.id : item.mission.id"
          class="overflow-hidden rounded-2xl border border-blue-100 bg-white shadow-sm"
          :class="item.type === 'inProgress' ? 'opacity-75' : ''"
        >
          <template v-if="item.type === 'completed'">
            <div v-if="travelStore.getContentById(item.completion.contentId) && travelStore.getMissionById(item.completion.missionId)" class="flex">
              <div class="relative h-36 w-32 shrink-0 overflow-hidden sm:w-44">
                <img
                  :src="item.completion.photoUrl"
                  :alt="travelStore.getContentById(item.completion.contentId)?.name"
                  class="h-full w-full object-cover"
                />
                <div
                  v-if="primaryAchievementBadgeForContent(item.completion.contentId)"
                  class="absolute left-2.5 top-2.5 grid h-8 w-8 place-items-center rounded-full bg-amber-400 text-sm shadow-sm"
                  :title="primaryAchievementBadgeForContent(item.completion.contentId)?.achievement.title"
                >
                  {{ primaryAchievementBadgeForContent(item.completion.contentId)?.emoji }}
                </div>
              </div>
              <div class="flex flex-1 flex-col justify-between p-4">
                <div>
                  <div class="mb-2 flex flex-wrap items-center gap-2">
                    <span class="inline-flex items-center gap-1 rounded-full bg-green-100 px-2 py-0.5 text-xs text-green-700">
                      <CheckCircle class="h-3 w-3" /> 완료
                    </span>
                    <span class="text-xs text-[#4a6b8a]">{{ item.completion.completedAt }}</span>
                  </div>
                  <h3 class="font-bold">{{ travelStore.getContentById(item.completion.contentId)?.name }}</h3>
                  <p class="mt-1 text-xs text-[#4a6b8a]">{{ travelStore.getMissionById(item.completion.missionId)?.title }}</p>
                </div>
                <p v-if="item.completion.memo" class="mt-3 text-sm italic text-[#4a6b8a]">{{ item.completion.memo }}</p>
              </div>
            </div>
          </template>

          <template v-else>
            <div v-if="travelStore.getContentById(item.mission.contentId)" class="flex">
              <img
                :src="travelStore.getContentById(item.mission.contentId)?.image"
                :alt="travelStore.getContentById(item.mission.contentId)?.name"
                class="h-36 w-32 shrink-0 object-cover grayscale sm:w-44"
              />
              <div class="flex flex-1 flex-col justify-between p-4">
                <div>
                  <span class="inline-flex items-center gap-1 rounded-full bg-blue-100 px-2 py-0.5 text-xs text-[#0055b3]">
                    <Clock class="h-3 w-3" /> 진행중
                  </span>
                  <h3 class="mt-2 font-bold">{{ travelStore.getContentById(item.mission.contentId)?.name }}</h3>
                  <p class="mt-1 text-xs text-[#4a6b8a]">{{ item.mission.title }}</p>
                </div>
                <RouterLink :to="`/mission/${item.mission.id}`" class="mt-3 text-sm font-semibold text-[#0055b3]">
                  미션 수행하기
                </RouterLink>
              </div>
            </div>
          </template>
        </article>
      </div>
    </section>
  </main>
</template>
