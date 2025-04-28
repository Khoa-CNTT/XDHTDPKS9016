import { $api } from '@/api/ofetch'
import {ServiceResponse,AddService,Service} from "@/types/supplier"
//manager vervice

// export const getServiceListApi = async (): Promise<ServiceResponse> => {
//   return $api('/api/v1/management-service', { method: 'GET' }, false);
// };
export const getServiceListApi = async (): Promise<ServiceResponse> => {
  const token = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzcWMuY29tIiwic3ViIjoiaG90ZWxhYmMxIiwiZXhwIjoxNzQ1ODU4Njc2LCJpYXQiOjE3NDU4NTUwNzYsImp0aSI6IjI4NTBmOGFkLTUxZDQtNDMyOS05MzFjLWZkMTA2MzYyMWJiZiIsInNjb3BlIjoiU1VQUExJRVIifQ.AORmyWzi3gXmEIjCeNN4-SOZl3oh0ExU0OZXEuOVPeOfGSy69ik4gfIHM5H-L_LUN8KAG-VoNCp-WgCbxsk9zQ"; 
  return $api('/management-service', {
    method: 'GET',
    headers: {
      Authorization: `Bearer ${token}`
    }
  }, false);
};
export const getServiceByIdApi = async (serviceId: number): Promise<Service> => {
  const token = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzcWMuY29tIiwic3ViIjoiaG90ZWxhYmMxIiwiZXhwIjoxNzQ1ODU4Njc2LCJpYXQiOjE3NDU4NTUwNzYsImp0aSI6IjI4NTBmOGFkLTUxZDQtNDMyOS05MzFjLWZkMTA2MzYyMWJiZiIsInNjb3BlIjoiU1VQUExJRVIifQ.AORmyWzi3gXmEIjCeNN4-SOZl3oh0ExU0OZXEuOVPeOfGSy69ik4gfIHM5H-L_LUN8KAG-VoNCp-WgCbxsk9zQ"; 

  return $api(`/management-service/${serviceId}`, {
    method: 'GET',
    headers: {
      Authorization: `Bearer ${token}`
    }
  }, false);
};


// Hàm xóa dịch vụ theo ID
export const deleteServiceApi = async (serviceId: number): Promise<void> => {
  const token = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzcWMuY29tIiwic3ViIjoiaG90ZWxhYmMxIiwiZXhwIjoxNzQ1ODU4Njc2LCJpYXQiOjE3NDU4NTUwNzYsImp0aSI6IjI4NTBmOGFkLTUxZDQtNDMyOS05MzFjLWZkMTA2MzYyMWJiZiIsInNjb3BlIjoiU1VQUExJRVIifQ.AORmyWzi3gXmEIjCeNN4-SOZl3oh0ExU0OZXEuOVPeOfGSy69ik4gfIHM5H-L_LUN8KAG-VoNCp-WgCbxsk9zQ"; 

  await $api(`/management-service/${serviceId}`, {
    method: 'DELETE',
    headers: {
      Authorization: `Bearer ${token}`
    }
  }, false);
};

// Hàm cập nhật dịch vụ
export const updateServiceApi = async (serviceId: number, serviceData: AddService): Promise<Service> => {
  const token = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzcWMuY29tIiwic3ViIjoiaG90ZWxhYmMxIiwiZXhwIjoxNzQ1ODU4Njc2LCJpYXQiOjE3NDU4NTUwNzYsImp0aSI6IjI4NTBmOGFkLTUxZDQtNDMyOS05MzFjLWZkMTA2MzYyMWJiZiIsInNjb3BlIjoiU1VQUExJRVIifQ.AORmyWzi3gXmEIjCeNN4-SOZl3oh0ExU0OZXEuOVPeOfGSy69ik4gfIHM5H-L_LUN8KAG-VoNCp-WgCbxsk9zQ";

  return $api(`/management-service/${serviceId}`, {
    method: 'PUT',
    headers: {
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json', 
    },
    body: JSON.stringify(serviceData), 
  }, false);
};

// Hàm thêm dịch vụ mới
export const createServiceApi = async (serviceData: AddService): Promise<Service> => {
  const token = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzcWMuY29tIiwic3ViIjoiaG90ZWxhYmMxIiwiZXhwIjoxNzQ1ODU4Njc2LCJpYXQiOjE3NDU4NTUwNzYsImp0aSI6IjI4NTBmOGFkLTUxZDQtNDMyOS05MzFjLWZkMTA2MzYyMWJiZiIsInNjb3BlIjoiU1VQUExJRVIifQ.AORmyWzi3gXmEIjCeNN4-SOZl3oh0ExU0OZXEuOVPeOfGSy69ik4gfIHM5H-L_LUN8KAG-VoNCp-WgCbxsk9zQ"; 

  return $api('/management-service', {
    method: 'POST',
    headers: {
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(serviceData),
  }, false);
};