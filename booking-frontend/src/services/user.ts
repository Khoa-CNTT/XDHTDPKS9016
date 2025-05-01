import { $api } from '@/api/ofetch'
import type { BaseResponse } from '@/types/api'
import type { IUser } from '@/types/user'
import { UserProfile, UserInfo, CreateUserRequest, CreateUserResponse } from '@/types/user'

export const getInfoApi = async (): Promise<BaseResponse<UserInfo>> => {
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
