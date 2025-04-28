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
        Authorization: `Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzcWMuY29tIiwic3ViIjoiYWRtaW4iLCJleHAiOjE3NDU0ODQ4MzYsImlhdCI6MTc0NTQ4MTIzNiwianRpIjoiMGFjYzM3NDUtNzg0OS00NGYwLWI2MmMtYWRlMWFhYzZhY2Y4Iiwic2NvcGUiOiJBRE1JTiJ9.btYMp7RqNK--hBRLKSW_GJNBhUd-793-D-OCey2Y16ptqSlx6SlfpNtxmIiU7q9RM3z2M3VV92ivu1GHsyykMg`,
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
        Authorization: `Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzcWMuY29tIiwic3ViIjoiYWRtaW4iLCJleHAiOjE3NDU0ODQ4MzYsImlhdCI6MTc0NTQ4MTIzNiwianRpIjoiMGFjYzM3NDUtNzg0OS00NGYwLWI2MmMtYWRlMWFhYzZhY2Y4Iiwic2NvcGUiOiJBRE1JTiJ9.btYMp7RqNK--hBRLKSW_GJNBhUd-793-D-OCey2Y16ptqSlx6SlfpNtxmIiU7q9RM3z2M3VV92ivu1GHsyykMg`,
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
        Authorization: `Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzcWMuY29tIiwic3ViIjoiYWRtaW4iLCJleHAiOjE3NDU0ODQ4MzYsImlhdCI6MTc0NTQ4MTIzNiwianRpIjoiMGFjYzM3NDUtNzg0OS00NGYwLWI2MmMtYWRlMWFhYzZhY2Y4Iiwic2NvcGUiOiJBRE1JTiJ9.btYMp7RqNK--hBRLKSW_GJNBhUd-793-D-OCey2Y16ptqSlx6SlfpNtxmIiU7q9RM3z2M3VV92ivu1GHsyykMg`,
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

