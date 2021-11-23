import { NavigateFunction } from 'react-router-dom'

export interface RegisterData {
  email: string
  password: string
  firstName: string
  lastName: string
}

export interface LoginData {
  email: string
  password: string
}
export interface ContextTypes {
  user: UserType | null | undefined | {}
  login: (
    data: LoginData,
    onSuccess: (nav: NavigateFunction) => void,
    onError: (error: string) => void
  ) => void
  register: (
    data: RegisterData,
    onSuccess: () => void,
    onError: (error: string) => void
  ) => void
  loading: boolean
  accessToken: string
}
export interface UserType {
  id: number
  firstName: string
  lastName: string
  email: string
  jwtToken: string
  role: number
}
export interface RegisterFormData {
  terms: boolean;
  email: string;
  password: string;
  confirmPassword: string;
  firstName: string;
  lastName: string;
}
