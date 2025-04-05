import { createWebHistory, createRouter, type RouteRecordRaw } from 'vue-router'
import { authRoute, dashboardRoute, profileRoute, quizRoute,hotelRoute,contactRoute,aboutRoute,roomRoute } from './modules'
import { authGuard } from './auth-guard'
const { progress } = useIndicator()

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    // beforeEnter: [authGuard],
    component: () => import('@/pages/index.vue'),
    children: dashboardRoute,
  },
  {
    path: '/test',
    component: () => import('@/pages/test.vue'),
  },
  {
    path: '/auth',
    meta: {
      layout: 'GuestLayout',
      public: true,
    },
    beforeEnter: [authGuard],
    children: authRoute,
  },
  {
    path: '/about',
    children: aboutRoute,
  },
  {
    path: '/contact',
    children: contactRoute,
  },

  {
    path: '/hotels',
    children: [
      ...hotelRoute,
      {
        path: ':id', 
        name: 'HotelDetail',
        component: () => import('@/pages/hotel/hotelDetail.vue'),
      },
    ],
  },

]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach(() => {
  progress.value = 0.3
})

router.afterEach(() => {
  setTimeout(() => {
    progress.value = 1
  }, 100)
})

export default router
