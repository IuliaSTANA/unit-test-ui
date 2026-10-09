<template>
  <footer
    v-if="$slidev.nav.currentPage !== 1 && $slidev.nav.currentLayout !== 'cover'"
    class="absolute bottom-0 right-0 p-4 text-xs opacity-50 z-50 pointer-events-none"
  >
    {{ $slidev.nav.currentPage }} / {{ $slidev.nav.total }}
  </footer>
</template>
