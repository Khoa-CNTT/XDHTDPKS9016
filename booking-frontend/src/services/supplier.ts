// import { $api } from '@/api/ofetch'
// import {ServiceResponse,AddService,Service} from "@/types/supplier"
// //manager vervice

// // export const getServiceListApi = async (): Promise<ServiceResponse> => {
// //   return $api('/api/v1/management-service', { method: 'GET' }, false);
// // };
// export const getServiceListApi = async (): Promise<ServiceResponse> => {
//   const token = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzcWMuY29tIiwic3ViIjoiaG90ZWxhYmMxIiwiZXhwIjoxNzQ1ODY1Mjk5LCJpYXQiOjE3NDU4NjE2OTksImp0aSI6IjQ4ZmE0NjJlLWYzNjYtNDY2NC05MTdkLWE3Mzc0ZjQ5NmZkNSIsInNjb3BlIjoiU1VQUExJRVIifQ.yLhzLXFsY0D-rSUnGT5WkfdjMO0AiqrTCwZndhhBeF5MZBXyxbrHE7F6KL8yJuNlkDDnm2jn-DPHEmTWRa-6IA"; 
//   return $api('/management-service', {
//     method: 'GET',
//     headers: {
//       Authorization: `Bearer ${token}`
//     }
//   }, false);
// };
// export const getServiceByIdApi = async (serviceId: number): Promise<Service> => {
//   const token = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzcWMuY29tIiwic3ViIjoiaG90ZWxhYmMxIiwiZXhwIjoxNzQ1ODY1Mjk5LCJpYXQiOjE3NDU4NjE2OTksImp0aSI6IjQ4ZmE0NjJlLWYzNjYtNDY2NC05MTdkLWE3Mzc0ZjQ5NmZkNSIsInNjb3BlIjoiU1VQUExJRVIifQ.yLhzLXFsY0D-rSUnGT5WkfdjMO0AiqrTCwZndhhBeF5MZBXyxbrHE7F6KL8yJuNlkDDnm2jn-DPHEmTWRa-6IA"; 

//   return $api(`/management-service/${serviceId}`, {
//     method: 'GET',
//     headers: {
//       Authorization: `Bearer ${token}`
//     }
//   }, false);
// };


// // Hàm xóa dịch vụ theo ID
// export const deleteServiceApi = async (serviceId: number): Promise<void> => {
//   const token = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzcWMuY29tIiwic3ViIjoiaG90ZWxhYmMxIiwiZXhwIjoxNzQ1ODY1Mjk5LCJpYXQiOjE3NDU4NjE2OTksImp0aSI6IjQ4ZmE0NjJlLWYzNjYtNDY2NC05MTdkLWE3Mzc0ZjQ5NmZkNSIsInNjb3BlIjoiU1VQUExJRVIifQ.yLhzLXFsY0D-rSUnGT5WkfdjMO0AiqrTCwZndhhBeF5MZBXyxbrHE7F6KL8yJuNlkDDnm2jn-DPHEmTWRa-6IA"; 

//   await $api(`/management-service/${serviceId}`, {
//     method: 'DELETE',
//     headers: {
//       Authorization: `Bearer ${token}`
//     }
//   }, false);
// };

// // Hàm cập nhật dịch vụ
// export const updateServiceApi = async (serviceId: number, serviceData: AddService): Promise<Service> => {
//   const token = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzcWMuY29tIiwic3ViIjoiaG90ZWxhYmMxIiwiZXhwIjoxNzQ1ODY1Mjk5LCJpYXQiOjE3NDU4NjE2OTksImp0aSI6IjQ4ZmE0NjJlLWYzNjYtNDY2NC05MTdkLWE3Mzc0ZjQ5NmZkNSIsInNjb3BlIjoiU1VQUExJRVIifQ.yLhzLXFsY0D-rSUnGT5WkfdjMO0AiqrTCwZndhhBeF5MZBXyxbrHE7F6KL8yJuNlkDDnm2jn-DPHEmTWRa-6IA";

//   return $api(`/management-service/${serviceId}`, {
//     method: 'PUT',
//     headers: {
//       Authorization: `Bearer ${token}`,
//       'Content-Type': 'application/json', 
//     },
//     body: JSON.stringify(serviceData), 
//   }, false);
// };

// // Hàm thêm dịch vụ mới
// export const createServiceApi = async (serviceData: AddService): Promise<Service> => {
//   const token = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzcWMuY29tIiwic3ViIjoiaG90ZWxhYmMxIiwiZXhwIjoxNzQ1ODY1Mjk5LCJpYXQiOjE3NDU4NjE2OTksImp0aSI6IjQ4ZmE0NjJlLWYzNjYtNDY2NC05MTdkLWE3Mzc0ZjQ5NmZkNSIsInNjb3BlIjoiU1VQUExJRVIifQ.yLhzLXFsY0D-rSUnGT5WkfdjMO0AiqrTCwZndhhBeF5MZBXyxbrHE7F6KL8yJuNlkDDnm2jn-DPHEmTWRa-6IA"; 

//   return $api('/management-service', {
//     method: 'POST',
//     headers: {
//       Authorization: `Bearer ${token}`,
//       'Content-Type': 'application/json',
//     },
//     body: JSON.stringify(serviceData),
//   }, false);
// };

import { $api } from '@/api/ofetch'
import { ServiceResponse, AddService, Service } from "@/types/supplier"

// Lấy danh sách dịch vụ
export const getServiceListApi = async (): Promise<ServiceResponse> => {
  return $api('/management-service', { method: 'GET' }, true);
};

// Lấy dịch vụ theo ID
export const getServiceByIdApi = async (serviceId: number): Promise<Service> => {
  return $api(`/management-service/${serviceId}`, { method: 'GET' }, true);
};

// Xóa dịch vụ theo ID
export const deleteServiceApi = async (serviceId: number): Promise<void> => {
  await $api(`/management-service/${serviceId}`, { method: 'DELETE' }, true);
};

// Cập nhật dịch vụ
export const updateServiceApi = async (serviceId: number, serviceData: AddService): Promise<Service> => {
  return $api(`/management-service/${serviceId}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(serviceData),
  }, true);
};

// Thêm dịch vụ mới
export const createServiceApi = async (serviceData: AddService): Promise<Service> => {
  return $api('/management-service', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(serviceData),
  }, true);
};
