import type { RouteRecordRaw } from 'vue-router'

export const authRoute: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'login',
    component: () => import('@/pages/auth/login.vue'),
  },
  {
    path: '/register',
    children: [
      {
        path: '',
        name: 'register',
        component: () => import('@/pages/auth/register/register.vue'),
      },
      {
        path: 'confirm',
        name: 'register-confirm',
        component: () => import('@/pages/auth/register/Confirm.vue'),
      },
    ],
  },
  {
    path: '/password',
    name: 'password',
    children: [
      {
        path: 'forgot',
        name: 'password-forgot',
        component: () => import('@/pages/auth/password/forgot.vue'),
      },
      {
        path: 'otp',
        name: 'otp',
        component: () => import('@/pages/auth/password/confirm.vue'),
      },
      {
        path: 'reset',
        name: 'password-reset',
        component: () => import('@/pages/auth/password/reset.vue'),
      },
    ],
  },
]
