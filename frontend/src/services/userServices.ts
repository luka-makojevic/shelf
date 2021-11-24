import axios from 'axios';
// import { ResetPasswordData, ForgotPasswordData } from '../interfaces/types';

const API_URL = `http://10.10.0.117:8080/tokens`;

const emailConfirmation = (token: string | undefined) =>
  axios.post(`${API_URL}/confirmation`, { token });

const resendEmailVerification = (token: string | undefined) =>
  axios.post(`${API_URL}/resend`, { token });

// const forgotPassword = (data: ForgotPasswordData) =>
//   axios.post(`${API_URL}users/password-reset-request`, data);

// const resetPassword = (data: ResetPasswordData, token: string | undefined) =>
//   axios.post(`${API_URL}users/password-reset-response`, { data, token });

export default {
  emailConfirmation,
  resendEmailVerification,
  // forgotPassword,
  // resetPassword,
};
