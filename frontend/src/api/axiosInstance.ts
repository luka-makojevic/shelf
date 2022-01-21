import axios, { AxiosRequestConfig } from 'axios';
import { toast } from 'react-toastify';
import { store } from '../store/store';
import { setIsLoading } from '../store/loadingReducer';
import { removeUser } from '../store/userReducer';

const instanceConfig: AxiosRequestConfig = {
  baseURL: process.env.REACT_APP_API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
};
let loadingCounter = 0;

const token = JSON.parse(localStorage.getItem('token') || 'null');

if (token) {
  if (instanceConfig.headers)
    instanceConfig.headers.Authorization = `Bearer ${token}`;
}

const instance = axios.create(instanceConfig);

instance.interceptors.request.use(
  (config) => {
    loadingCounter++;
    if (loadingCounter === 1) store.dispatch(setIsLoading(true));
    return config;
  },
  (err) => err
);

instance.interceptors.response.use(
  (res) => {
    loadingCounter--;
    if (loadingCounter === 0) store.dispatch(setIsLoading(false));
    return res;
  },
  (err) => {
    const originalConfig = err.config;
    loadingCounter--;
    if (loadingCounter === 0) {
      store.dispatch(setIsLoading(false));
    }
    if (err?.response?.status === 401) {
      instance.defaults.headers.common.Authorization = '';
      return instance
        .post(
          `/account/auth/refresh/token`,
          JSON.parse(localStorage.getItem('refreshToken') || ''),
          {
            headers: {
              'Content-Type': 'text/plain',
              Authorization: '',
            },
          }
        )
        .then((res) => {
          localStorage.setItem('token', JSON.stringify(res?.data?.token));
          Object.assign(instance.defaults, {
            headers: {
              ...instance.defaults.headers,
              common: {
                ...instance.defaults.headers.common,
                Authorization: `Bearer ${res?.data?.token}`,
              },
            },
          });
          if (originalConfig.headers) {
            originalConfig.headers.Authorization = `Bearer ${res?.data?.token}`;
          }
          return instance(originalConfig);
        })
        .catch((error) => {
          localStorage.clear();
          instance.defaults.headers.common.Authorization = '';
          store.dispatch(removeUser());
          Promise.reject(error);
        });
    }

    if (err.response?.status === 404) {
      toast.error(err.response?.data?.message);
      return Promise.reject(err);
    }
    if (err.response?.status === 500) {
      toast.error('Internal server error');
      return Promise.reject(err);
    }
    if (err?.message === 'Network Error') {
      toast.error('Network Error');
      return Promise.reject(err);
    }
    return Promise.reject(err);
  }
);

export default instance;
