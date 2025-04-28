export interface PageInfo {
    totalElements: number
    totalPages: number
    number: number
    size: number
  }
export interface ManagementSupplierResponse {
  content: any[]
  page: PageInfo
}