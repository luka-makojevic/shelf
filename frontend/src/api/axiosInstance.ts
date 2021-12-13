import axios, { AxiosRequestConfig } from 'axios';

const API_URL = 'http://10.10.0.136:8080/';

const instance = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

instance.interceptors.request.use(
  (config: AxiosRequestConfig) => {
    const token = JSON.parse(localStorage.getItem('token') || '');

    if (token) {
      // eslint-disable-next-line no-param-reassign
      if (config.headers) config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

instance.interceptors.response.use(
  (res) => res,
  async (err) => {
    const originalConfig = err.config;
    if (err.response.data.message === 'Token expired.') {
      try {
        const response = await axios.get(`${API_URL}auth/refresh/token`, {
          headers: {
            Authorization: `Bearer ${JSON.parse(
              localStorage.getItem('refreshToken') || ''
            )}`,
          },
        });

        const { token: acccesToken } = response.data;
        localStorage.setItem('token', JSON.stringify(acccesToken));

        return await instance(originalConfig);
      } catch (_error) {
        return Promise.reject(_error);
      }
    }

    return Promise.reject(err);
  }
);

export default instance;
