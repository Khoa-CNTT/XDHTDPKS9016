import { createWebHistory, createRouter, type RouteRecordRaw } from 'vue-router'
import { authRoute, dashboardRoute, profileRoute, quizRoute,hotelRoute,contactRoute,aboutRoute,roomRoute } from './modules'
import { adminRoute } from './modules/admin'
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
    // beforeEnter: [authGuard],
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
    // beforeEnter: [authGuard],
    children: aboutRoute,
  },
  {
    path: '/rooms',
    // beforeEnter: [authGuard],
    children: roomRoute,
  },
  {
    path: '/contact',
    // beforeEnter: [authGuard],
    children: contactRoute,
  },
  // {
  //   path: '/hotels',
  //   // beforeEnter: [authGuard],
  //   children: hotelRoute,
  // },
  {
    path: '/hotels',
    children: [
      ...hotelRoute,
      {
        path: ':id', // Route động cho từng khách sạn
        name: 'HotelDetail',
        component: () => import('@/pages/hotel/hotelDetail.vue'),
      },
    ],
  },
  adminRoute

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
