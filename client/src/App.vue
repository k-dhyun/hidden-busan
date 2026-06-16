<script setup lang="ts">
import NavBar from './components/NavBar.vue';
</script>

<template>
  <div class="min-h-screen bg-[#f0f7ff] text-[#0d1b2a]">
    <NavBar />
    <RouterView />
  </div>
</template>
