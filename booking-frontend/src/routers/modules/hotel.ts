import type { RouteRecordRaw } from 'vue-router'

export const hotelRoute: RouteRecordRaw[] = [
  {
    path: '',
    name: 'hotel',
    component: () => import('@/pages/hotel/index.vue'),
  },
  {
    path: ':hotelId',
    name: 'HotelDetail',
    component: () => import('@/pages/hotel/hotelDetail.vue'), 
  },
]