import axios from 'axios'

const API_URL = 'http://localhost:8080/users/'

const register = (data: any) => {
  return axios.post(`${API_URL}register`, {
    data,
  })
}

const login = (data: any) => {
  return axios.post(`${API_URL}signin`, {
    data,
  })
}

const logout = (setUser: any) => {
  localStorage.removeItem('user')
  setUser(null)
}

export default {
  register,
  login,
  logout,
}
