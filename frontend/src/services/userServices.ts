import axios from 'axios';
import instance from '../api/axiosInstance';
import { ForgotPasswordData, ResetPasswordData } from '../interfaces/dataTypes';

const API_URL = `http://10.10.0.136:8080/account/`;
const API_URL_ADMIN = `http://10.10.0.136:8081/admin/`;

const emailConfirmation = (token: string | undefined) =>
  axios.post(`${API_URL}tokens/confirmation`, { token });

const resendEmailVerification = (token: string | undefined) =>
  axios.post(`${API_URL}tokens/resend`, { token });

const forgotPassword = (data: ForgotPasswordData) =>
  axios.post(`${API_URL}users/password-reset-request`, data);

const resetPassword = (data: ResetPasswordData) =>
  axios.post(`${API_URL}users/password-reset`, data);

const getUserById = (id: number | string) =>
  instance.get(`${API_URL_ADMIN}${id}`);

export default {
  emailConfirmation,
  resendEmailVerification,
  forgotPassword,
  resetPassword,
  getUserById,
};
