import axios, { AxiosRequestConfig } from 'axios';
import { toast } from 'react-toastify';
import { API_URL_ACCOUNT } from './api';
import { store } from '../store/store';
import {
  increaseLoadingCounter,
  decreaseLoadingCounter,
} from '../store/loadingReducer';

const instanceConfig: AxiosRequestConfig = {
  baseURL: API_URL_ACCOUNT,
  headers: {
    'Content-Type': 'application/json',
  },
};

const token = localStorage.getItem('token')
  ? JSON.parse(localStorage.getItem('token') || '')
  : '';

if (token) {
  // eslint-disable-next-line no-param-reassign
  if (instanceConfig.headers)
    instanceConfig.headers.Authorization = `Bearer ${token}`;
}

const instance = axios.create(instanceConfig);

instance.interceptors.request.use(
  (config) => {
    store.dispatch(increaseLoadingCounter());
    return config;
  },
  (err) => err
);

instance.interceptors.response.use(
  (res) => {
    if (res?.data?.jwtToken) {
      localStorage.setItem('token', JSON.stringify(res.data.jwtToken));
      localStorage.setItem(
        'refreshToken',
        JSON.stringify(res.data.jwtRefreshToken)
      );
      if (instanceConfig.headers)
        instanceConfig.headers.Authorization = `Bearer ${res.data.jwtToken}`;
    }
    store.dispatch(decreaseLoadingCounter());
    return res;
  },
  async (err) => {
    const originalConfig = err.config;

    if (err.response?.data?.message === 'Token expired.') {
      try {
        const response = await axios.get(
          `${API_URL_ACCOUNT}auth/refresh/token`,
          {
            headers: {
              Authorization: `Bearer ${JSON.parse(
                localStorage.getItem('refreshToken') || ''
              )}`,
            },
          }
        );

        const { token: acccesToken } = response.data;
        localStorage.setItem('token', JSON.stringify(acccesToken));

        return await instance(originalConfig);
      } catch (_error) {
        return Promise.reject(_error);
      }
    }
    store.dispatch(decreaseLoadingCounter());

    if (err.response?.status === 404) {
      toast.error(err.response.data.message);
    } else if (err.response?.status === 500) {
      toast.error('Internal server error');
    } else if (err?.message === 'Network Error') {
      toast.error('Network Error');
    } else {
      return Promise.reject(err);
    }
    return null;
  }
);

export default instance;
