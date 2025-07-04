import { createWebHistory, createRouter, type RouteRecordRaw } from 'vue-router'
import {
  authRoute,
  dashboardRoute,
  profileRoute,
  quizRoute,
  hotelRoute,
  contactRoute,
  aboutRoute,
} from './modules'
import { adminRoute } from './modules/admin'
import { supplierRoute } from './modules/supplier'
import {} from './modules/admin'
import { authGuard } from './auth-guard'
import { useIndicator } from '@/composables/useIndicator'
const { progress } = useIndicator()

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    beforeEnter: [authGuard],
    component: () => import('@/pages/index.vue'),
    children: dashboardRoute,
  },
  {
    path: '/profiles',
    beforeEnter: [authGuard],
    children: profileRoute,
  },
  {
    path: '/payment-history',
    component: () => import('@/pages/historyPayment/index.vue'),
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
    name: 'Hotel',
    children: [
      ...hotelRoute,
      {
        path: ':id',
        name: 'HotelDetail',
        component: () => import('@/pages/hotel/hotelDetail.vue'),
        children: [
          {
            path: 'review',
            name: 'HotelReview',
            component: () => import('@/pages/hotel/hotelReview.vue'),
          },
        ],
      },
    ],
  },
  adminRoute,
  supplierRoute,
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach(() => {
  progress.value = 0.3
})

// router.afterEach(() => {
//   setTimeout(() => {
//     progress.value = 1
//   }, 100)
// })
router.afterEach((to) => {
  setTimeout(() => {
    progress.value = 1
  }, 100)

  // Cập nhật document.title khi chuyển route
  const baseTitle = 'EliteBooking'
  const pageTitle = to.meta.title as string
  document.title = pageTitle || baseTitle
})

export default router
