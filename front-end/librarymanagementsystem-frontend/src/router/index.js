import { createRouter, createWebHistory } from 'vue-router'
import UserVue from '@/components/User.vue'
import ItemsVue from '@/components/Items.vue'
import LoginVue from '@/components/Login.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/user'
    },
    {
      path: '/user',
      component: UserVue
    },
    {
      path: '/items',
      component: ItemsVue
    },
    {
      path: '/login',
      component: LoginVue
    }
  ]
})

export default router
