<template>
  <div class="search-restaurant">
    <p>Listar restaurantes já cadastrados:</p>
    <div class="container">
      <ul class="list-group">
        <li class="list-group-item" v-for="restaurant in restaurants" :key="restaurant.uuid">
          {{ restaurant.name }}
        </li>
      </ul>
    </div>
  </div>
</template>

<script lang="ts">
  import { defineComponent } from 'vue'

  import { fetchRestaurants } from '@/services/Restaurant.services'
  import Restaurant from '@/models/Restaurant.model'

  export default defineComponent({
   name: 'SearchRestaurant', 
   data() {
     return { 
       restaurants: [] as Restaurant[]
     }
   },
   mounted() {
     fetchRestaurants()
       .then(restaurants=> {
         this.restaurants = restaurants
       })
   }
  })
</script>

<style scoped>
  @media (min-width: 1200px) {
    .container{
      max-width: 900px;
    }
  }
</style>