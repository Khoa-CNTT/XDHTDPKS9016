export const adminRoute = {
  path: '/admin',
  meta: {
    layout: 'AdminLayout', 
  },
  component: () => import('@/layouts/AdminLayout.vue'),
  children: [
    {
      path: 'manage-providers',
      name: 'ManageProviders',
      component: () => import('@/pages/admin/ManageProviders.vue'),
    },
    {
      path: 'manage-user',
      name: 'ManagUsers',
      component: () => import('@/pages/admin/ManageUsers.vue'),
    },
    {
      path: 'statistics',
      name: 'Statistics',
      component: () => import('@/pages/admin/Statistics.vue'),
    },
  ],
}
