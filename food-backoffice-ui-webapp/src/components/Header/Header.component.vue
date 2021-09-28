<template>
  <div class="container">
    <header class="d-flex flex-wrap justify-content-center py-3 mb-4 border-bottom">
      <a href="/" class="d-flex align-items-center mb-3 mb-md-0 me-md-auto text-dark text-decoration-none">
        <i style="padding-right: 15px; padding-left: 15px" className="fas fa-desktop fa-2x"></i>
        <span class="fs-4">Food Plataform Backoffice</span>
      </a>
      <ul class="nav nav-pills">
        <li class="nav-item">
          <router-link class="nav-link" active-class="active" to="/">Home</router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" active-class="active" to="/restaurant">Restaurante</router-link>
        </li>
      </ul>
    </header>
  </div>
</template>

<script lang="ts">
  import { defineComponent } from 'vue'

  export default defineComponent({
    name: 'header'
  })
</script>