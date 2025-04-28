// api/user.ts
import { $api } from '@/api/ofetch'
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
        Authorization: `Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzcWMuY29tIiwic3ViIjoiYWRtaW4iLCJleHAiOjE3NDU4MzI2NzcsImlhdCI6MTc0NTgyOTA3NywianRpIjoiMDE2NTAwM2EtYTViNi00Yzc0LTlmNjItY2RhOGZmMTVjZjY3Iiwic2NvcGUiOiJBRE1JTiJ9.9zOQ0aRHkmFy8qGL29zKeB93gUKP3AXA0g6fZfgovsjpsPVjd8sf4ZcX9ff4dLwX_kTDgHKOg6FvGLVWTqb50Q`,
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
        Authorization: `Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzcWMuY29tIiwic3ViIjoiYWRtaW4iLCJleHAiOjE3NDU4MzI2NzcsImlhdCI6MTc0NTgyOTA3NywianRpIjoiMDE2NTAwM2EtYTViNi00Yzc0LTlmNjItY2RhOGZmMTVjZjY3Iiwic2NvcGUiOiJBRE1JTiJ9.9zOQ0aRHkmFy8qGL29zKeB93gUKP3AXA0g6fZfgovsjpsPVjd8sf4ZcX9ff4dLwX_kTDgHKOg6FvGLVWTqb50Q`,
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
        Authorization: `Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzcWMuY29tIiwic3ViIjoiYWRtaW4iLCJleHAiOjE3NDU4MzI2NzcsImlhdCI6MTc0NTgyOTA3NywianRpIjoiMDE2NTAwM2EtYTViNi00Yzc0LTlmNjItY2RhOGZmMTVjZjY3Iiwic2NvcGUiOiJBRE1JTiJ9.9zOQ0aRHkmFy8qGL29zKeB93gUKP3AXA0g6fZfgovsjpsPVjd8sf4ZcX9ff4dLwX_kTDgHKOg6FvGLVWTqb50Q`,
      },
    },
    false,
  )
}
