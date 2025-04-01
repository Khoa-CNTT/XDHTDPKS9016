import type { RouteRecordRaw } from 'vue-router'

export const contactRoute: RouteRecordRaw[] = [
  {
    path: '',
    name: 'contact',
    component: () => import('@/pages/contact/index.vue'),
  },
]