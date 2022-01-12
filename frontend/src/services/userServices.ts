import axios, { AxiosRequestConfig } from 'axios';
import { API_URL_ACCOUNT } from '../api/api';
import instance from '../api/axiosInstance';
import {
  ForgotPasswordData,
  ResetPasswordData,
  UpdateProfileData,
} from '../interfaces/dataTypes';

const emailConfirmation = (token: string | undefined) =>
  axios.post(`${API_URL_ACCOUNT}tokens/confirmation`, { token });

const resendEmailVerification = (token: string | undefined) =>
  axios.post(`${API_URL_ACCOUNT}tokens/resend`, { token });

const forgotPassword = (data: ForgotPasswordData) =>
  axios.post(`${API_URL_ACCOUNT}users/password-reset-request`, data);

const resetPassword = (data: ResetPasswordData) =>
  axios.post(`${API_URL_ACCOUNT}users/password-reset`, data);

const updateProfile = (data: UpdateProfileData) =>
  instance.put(`${API_URL_ACCOUNT}/update`, data);

const uploadProfilePicture = (files: FormData, options: AxiosRequestConfig) =>
  instance.post(`${API_URL_ACCOUNT}/upload`, files, options);

export default {
  emailConfirmation,
  resendEmailVerification,
  forgotPassword,
  resetPassword,
  updateProfile,
  uploadProfilePicture,
};
