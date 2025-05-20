// export type BaseResponse<T> = {
//   token: BaseResponse<{ token: string }>
//   data: T
//   status: string
// }
export type BaseResponse<T> = {
  data: T
  status: string
}