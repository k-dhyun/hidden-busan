<script setup lang="ts">
import { computed } from 'vue';
import originalMapSvg from '@/assets/busan-administrative-divisions-ko.svg?raw';
import type { District } from '@/types/domain';

const props = defineProps<{
  districts: District[];
  selectedDistrictId: string | null;
}>();

const emit = defineEmits<{
  select: [id: string];
}>();

const districtShapeIndexes: Record<string, number[]> = {
  gangseo: [15, 17, 18, 19, 20, 21, 22, 23, 24],
  sasang: [40],
  buk: [69],
  geumjeong: [70],
  gijang: [44],
  dongrae: [2],
  yeonje: [3],
  busanjin: [42],
  haeundae: [1],
  suyeong: [43],
  nam: [46],
  dong: [47],
  saha: [56, 59],
  seo: [62],
  jung: [65],
  yeongdo: [67],
};

const primaryShapeIndexes: Record<string, number> = {
  gangseo: 17,
  sasang: 40,
  buk: 69,
  geumjeong: 70,
  gijang: 44,
  dongrae: 2,
  yeonje: 3,
  busanjin: 42,
  haeundae: 1,
  suyeong: 43,
  nam: 46,
  dong: 47,
  saha: 59,
  seo: 62,
  jung: 65,
  yeongdo: 67,
};

const shapeIndexToDistrict = new Map<number, string>();

for (const [districtId, indexes] of Object.entries(districtShapeIndexes)) {
  for (const index of indexes) {
    shapeIndexToDistrict.set(index, districtId);
  }
}

const districtLabels = computed(() =>
  Object.fromEntries(props.districts.map((district) => [district.id, district.label])),
);

const interactiveMapMarkup = computed(() => {
  let shapeIndex = -1;
  const cleanSvg = originalMapSvg
    .replace(/<\?xml[^>]*\?>\s*/i, '')
    .replace(/<!DOCTYPE[\s\S]*?>\s*/i, '');

  return cleanSvg
    .replace(
      /<svg\b([^>]*)>/i,
      '<svg$1 class="busan-map-svg" preserveAspectRatio="xMidYMid meet" aria-label="부산 행정구역 지도">',
    )
    .replace(/<(path|polygon)\b([^>]*)>/g, (full, tag: string, attrs: string) => {
      shapeIndex += 1;
      const districtId = shapeIndexToDistrict.get(shapeIndex);

      if (!districtId) {
        return full;
      }

      const isPrimaryShape = primaryShapeIndexes[districtId] === shapeIndex;
      const isSelected = props.selectedDistrictId === districtId;
      const label = districtLabels.value[districtId] ?? districtId;
      const interactiveAttributes = [
        `data-district-id="${districtId}"`,
        `data-selected="${isSelected ? 'true' : 'false'}"`,
        'data-hovered="false"',
        'class="district-shape"',
        isPrimaryShape ? 'role="button"' : 'aria-hidden="true"',
        isPrimaryShape ? 'tabindex="0"' : '',
        isPrimaryShape ? `aria-label="${label} 선택"` : '',
        isPrimaryShape ? `aria-pressed="${isSelected ? 'true' : 'false'}"` : '',
      ]
        .filter(Boolean)
        .join(' ');

      return `<${tag} ${interactiveAttributes}${attrs}>`;
    });
});

function getDistrictIdFromTarget(target: EventTarget | null) {
  if (!(target instanceof Element)) {
    return null;
  }

  return target.closest('[data-district-id]')?.getAttribute('data-district-id') ?? null;
}

function setHoveredDistrict(root: Element, districtId: string | null) {
  root.querySelectorAll('.district-shape[data-hovered="true"]').forEach((shape) => {
    shape.setAttribute('data-hovered', 'false');
  });

  if (!districtId) {
    return;
  }

  root.querySelectorAll(`.district-shape[data-district-id="${districtId}"]`).forEach((shape) => {
    shape.setAttribute('data-hovered', 'true');
  });
}

function previewFromEvent(event: Event) {
  const root = event.currentTarget as Element | null;
  const districtId = getDistrictIdFromTarget(event.target);

  if (root && districtId) {
    setHoveredDistrict(root, districtId);
  }
}

function clearPreviewFromEvent(event: PointerEvent | MouseEvent | FocusEvent) {
  const root = event.currentTarget as Element | null;
  const nextDistrictId = getDistrictIdFromTarget(event.relatedTarget);

  if (!root || nextDistrictId) {
    return;
  }

  setHoveredDistrict(root, null);
}

function selectFromEvent(event: Event) {
  const districtId = getDistrictIdFromTarget(event.target);

  if (districtId) {
    emit('select', districtId);
  }
}

function handleKeyboardSelect(event: KeyboardEvent) {
  if (event.key !== 'Enter' && event.key !== ' ') {
    return;
  }

  selectFromEvent(event);
}
</script>

<template>
  <div
    class="busan-map-shell"
    v-html="interactiveMapMarkup"
    @click="selectFromEvent"
    @pointerover="previewFromEvent"
    @pointerout="clearPreviewFromEvent"
    @mouseover="previewFromEvent"
    @mouseout="clearPreviewFromEvent"
    @focusin="previewFromEvent"
    @focusout="clearPreviewFromEvent"
    @keydown.enter="handleKeyboardSelect"
    @keydown.space.prevent="handleKeyboardSelect"
  />
</template>

<style scoped>
.busan-map-shell {
  width: 100%;
}

.busan-map-shell :deep(.busan-map-svg) {
  display: block;
  width: 100%;
  height: auto;
}

.busan-map-shell :deep(#레이어_3) {
  pointer-events: none;
}

.busan-map-shell :deep(.district-shape) {
  cursor: pointer;
  outline: none;
  transition:
    fill 140ms ease,
    stroke 140ms ease,
    stroke-width 140ms ease;
}

.busan-map-shell :deep(.district-shape[data-hovered='true']) {
  fill: #d8ecff !important;
  stroke: #2f80ed !important;
  stroke-width: 1.5 !important;
}

.busan-map-shell :deep(.district-shape[data-selected='true']) {
  fill: #78b7ff !important;
  stroke: #0055b3 !important;
  stroke-width: 1.8 !important;
}
</style>
