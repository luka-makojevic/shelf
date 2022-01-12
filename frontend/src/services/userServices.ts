import { AxiosRequestConfig } from 'axios';
import instance from '../api/axiosInstance';
import {
  ForgotPasswordData,
  ResetPasswordData,
  UpdateProfileData,
} from '../interfaces/dataTypes';

const API_URL_ACCOUNT = '/account';

const emailConfirmation = (token: string | undefined) =>
  instance.post(`${API_URL_ACCOUNT}/tokens/confirmation`, { token });

const resendEmailVerification = (token: string | undefined) =>
  instance.post(`${API_URL_ACCOUNT}/tokens/resend`, { token });

const forgotPassword = (data: ForgotPasswordData) =>
  instance.post(`${API_URL_ACCOUNT}/users/password-reset-request`, data);

const resetPassword = (data: ResetPasswordData) =>
  instance.post(`${API_URL_ACCOUNT}/users/password-reset`, data);

const getUser = () => instance.get(`${API_URL_ACCOUNT}/users/me`);

const updateProfile = (data: UpdateProfileData, id: number) =>
  instance.put(`${API_URL_ACCOUNT}/users/${id}`, data);

const uploadProfilePicture = (data: FormData, options: AxiosRequestConfig) =>
  instance.put(`${API_URL_ACCOUNT}/users/update/photo`, data, options);

export default {
  emailConfirmation,
  resendEmailVerification,
  forgotPassword,
  resetPassword,
  updateProfile,
  uploadProfilePicture,
  getUser,
};
