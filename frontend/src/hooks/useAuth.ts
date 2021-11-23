import axios from 'axios';
// import { useState } from 'react'

const API_URL = 'http://10.10.0.120:8080/users/';

export const useAuthService = () => {
  // const [user, setUser] = useState<string>(
  //   JSON.parse(localStorage.getItem('user') || '')
  // )

  const register = (data: {}) => {
    return axios
      .post(`${API_URL}register`, {
        data,
      })
      .then((res) => {
        if (res) {
          localStorage.setItem('user', JSON.stringify(res.data));
          // setUser(res.data)
        }
      });
  };

  const login = (data: {}) => {
    return axios
      .post(`${API_URL}register`, {
        data,
      })
      .then((res) => {
        if (res.data.accessToken) {
          localStorage.setItem('user', JSON.stringify(res.data));
          // setUser(res.data)
        }
      });
  };

  return { register, login };
};
