import axios from 'axios';
import {
  RegisterData,
  LoginData,
  MicrosoftRegisterData,
  MicrosoftLoginData,
  ResetPasswordData,
} from '../interfaces/types';

const API_URL = 'http://10.10.0.117:8080/';

const register = (data: RegisterData) => axios.post(`${API_URL}register`, data);

const microsoftRegister = (data: MicrosoftRegisterData) =>
  axios.post(`${API_URL}register/microsoft`, data);

const login = (data: LoginData) => axios.post(`${API_URL}login`, data);

const microsoftLogin = (data: MicrosoftLoginData) =>
  axios.post(`${API_URL}login/microsoft`, data);

const logout = (setUser: (arg: null) => void) => {
  // missing features from backend
  localStorage.removeItem('user');
  setUser(null);
};

const resetPass = (data: ResetPasswordData) =>
  axios.post(`${API_URL}password-reset-request`, data);

export default {
  register,
  microsoftRegister,
  login,
  microsoftLogin,
  logout,
  resetPass,
};
