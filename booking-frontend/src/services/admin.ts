// api/user.ts

import { $api } from "@/api/ofetch"
import {PageInfo, ManagementSupplierResponse} from "@/types/admin"
export interface User {
  user_id: number
  full_name: string | null
  gender: string | null
  address: string | null
  email: string
  phone: string | null
  birth_date: string | null
  status: string | null
  bookings: any[]
  comments: any[]
  ratings: any[]
}

export interface BaseResponse<T> {
  content: T
  page: {
    totalElements: number
    totalPages: number
    number: number
    size: number
  }
}

export const getUsersApi = async (
  page: number,
  pageSize: number,
): Promise<BaseResponse<User[]>> => {
  return $api(
    '/management-user',
    {
      method: 'GET',
      query: { page, pageSize },
      headers: {
        Authorization: `Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzcWMuY29tIiwic3ViIjoiYWRtaW4iLCJleHAiOjE3NDU4NjU2NDIsImlhdCI6MTc0NTg2MjA0MiwianRpIjoiNTYxOWVjOTgtODg5YS00NmY2LWJmZmItZjM5MDEyZjVjN2E5Iiwic2NvcGUiOiJBRE1JTiJ9.jVKwrFiZ9iM3Rjf418u3mwalRpR_XLWzViKVMWQEE5lvF_NcCa18UR9hlC4zbT9-7ASz64iqCNmLA9vklxbHHA`,
      },
    },
    false,
  )
}

export const deleteUserApi = async (userId: number): Promise<void> => {
  return $api(
    `/management-user/${userId}`,
    {
      method: 'DELETE',
      headers: {
        Authorization: `Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzcWMuY29tIiwic3ViIjoiYWRtaW4iLCJleHAiOjE3NDU4NjU2NDIsImlhdCI6MTc0NTg2MjA0MiwianRpIjoiNTYxOWVjOTgtODg5YS00NmY2LWJmZmItZjM5MDEyZjVjN2E5Iiwic2NvcGUiOiJBRE1JTiJ9.jVKwrFiZ9iM3Rjf418u3mwalRpR_XLWzViKVMWQEE5lvF_NcCa18UR9hlC4zbT9-7ASz64iqCNmLA9vklxbHHA`,
      },
    },
    false,
  )
}

export const getUserDetailsApi = async (userId: number): Promise<User> => {
  return $api(
    `/management-user/${userId}`,
    {
      method: 'GET',
      headers: {
        Authorization: `eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzcWMuY29tIiwic3ViIjoiYWRtaW4iLCJleHAiOjE3NDU4NjU2NDIsImlhdCI6MTc0NTg2MjA0MiwianRpIjoiNTYxOWVjOTgtODg5YS00NmY2LWJmZmItZjM5MDEyZjVjN2E5Iiwic2NvcGUiOiJBRE1JTiJ9.jVKwrFiZ9iM3Rjf418u3mwalRpR_XLWzViKVMWQEE5lvF_NcCa18UR9hlC4zbT9-7ASz64iqCNmLA9vklxbHHA`,
      },
    },
    false,
  )
}


export const getManagementSupplier = async (): Promise<ManagementSupplierResponse> => {
  return $api('/management-supplier', {
    method: 'GET',
  })
}

