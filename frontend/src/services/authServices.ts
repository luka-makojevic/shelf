import axios from 'axios';
import {
  RegisterData,
  LoginData,
  MicrosoftRegisterData,
  MicrosoftLoginData,
  LogoutData,
} from '../interfaces/dataTypes';
import instance from '../api/axiosInstance';
import loginInstance from '../api/loginInstance';
import { API_URL_ACCOUNT } from '../api/api';

const register = (data: RegisterData) =>
  axios.post(`${API_URL_ACCOUNT}register`, data);

const microsoftRegister = (data: MicrosoftRegisterData) =>
  axios.post(`${API_URL_ACCOUNT}register/microsoft`, data);

const login = (data: LoginData) =>
  loginInstance.post(`${API_URL_ACCOUNT}login`, data);

const microsoftLogin = (data: MicrosoftLoginData) =>
  loginInstance.post(`${API_URL_ACCOUNT}login/microsoft`, data);

const logout = (data: LogoutData) =>
  instance.post(`${API_URL_ACCOUNT}auth/logout`, data);

export default {
  register,
  microsoftRegister,
  login,
  microsoftLogin,
  logout,
};
