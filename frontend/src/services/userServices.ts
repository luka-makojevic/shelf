import axios from 'axios';
import { API_URL_ACCOUNT } from '../api/api';
import { ForgotPasswordData, ResetPasswordData } from '../interfaces/dataTypes';

const emailConfirmation = (token: string | undefined) =>
  axios.post(`${API_URL_ACCOUNT}tokens/confirmation`, { token });

const resendEmailVerification = (token: string | undefined) =>
  axios.post(`${API_URL_ACCOUNT}tokens/resend`, { token });

const forgotPassword = (data: ForgotPasswordData) =>
  axios.post(`${API_URL_ACCOUNT}users/password-reset-request`, data);

const resetPassword = (data: ResetPasswordData) =>
  axios.post(`${API_URL_ACCOUNT}users/password-reset`, data);

export default {
  emailConfirmation,
  resendEmailVerification,
  forgotPassword,
  resetPassword,
};
