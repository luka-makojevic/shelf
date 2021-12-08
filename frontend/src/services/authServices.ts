import axios from 'axios';
import {
  RegisterData,
  LoginData,
  MicrosoftRegisterData,
  MicrosoftLoginData,
  LogoutData,
} from '../utils/interfaces/dataTypes';
import instance from '../api/axiosInstance';

const API_URL = 'http://10.10.0.136:8080/';

const register = (data: RegisterData) => axios.post(`${API_URL}register`, data);

const microsoftRegister = (data: MicrosoftRegisterData) =>
  axios.post(`${API_URL}register/microsoft`, data);

const login = (data: LoginData) => axios.post(`${API_URL}login`, data);

const microsoftLogin = (data: MicrosoftLoginData) =>
  axios.post(`${API_URL}login/microsoft`, data);

const logout = (data: LogoutData) =>
  instance.post(`${API_URL}auth/logout`, data);

export default {
  register,
  microsoftRegister,
  login,
  microsoftLogin,
  logout,
};
