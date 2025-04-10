export const adminRoute = {
  path: '/admin',
  meta: {
    layout: 'AdminLayout', 
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
