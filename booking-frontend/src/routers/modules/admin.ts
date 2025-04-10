export const adminRoute = {
  path: '/admin',
  meta: {
    layout: 'AdminLayout', // 👈 bắt buộc để App.vue load đúng layout
  },
  component: () => import('@/layouts/AdminLayout.vue'),
  children: [
    {
      path: 'dashboard',
      name: 'AdminDashboard',
      component: () => import('@/pages/admin/Dashboard.vue'),
    },
  ],
}
