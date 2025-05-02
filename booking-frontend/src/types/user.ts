import { number } from "yup"
export interface User {
  id: number;
  username: string;
  role: string;
}
export interface IUser {
  id: string
  first_name: ''
  last_name: ''
  user_id?: string
  sub_id: string
  name: string
  email: string
  role: string
  property_id: string
  avatar: string
  setting_language_id: string
  working_language_id: string
  is_company: boolean
  confirm_is_company: boolean
  enabled: boolean
}

export interface Member {
  member: IUser
  permission: 'OWNER' | 'MEMBER'
}
export interface UserProfile {
  user_id: number;
  full_name: string;
  gender: string;
  address: string;
  email: string;
  phone: string;
  birth_date: string;
  status: string | number;
  username: string;
  role: string;
}
export interface UserInfo {
  user_id: number;
  full_name: string | null;
  gender: string | null;
  address: string | null;
  email: string;
  phone: string | null;
  birth_date: string | null;
  status: string |  number;
  username: string;
  role:string;
}
export interface CreateUserRequest {
  full_name: string;
  gender: string;
  address: string;
  email: string;
  phone: string;
  birth_date: string; 
  status: string;
}

export interface CreateUserResponse {
  user_id: number;
  full_name: string;
  gender: string;
  address: string;
  email: string;
  phone: string;
  birth_date: string;
  status: string;
  username: string;
}