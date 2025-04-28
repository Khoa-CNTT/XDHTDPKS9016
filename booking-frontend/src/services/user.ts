import { $api } from '@/api/ofetch'
import type { BaseResponse } from '@/types/api'
import type { IUser } from '@/types/user'
import { UserProfile,UserInfo } from '@/types/user';

export const getInfoApi = async (): Promise<BaseResponse<UserInfo>> => {
  return $api('/user-profile', { method: 'GET' })
}

export const updateUserApi = async (id: string, value: Partial<UserProfile>): Promise<BaseResponse<UserProfile>> => {
  return $api(`/user-profile/${id}`, {
    method: 'PUT',
    body: value,
  });
};

