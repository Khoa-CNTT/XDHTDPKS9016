import { $api } from '@/api/ofetch'
import type { BaseResponse } from '@/types/api'
import { UserInfo, CreateUserRequest, CreateUserResponse } from '@/types/user'

// export const getInfoApi = async (id: number): Promise<BaseResponse<UserInfo>> => {
//   return $api('/user-profile', { method: 'GET' })
// }

export const getInfoApi = async (): Promise<UserInfo> => {
  return $api('/user-profile', { method: 'GET' })
}

export const updateUserApi = async (
  id: number,
  value: Partial<CreateUserRequest>,
): Promise<BaseResponse<CreateUserResponse>> => {
  return $api(`/user-profile/${id}`, {
    method: 'PUT',
    body: value,
  })
}
