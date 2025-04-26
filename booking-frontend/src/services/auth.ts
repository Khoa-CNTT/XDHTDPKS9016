import { $api } from '@/api/ofetch'
import type { BaseResponse } from '@/types/api'

// export const loginApi = async (
//   username: string,
//   password: string,
// ): Promise<
//   BaseResponse<{
//   token: string
//     // refresh_token: string
//   }>
// > => {
//   return $api(
//     '/auth/login',
//     {
//       method: 'POST',
//       body: {
//         username,
//         password,
//       },
//     },
//     false,
//   )
// }
export const loginApi = async (
  username: string,
  password: string,
): Promise<{
  token: string
}> => {
  return $api(
    '/auth/login',
    {
      method: 'POST',
      body: {
        username,
        password,
      },
    },
    false,
  )
}

interface RegisterBody {
  email: string
  password: string
  username: string
}

export const registerApi = async (data: RegisterBody): Promise<any> => {
  return $api('/auth/register', { body: data, method: 'POST' }, false)
}

export const confirmEmailApi = async (token: string): Promise<any> => {
  return $api('/users/confirm', { query: { token: token } })
}
export const logoutApi = async (data: { token: string }): Promise<any> => {
  return $api('/auth/logout', { body: data, method: 'POST' }, false)
}
// export const forgotPasswordApi = async (email: string): Promise<any> => {
//   return $api('/users/forgot-password', {
//     method: 'POST',
//     body: { email },
//   })
// }
export const forgotPasswordApi = async (email: string): Promise<any> => {
  return $api(`/forgotPassword/verifyMail/${email}`, {
    method: 'POST',
  })
}
export const confirmOtpApi = async (otp: string, email: string): Promise<any> => {
  return $api(`/forgotPassword/verifyOtp/${otp}/${encodeURIComponent(email)}`, {
    method: 'POST',
  })
}
// interface ChangePasswordPayload {
//   old_password: string
//   new_password: string
//   confirm_new_password: string
// }
// export const changePasswordApi = async (payload: ChangePasswordPayload): Promise<any> => {
//   return $api('/users/change-password', {
//     method: 'POST',
//     body: payload,
//   })
// }
interface ChangePasswordPayload {
  password: string
  repeatPassword: string
}

export const changePasswordApi = async (email: string, payload: ChangePasswordPayload): Promise<any> => {
  return $api(`/forgotPassword/changePassword/${encodeURIComponent(email)}`, {
    method: 'POST',
    body: payload,
  })
}
interface ProfileBody {
  first_name: string
  last_name: string
}

export const updateProfileApi = async (payload: ProfileBody): Promise<any> => {
  return $api('/users/info', {
    method: 'POST',
    body: payload,
  })
}
