<script setup lang="ts">
import { Clock, MapPin, RotateCcw } from 'lucide-vue-next';
import { computed, ref } from 'vue';
import BusanDistrictMap from '@/components/BusanDistrictMap.vue';
import { useTravelStore } from '@/stores/travel';
import type { Category } from '@/types/domain';

const travelStore = useTravelStore();

const categories: Array<Category | '전체'> = ['전체', '명소', '맛집', '축제', '쇼핑', '시장'];
const selectedDistrictId = ref<string | null>(null);
const activeCategory = ref<Category | '전체'>('전체');

const selectedDistrict = computed(() =>
  travelStore.districts.find((district) => district.id === selectedDistrictId.value),
);

const districtPlaces = computed(() => {
  if (!selectedDistrict.value) {
    return travelStore.contents;
  }

  return travelStore.contents.filter((content) => content.region === selectedDistrict.value?.region);
});

const filteredPlaces = computed(() => {
  if (activeCategory.value === '전체') {
    return districtPlaces.value;
  }

  return districtPlaces.value.filter((place) => place.category === activeCategory.value);
});

function selectDistrict(id: string) {
  selectedDistrictId.value = selectedDistrictId.value === id ? null : id;
  activeCategory.value = '전체';
}

function clearDistrict() {
  selectedDistrictId.value = null;
  activeCategory.value = '전체';
}
</script>

<template>
  <main class="page-shell">
    <section class="container py-8">
      <div class="mb-5">
        <h1 class="text-3xl font-bold">부산 지도에서 숨은 장소 찾기</h1>
        <p class="mt-2 text-sm text-[#4a6b8a]">구 이름을 클릭하면 해당 지역의 명소·맛집·축제를 확인할 수 있어요.</p>
      </div>

      <div class="overflow-hidden rounded-2xl border border-blue-100 bg-white shadow-sm">
        <BusanDistrictMap
          :districts="travelStore.districts"
          :selected-district-id="selectedDistrictId"
          @select="selectDistrict"
        />
      </div>

      <div class="mt-5 rounded-2xl border border-blue-100 bg-white p-5 shadow-sm">
        <div class="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
          <div>
            <h2 class="text-xl font-bold">
              {{ selectedDistrict ? selectedDistrict.label : '전체 지역' }}
            </h2>
            <p class="mt-1 text-sm text-[#4a6b8a]">
              {{ selectedDistrict ? `${districtPlaces.length}개의 장소가 있습니다.` : `전체 ${districtPlaces.length}개의 장소가 있습니다.` }}
            </p>
          </div>
          <button
            v-if="selectedDistrict"
            class="inline-flex items-center justify-center gap-2 rounded-xl border border-blue-200 px-4 py-2 text-sm text-[#0055b3] hover:bg-[#e8f4fd]"
            @click="clearDistrict"
          >
            <RotateCcw class="h-4 w-4" /> 초기화
          </button>
        </div>

        <div class="mt-4 flex flex-wrap gap-2">
          <button
            v-for="category in categories"
            :key="category"
            class="rounded-full px-3 py-1.5 text-sm"
            :class="activeCategory === category ? 'bg-[#0055b3] text-white' : 'bg-[#f0f7ff] text-[#4a6b8a]'"
            @click="activeCategory = category"
          >
            {{ category }}
          </button>
        </div>

        <div v-if="filteredPlaces.length" class="mt-5 grid gap-4 sm:grid-cols-2">
          <RouterLink
            v-for="place in filteredPlaces"
            :key="place.id"
            :to="`/places/${place.id}`"
            class="flex gap-4 rounded-xl border border-blue-100 p-3 hover:bg-[#f0f7ff]"
          >
            <img :src="place.image" :alt="place.name" class="h-24 w-28 rounded-lg object-cover" />
            <div class="min-w-0">
              <span class="rounded-full bg-[#e8f4fd] px-2 py-1 text-xs text-[#0055b3]">{{ place.category }}</span>
              <h3 class="mt-2 font-bold">{{ place.name }}</h3>
              <p class="mt-1 flex items-center gap-1 text-xs text-[#4a6b8a]">
                <MapPin class="h-3 w-3" /> {{ place.address }}
              </p>
              <p class="mt-1 flex items-center gap-1 text-xs text-[#4a6b8a]">
                <Clock class="h-3 w-3" /> {{ place.duration }}
              </p>
            </div>
          </RouterLink>
        </div>
      </div>
    </section>
  </main>
</template>
