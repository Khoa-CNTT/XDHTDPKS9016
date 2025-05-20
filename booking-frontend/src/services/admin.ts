import { $api } from '@/api/ofetch'
import {
  // PageInfo,
  // ManagementSupplierResponse,
  User,
  Hotel,
  // CreateHotelPayload,
  ApiResponse,
  Residence,
  // Residence,
  InfoHotel,
  HotelDetail
} from '@/types/admin'

export interface BaseResponse<T> {
  content: T
  page: {
    totalElements: number
    totalPages: number
    number: number
    size: number
  }
}
// export const getUsersApi = async (
//   page: number,
//   pageSize: number,
// ): Promise<BaseResponse<User[]>> => {
//   return $api(
//     '/management-user',
//     {
//       method: 'GET',
//       query: { page, pageSize },
//     },
//     true,
//   )
// }
export const getUsersApi = async (
  page: number,
  size: number,
  keyword?: string,
  gender?: string,
): Promise<BaseResponse<User[]>> => {
  return $api(
    '/management-user',
    {
      method: 'GET',
      query: {
        page,
        size,
        keyword: keyword?.trim() || undefined,
        gender: gender || undefined,
      },
    },
    true,
  )
}

export const deleteUserApi = async (userId: number): Promise<void> => {
  return $api(
    `/management-user/${userId}`,
    {
      method: 'DELETE',
    },
    true,
  )
}

export const getUserDetailsApi = async (userId: number): Promise<User> => {
  return $api(
    `/management-user/${userId}`,
    {
      method: 'GET',
    },
    true,
  )
}
// Supplier
export const getManagementSupplier = async (page = 0): Promise<ApiResponse> => {
  return $api(
    `/management-supplier?page=${page}`,
    {
      method: 'GET',
    },
    true,
  )
}
export const getSupplierByIdApi = async (supplierId: number): Promise<Hotel> => {
  return $api(
    `/management-supplier/${supplierId}`,
    {
      method: 'GET',
    },
    true,
  )
}


export const deleteSupplierApi = async (supplierId: number): Promise<void> => {
  return $api(
    `/management-supplier/${supplierId}`,
    {
      method: 'DELETE',
    },
    true,
  )
}



// export const createSupplierApi = async (supplierData: CreateHotelPayload): Promise<Hotel> => {
//   return $api(
//     '/management-supplier',
//     {
//       method: 'POST',
//       body: supplierData,
//     },
//     true,
//   )
// }

export const updateSupplierApi = async (
  supplierId: number,
  supplierData: InfoHotel
): Promise<HotelDetail> => {
  return $api(
    `/management-supplier/${supplierId}`,
    {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(supplierData),
    },
    true,
  )
}


// export const deleteSupplierApi = async (supplierId: number): Promise<void> => {
//   return $api(
//     `/management-supplier/${supplierId}`,
//     {
//       method: 'DELETE',
//     },
//     true,
//   )
// }
