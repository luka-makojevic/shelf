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
    if (loadingCounter === 0) store.dispatch(setIsLoading(false));
    if (err?.response?.data.status === 401 && !originalConfig.headers.Refresh) {
      instance
        .post(
          `/account/auth/refresh/token`,
          localStorage.getItem('refreshToken'),
          {
            headers: {
              Authorization: '',
            },
          }
        )
        .then((res) => {
          localStorage.setItem('token', JSON.stringify(res.data.token));
          instance.defaults.headers.common.Authorization = `Bearer ${res.data.token}`;
          if (originalConfig.headers) {
            originalConfig.headers.Authorization = `Bearer ${res.data.token}`;
          }
          return instance(originalConfig).then((response) =>
            Promise.resolve(response)
          );
        })
        .catch((error) => {
          localStorage.clear();
          delete instance.defaults.headers.common.Authorization;
          store.dispatch(removeUser());
          Promise.reject(error);
        });
    }

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
