import type { RouteRecordRaw } from 'vue-router'

export const roomRoute: RouteRecordRaw[] = [
  {
    path: '',
    name: 'room',
    component: () => import('@/pages/room/index.vue'),
  },
]