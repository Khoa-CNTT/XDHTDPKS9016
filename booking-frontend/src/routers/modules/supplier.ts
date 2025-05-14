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
      path: 'hotel-info',
      name: 'HotelInformation',
      component: () => import('@/pages/suppliers/HotelProfile.vue'),
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
      component: () => import('@/pages/suppliers/OverviewReport.vue'),
    },
    {
      path: 'profile-settings',
      name: 'SupplierProfileSettings',
      component: () => import('@/pages/suppliers/ProfileSettings.vue'),
    },
    {
      path: 'room-types',
      name: 'SupplierRoomTypes',
      component: () => import('@/pages/suppliers/SupplierManageRoomTypes.vue'),
    },
    {
      path: 'room-manager',
      name: 'SupplierRoom',
      component: () => import('@/pages/suppliers/ManageRoom.vue'),
    },
    {
      path: 'billing',
      name: 'SupplierBilling',
      component: () => import('@/pages/suppliers/SupplierBilling.vue'),
    },
  ],
}
