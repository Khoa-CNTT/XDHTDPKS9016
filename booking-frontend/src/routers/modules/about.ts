import type { RouteRecordRaw } from 'vue-router'

export const aboutRoute: RouteRecordRaw[] = [
  {
    path: '',
    name: 'about',
    component: () => import('@/pages/about/index.vue'),
  },
]