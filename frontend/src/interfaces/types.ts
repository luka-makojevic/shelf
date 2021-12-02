import { ChangeEventHandler } from 'react';
import { FieldError, RegisterOptions } from 'react-hook-form';
import { Role } from '../enums/roles';

export interface RegisterData {
  areTermsRead: boolean;
  email: string;
  password: string;
  confirmPassword: string;
  firstName: string;
  lastName: string;
}

export interface MicrosoftRegisterData {
  bearerToken: string;
}

export interface LoginData {
  email: string;
  password: string;
}

export interface MicrosoftLoginData {
  bearerToken: string;
}

export interface ForgotPasswordData {
  email: string;
}

export interface PasswordData {
  password: string;
}

export interface ResetPasswordData {
  password: string;
  jwtToken: string | undefined;
}

export interface ForgotPasswordConfig {
  validations: RegisterOptions;
}

export interface ResetPasswordFieldConfig {
  type: InputFieldType;
  placeholder: string;
  name: 'password' | 'confirmPassword';
  validations: RegisterOptions;
}

export interface ContextTypes {
  user?: UserType | null;
  login: (
    data: LoginData,
    onSuccess: () => void,
    onError: (error: string) => void
  ) => void;
  microsoftLogin: (
    data: MicrosoftLoginData,
    onSuccess: () => void,
    onError: (error: string) => void
  ) => void;
  register: (
    data: RegisterData,
    onSuccess: () => void,
    onError: (error: string) => void
  ) => void;
  microsoftRegister: (
    data: MicrosoftRegisterData,
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
  variant?: string;
  size?: string;
  fullwidth?: boolean;
}
export type InputFieldType = 'text' | 'password' | 'email';

export type InputFieldProps = {
  name?: string;
  placeholder?: string;
  onChange?: ChangeEventHandler;
  error?: FieldError;
  type?: InputFieldType;
  value?: string;
};
export interface LoginFieldConfig {
  type: InputFieldType;
  placeholder: string;
  name: 'email' | 'password';

  validations: RegisterOptions;
}

export interface RegisterFieldConfig {
  type: InputFieldType;
  placeholder: string;
  name:
    | 'areTermsRead'
    | 'email'
    | 'password'
    | 'confirmPassword'
    | 'firstName'
    | 'lastName';
  validations: RegisterOptions;
}
