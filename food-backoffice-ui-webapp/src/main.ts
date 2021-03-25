import { createApp } from 'vue'
import { createWebHistory, createRouter } from 'vue-router'

import App from './App.vue'

import Main from './features/Home/screens/Home/Main.component.vue'
import SearchRestaurant from './features/Restaurant/screens/SearchRestaurant/SearchRestaurant.component.vue'

const routes = [
  {
    path: "/",
    name: "home",
    component: Main,
  },
  {
    path: "/restaurant",
    name: "restaurant",
    component: SearchRestaurant,
  },
];
  
const router = createRouter({
  history: createWebHistory(),
  routes,
});

createApp(App).use(router).mount('#app')
