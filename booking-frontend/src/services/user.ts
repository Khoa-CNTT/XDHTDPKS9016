import { $api } from '@/api/ofetch'
import type { BaseResponse } from '@/types/api'
import { UserInfo, CreateUserRequest, CreateUserResponse,ChangePasswordPayload } from '@/types/user'

// export const getInfoApi = async (id: number): Promise<BaseResponse<UserInfo>> => {
//   return $api('/user-profile', { method: 'GET' })
// }

export const getInfoApi = async (): Promise<UserInfo> => {
  return $api('/user-profile', { method: 'GET' })
}

//change password user
export const updateUserApi = async (
  value: ChangePasswordPayload
): Promise<void> => {
  await $api(
    '/accounts/change-password',
    {
      method: 'PUT',
      body: value,
    },
    true
  );
};