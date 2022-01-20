import { RegisterOptions } from 'react-hook-form';
import { Role } from '../utils/enums/roles';
import { SortingDirection } from '../utils/enums/table';

export interface RegisterData {
  areTermsRead?: boolean;
  email: string;
  password: string;
  confirmPassword?: string;
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

export interface UpdateProfileData {
  password: string | null;
  firstName: string | null;
  lastName: string | null;
}

export interface ResetPasswordFieldConfig {
  type: InputFieldType;
  placeholder: string;
  name: 'password' | 'confirmPassword';
  validations: RegisterOptions;
}

export interface ContextTypes {
  user: UserType | null;
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
  logout: (
    data: LogoutData,
    onSuccess: () => void,
    onError: (error: string) => void
  ) => void;
}

export interface UserType {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  jwtToken: string;
  jwtRefreshToken: string;
  role: {
    id: Role;
    name: string;
  };
}

export interface RegisterFormData {
  areTermsRead: boolean;
  email: string;
  password: string;
  confirmPassword: string;
  firstName: string;
  lastName: string;
}

export interface ManageProfileFormData {
  email: string;
  password: string;
  confirmPassword: string;
  firstName: string;
  lastName: string;
}

export interface LogoutData {
  jwtRefreshToken: string | undefined;
  jwtToken: string;
}
export type InputFieldType = 'text' | 'password' | 'email';

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

export interface ManageProfileFieldConfig {
  type: InputFieldType;
  placeholder: string;
  name: 'email' | 'password' | 'confirmPassword' | 'firstName' | 'lastName';
  validations: RegisterOptions;
}

export interface ShelfFormData {
  name: string;
}

export interface ShelfDataType {
  id: number;
  name: string;
  createdAt: string;
  isDeleted: boolean;
  userId: number;
}

export interface RoleDataType {
  id: number;
  name: string;
}

export interface UserDataType {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  pictureName: string | null;
  role: RoleDataType;
}

export interface HeaderTypes {
  header: string;
  key: string;
}

export interface ShelfTableDataTypes {
  [key: string]: string | number;
  name: string;
  createdAt: string;
  id: number;
}
export interface FileTableDataTypes {
  [key: string]: string | number;
  name: string;
  createdAt: string;
  id: number;
  folder: number;
}
export interface FunctionTableDataTypes {
  [key: string]: string | number;
  name: string;

  id: number;
}

export interface UserTableDataType {
  [key: string]: string | number;
  id: number;
  name: string;
  email: string;
  role: string;
}

export type TableDataTypes =
  | UserTableDataType
  | FunctionTableDataTypes
  | FileTableDataTypes
  | ShelfTableDataTypes;

export interface SortingDirectionTypes {
  [name: string]: SortingDirection;
}

export interface SorterDataTypes {
  [key: string]: string | number;
}

export interface FileDataType {
  id: number;
  name: string;
  path: string;
  createdAt: string;
  folder: boolean;
  shelfId: number;
  parentFolderId: number;
  userId: number;
  size: number;
  isDeleted: boolean;
}

export interface PathHistoryData {
  folderName: string;
  folderId: number;
}

export interface FunctionFromScratchData {
  [key: string]: string;
  name: string;
  language: string;
  shelfId: string;
  trigger: string;
}
export interface BackupFunctionData {
  [key: string]: string | number;
  name: string;
  function: string;
  shelfId: string;
  functionParam: string;
  eventId: number;
}

export interface LogFunctionData {
  [key: string]: string;
  name: string;
  function: string;
  shelfId: string;
  trigger: string;
}

export type FunctionFormData =
  | FunctionFromScratchData
  | BackupFunctionData
  | LogFunctionData;
