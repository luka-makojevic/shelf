import axios from 'axios';
import {
  RegisterData,
  LoginData,
  ResetPasswordData,
} from '../interfaces/types';

const API_URL = 'http://10.10.0.117:8080/users/';

const register = (data: RegisterData) => axios.post(`${API_URL}register`, data);

const login = (data: LoginData) => axios.post(`${API_URL}login`, data);

const logout = (setUser: (arg: null) => void) => {
  // missing features from backend
  localStorage.removeItem('user');
  setUser(null);
};

const resetPassword = (data: ResetPasswordData) =>
  axios.post(`${API_URL}password-reset-request`, data);

export default {
  register,
  login,
  logout,
  resetPassword,
};
