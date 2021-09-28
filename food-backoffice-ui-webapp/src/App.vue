<template>
  <Header />
  <router-view />
</template>

<script lang="ts">
  import { defineComponent } from 'vue'

  import Header from './components/Header/Header.component.vue'

  export default defineComponent({
    name: 'App',
    components: {
      Header
    }
  })
</script>

<style>
  #app {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
  }
</style>
