import axios from 'axios'

const API_URL = 'http://localhost:8080/api/auth/signin'

export const login = (email: string, password: string) => {
  return axios.post(API_URL, { email, password }).then((res) => {
    if (res.data.accessToken) {
      localStorage.setItem('user', JSON.stringify(res.data))
    }
  })
}
