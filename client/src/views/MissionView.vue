<script setup lang="ts">
import { CheckCircle, ChevronLeft, FileText, Image, Sparkles, Stamp, Target, Upload } from 'lucide-vue-next';
import { computed, onBeforeUnmount, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { missionsApi } from '@/api/missionsApi';
import { useAuthStore } from '@/stores/auth';
import { useTravelStore } from '@/stores/travel';

const route = useRoute();
const router = useRouter();
const authStore = useAuthStore();
const travelStore = useTravelStore();

const photoPreviewUrl = ref('');
const completedPhotoUrl = ref('');
const photoFile = ref<File | null>(null);
const photoFileName = ref('');
const memo = ref('');
const submitted = ref(false);
const submitting = ref(false);
const errors = ref<{ photo?: string; memo?: string }>({});

const missionId = computed(() => String(route.params.id ?? ''));
const mission = computed(() => travelStore.getMissionById(missionId.value));
const place = computed(() => (mission.value ? travelStore.getContentById(mission.value.contentId) : undefined));
const submittedPhotoUrl = computed(() => completedPhotoUrl.value || photoPreviewUrl.value);
const allowedPhotoTypes = new Set(['image/jpeg', 'image/png', 'image/webp']);
const allowedPhotoExtensions = ['.jpg', '.jpeg', '.png', '.webp'];

function revokePhotoPreview() {
  if (photoPreviewUrl.value) {
    URL.revokeObjectURL(photoPreviewUrl.value);
    photoPreviewUrl.value = '';
  }
}

function validate() {
  const nextErrors: { photo?: string; memo?: string } = {};

  if (!photoFile.value) {
    nextErrors.photo = '사진을 선택해주세요.';
  }

  if (!memo.value.trim()) {
    nextErrors.memo = '한 줄 메모를 입력해주세요.';
  }

  errors.value = nextErrors;
  return Object.keys(nextErrors).length === 0;
}

function isAllowedPhoto(file: File) {
  const lowerName = file.name.toLowerCase();
  const hasAllowedExtension = allowedPhotoExtensions.some((extension) => lowerName.endsWith(extension));

  return allowedPhotoTypes.has(file.type) && hasAllowedExtension;
}

function resetPhotoInput(input?: HTMLInputElement) {
  revokePhotoPreview();
  completedPhotoUrl.value = '';
  photoFile.value = null;
  photoFileName.value = '';

  if (input) {
    input.value = '';
  }
}

function handlePhotoChange(event: Event) {
  const input = event.target as HTMLInputElement;
  const file = input.files?.[0];

  if (!file) {
    resetPhotoInput();
    return;
  }

  if (!isAllowedPhoto(file)) {
    resetPhotoInput(input);
    errors.value = { ...errors.value, photo: 'JPG/PNG/WebP 사진을 선택해주세요.' };
    return;
  }

  revokePhotoPreview();
  completedPhotoUrl.value = '';
  photoFile.value = file;
  photoFileName.value = file.name;
  photoPreviewUrl.value = URL.createObjectURL(file);
  errors.value = { ...errors.value, photo: undefined };
}

async function completeMission() {
  if (!validate() || !authStore.user || !mission.value || !place.value || !photoFile.value || submitting.value) {
    return;
  }

  submitting.value = true;

  try {
    const { photoUrl } = await missionsApi.uploadMissionPhoto(photoFile.value);

    await travelStore.completeMission({
      userId: authStore.user.id,
      missionId: mission.value.id,
      contentId: place.value.id,
      photoUrl,
      memo: memo.value,
    });

    completedPhotoUrl.value = photoUrl;
    revokePhotoPreview();
    submitted.value = true;
  } catch {
    errors.value = { ...errors.value, photo: '사진 업로드에 실패했습니다. 다시 시도해주세요.' };
  } finally {
    submitting.value = false;
  }
}

onBeforeUnmount(() => {
  revokePhotoPreview();
});
</script>

<template>
  <main class="page-shell">
    <section v-if="mission && place && submitted" class="container flex min-h-[calc(100vh-4rem)] items-center justify-center py-10">
      <div class="w-full max-w-md text-center">
        <div class="relative mb-6 inline-flex h-32 w-32 items-center justify-center rounded-full bg-[#0055b3] text-white shadow-lg">
          <Stamp class="h-16 w-16" />
          <span class="absolute -right-1 -top-1 grid h-8 w-8 place-items-center rounded-full bg-amber-400">
            <Sparkles class="h-4 w-4" />
          </span>
        </div>
        <h1 class="text-3xl font-bold">미션 완료</h1>
        <p class="mt-2 text-[#4a6b8a]">새로운 스탬프가 패스포트에 추가되었습니다.</p>
        <p class="mt-2 font-semibold text-[#0055b3]">{{ place.name }} · {{ mission.title }}</p>

        <div class="mt-6 overflow-hidden rounded-2xl border border-blue-100 bg-white text-left shadow-sm">
          <img :src="submittedPhotoUrl" :alt="mission.title" class="h-48 w-full object-cover" />
          <p class="p-4 text-sm italic text-[#4a6b8a]">{{ memo }}</p>
        </div>

        <div class="mt-6 flex flex-col gap-3 sm:flex-row">
          <RouterLink to="/passport" class="flex-1 rounded-xl bg-[#0055b3] px-5 py-3 text-sm font-semibold text-white">
            내 패스포트 보기
          </RouterLink>
          <RouterLink to="/places" class="flex-1 rounded-xl border border-blue-200 px-5 py-3 text-sm font-semibold text-[#0055b3]">
            다른 장소 탐색
          </RouterLink>
        </div>
      </div>
    </section>

    <section v-else-if="mission && place" class="container py-10">
      <button class="mb-6 inline-flex items-center gap-1 text-sm text-[#4a6b8a]" @click="router.back()">
        <ChevronLeft class="h-4 w-4" /> 뒤로 가기
      </button>

      <div class="mx-auto max-w-xl">
        <div class="mb-6 flex items-center gap-3">
          <img :src="place.image" :alt="place.name" class="h-14 w-14 rounded-xl object-cover" />
          <div>
            <span class="rounded-full bg-[#e8f4fd] px-2 py-0.5 text-xs text-[#0055b3]">{{ place.region }}</span>
            <h1 class="mt-1 font-bold">{{ place.name }}</h1>
          </div>
        </div>

        <div class="mb-6 rounded-2xl bg-gradient-to-r from-[#0055b3] to-[#0099cc] p-5 text-white">
          <p class="mb-2 flex items-center gap-2 text-sm text-blue-100"><Target class="h-4 w-4" /> 도전 미션</p>
          <h2 class="text-xl font-bold">{{ mission.title }}</h2>
          <p class="mt-1 text-sm leading-6 text-blue-100">{{ mission.description }}</p>
        </div>

        <div class="rounded-2xl border border-blue-100 bg-white p-6 shadow-sm">
          <h2 class="text-2xl font-bold">미션 완료</h2>
          <p class="mt-1 text-sm text-[#4a6b8a]">사진과 한 줄 기록을 남겨보세요.</p>

          <div class="mt-6">
            <label class="mb-2 flex items-center gap-2 text-sm font-semibold">
              <Image class="h-4 w-4 text-[#0055b3]" /> 인증 사진
            </label>
            <label
              class="focus-ring flex cursor-pointer flex-col items-center justify-center rounded-xl border border-dashed bg-[#f0f7ff] px-4 py-6 text-center transition hover:bg-[#e8f4fd]"
              :class="errors.photo ? 'border-red-300' : 'border-blue-200'"
              tabindex="0"
            >
              <input
                class="sr-only"
                accept="image/jpeg,image/png,image/webp"
                capture="environment"
                type="file"
                @change="handlePhotoChange"
              />
              <Upload class="h-8 w-8 text-[#0055b3]" />
              <span class="mt-3 text-sm font-semibold text-[#0055b3]">
                {{ photoFileName || '사진 선택하기' }}
              </span>
              <span class="mt-1 text-xs text-[#4a6b8a]">JPG, PNG, WebP 사진을 선택하세요.</span>
            </label>
            <p v-if="errors.photo" class="mt-1 text-xs text-red-500">{{ errors.photo }}</p>
            <img v-if="photoPreviewUrl" :src="photoPreviewUrl" alt="미리보기" class="mt-3 h-40 w-full rounded-xl object-cover" />
          </div>

          <div class="mt-5">
            <label class="mb-2 flex items-center gap-2 text-sm font-semibold">
              <FileText class="h-4 w-4 text-[#0055b3]" /> 한 줄 메모
            </label>
            <textarea
              v-model="memo"
              class="focus-ring min-h-28 w-full resize-none rounded-xl border bg-[#f0f7ff] px-4 py-3 text-sm"
              :class="errors.memo ? 'border-red-300' : 'border-blue-100'"
              placeholder="오늘의 부산 기록을 남겨보세요."
            ></textarea>
            <p v-if="errors.memo" class="mt-1 text-xs text-red-500">{{ errors.memo }}</p>
          </div>

          <button
            class="mt-6 inline-flex w-full items-center justify-center gap-2 rounded-xl bg-[#0055b3] px-5 py-3 text-sm font-semibold text-white hover:bg-[#0044a0]"
            :class="submitting ? 'cursor-not-allowed opacity-70' : ''"
            :disabled="submitting"
            @click="completeMission"
          >
            <CheckCircle class="h-4 w-4" /> {{ submitting ? '업로드 중...' : '완료하기' }}
          </button>
        </div>
      </div>
    </section>

    <section v-else class="container py-20 text-center">
      <p class="text-[#4a6b8a]">미션을 찾을 수 없어요.</p>
      <RouterLink to="/places" class="mt-4 inline-block text-[#0055b3] hover:underline">장소 목록으로 돌아가기</RouterLink>
    </section>
  </main>
</template>
