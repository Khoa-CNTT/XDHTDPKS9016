import type { RouteRecordRaw } from 'vue-router'

export const hotelRoute: RouteRecordRaw[] = [
  {
    path: '',
    name: 'hotel',
    component: () => import('@/pages/hotel/index.vue'),
  },
  {
    path: ':hotelId',
    component: () => import('@/pages/hotel/hotelDetail.vue'), 
    // children: [
    //   {
    //     path: 'booking',
    //     name: 'BookingModal',
    //     component: () => import('@/pages/hotel/BookingModal.vue'),
    //   }
    // ]
  }

]