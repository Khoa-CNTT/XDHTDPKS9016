export const supplierRoute = {
  path: '/supplier',
  meta: {
    layout: 'SupplierLayout',
  },
  component: () => import('@/layouts/SupplierLayout.vue'),
  children: [
    {
      path: 'order-room-management',
      name: 'SupplierRoomManagement',
      component: () => import('@/pages/suppliers/RoomManagement.vue'),
    },
    {
      path: 'room-management',
      name: 'RoomManagement',
      component: () => import('@/pages/suppliers/RoomOrder.vue'),
    },
    {
      path: 'service-control',
      name: 'SupplierServiceControl',
      component: () => import('@/pages/suppliers/ServiceControl.vue'),
    },
    {
      path: 'review-manage',
      name: 'SupplierReviewManage',
      component: () => import('@/pages/suppliers/ReviewManage.vue'),
    },
    {
      path: 'overview-report',
      name: 'SupplierOverviewReport',
      component: () => import('@/pages/suppliers/OverviewReport.vue.vue'),
    },
    {
      path: 'profile-settings',
      name: 'SupplierProfileSettings',
      component: () => import('@/pages/suppliers/ProfileSettings.vue'),
    },
  ],
}
