import { NavigateFunction } from 'react-router-dom';
import { Role } from '../enums/roles';

export interface RegisterData {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
}

export interface LoginData {
  email: string;
  password: string;
}
export interface ContextTypes {
  user?: UserType | null;
  login: (
    data: LoginData,
    onSuccess: (nav: NavigateFunction) => void,
    onError: (error: string) => void
  ) => void;
  register: (
    data: RegisterData,
    onSuccess: () => void,
    onError: (error: string) => void
  ) => void;
  isLoading: boolean;
  accessToken: string;
}
export interface UserType {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  jwtToken: string;
  role: Role;
}
export interface RegisterFormData {
  areTermsRead: boolean;
  email: string;
  password: string;
  confirmPassword: string;
  firstName: string;
  lastName: string;
}
export interface FormProps {
  children: React.ReactNode;
}

export interface FormBaseProps {
  children: React.ReactNode;
  onSubmit: React.FormEventHandler<HTMLFormElement>;
}

export interface FormSubmitProps {
  children: React.ReactNode;
  isLoading: boolean;
}

export interface ButtonProps {
  children?: React.ReactNode;
  icon?: JSX.Element;
  to?: string;
  isLoading?: boolean;
  spinner?: boolean;
  onClick?: React.MouseEventHandler<HTMLButtonElement>;
  variant?: string | any;
  size?: string | any;
  fullwidth?: boolean;
}
