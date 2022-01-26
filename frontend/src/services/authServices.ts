import instance from '../api/axiosInstance';
import {
  RegisterData,
  LoginData,
  MicrosoftRegisterData,
  MicrosoftLoginData,
  LogoutData,
} from '../interfaces/dataTypes';

const API_URL_ACCOUNT = '/account';

const register = (data: RegisterData) =>
  instance.post(`${API_URL_ACCOUNT}/register`, data);

const microsoftRegister = (data: MicrosoftRegisterData) =>
  instance.post(`${API_URL_ACCOUNT}/register/microsoft`, data);

const login = (data: LoginData) =>
  instance.post(`${API_URL_ACCOUNT}/login`, data);

const microsoftLogin = (data: MicrosoftLoginData) =>
  instance.post(`${API_URL_ACCOUNT}/login/microsoft`, data);

const logout = (data: LogoutData) =>
  instance.post(`${API_URL_ACCOUNT}/auth/logout`, data);

export default {
  register,
  microsoftRegister,
  login,
  microsoftLogin,
  logout,
};
