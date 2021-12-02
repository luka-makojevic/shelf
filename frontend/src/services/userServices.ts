import axios from 'axios';
import { ForgotPasswordData, ResetPasswordData } from '../interfaces/types';

const API_URL = `http://10.10.0.136:8080/`;

const emailConfirmation = (token: string | undefined) =>
  axios.post(`${API_URL}tokens/confirmation`, { token });

const resendEmailVerification = (token: string | undefined) =>
  axios.post(`${API_URL}tokens/resend`, { token });

const forgotPassword = (data: ForgotPasswordData) =>
  axios.post(`${API_URL}users/password-reset-request`, data);

const resetPassword = (data: ResetPasswordData) =>
  axios.post(`${API_URL}users/password-reset`, data);

export default {
  emailConfirmation,
  resendEmailVerification,
  forgotPassword,
  resetPassword,
};
