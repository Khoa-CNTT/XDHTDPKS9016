import { $api } from '@/api/ofetch'
import type { BaseResponse } from '@/types/api'
import type { IUser } from '@/types/user'
// interface UserProfile {
//   first_name: string
//   last_name: string
// }
export interface UserProfile {
  user_id: number;
  full_name: string | null;
  gender: string | null;
  address: string | null;
  email: string;
  phone: string | null;
  birth_date: string | null;
  status: string | null;
  username: string;
}
// export const getInfoApi = async (): Promise<BaseResponse<IUser>> => {
//   return $api('/user-profile', {
//     method: 'GET',
//   })
// }
export const getInfoApi = async (): Promise<BaseResponse<UserProfile>> => {
  return $api('/user-profile', { method: 'GET' })
}

export const getUserApi = async (id: string): Promise<BaseResponse<IUser[]>> => {
  return $api(`/users/profiles`, {
    method: 'GET',
    query: {
      ids: id,
    },
  })
}

export const createProfileApi = async (payload: UserProfile): Promise<BaseResponse<IUser>> => {
  return $api('/users/info', {
    method: 'POST',
    body: payload,
  })
}
