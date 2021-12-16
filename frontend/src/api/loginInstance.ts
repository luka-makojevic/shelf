import axios from 'axios';
import { LocalStorage } from '../services/localStorage';

const API_URL = 'http://10.10.0.136:8080/account/';

const loginInstance = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

loginInstance.interceptors.response.use(
  async (res) => {
    const resConfig = res.config;

    const user = LocalStorage.get('user')
      ? JSON.parse(LocalStorage.get('user') || '')
      : null;

    if (!user) {
      try {
        const response = await axios.get(`${API_URL}users/${res.data.id}`, {
          headers: {
            Authorization: `Bearer ${res.data.jwtToken}`,
          },
        });

        const userData = response.data;
        localStorage.setItem('user', JSON.stringify(userData));

        return await loginInstance(resConfig);
      } catch (_error) {
        return Promise.reject(_error);
      }
    }
    return res;
  },
  (err) => err
);

export default loginInstance;
