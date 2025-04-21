// export const config = {
//   baseURL: import.meta.env.VITE_API_ENDPOINT,
//   path: {
//     refreshToken: 'auth/refresh-tokens',
//   },
//   key: {
//     refreshToken: 'refresh_token', //KEY SAVED IN LOCALSTORAGE
//     accessToken: 'access_token',
//   },
//   retryStatusCodes: [401, 403],
// }
export const config = {
  baseURL: import.meta.env.VITE_API_ENDPOINT,
  path: {
    // Nếu API không có refresh thì có thể bỏ hẳn dòng này, hoặc để trống cũng được
    refreshToken: '', 
  },
  key: {
    token: 'token', // key dùng để lưu vào localStorage
  },
  retryStatusCodes: [401, 403], // vẫn giữ nguyên nếu muốn retry khi token hết hạn
}
