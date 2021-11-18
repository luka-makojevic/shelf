import axios from 'axios'

const API_URL = 'http://10.10.0.120:8080/users/'

const register = (data: any) => {
  console.log(data)
  return axios.post(`${API_URL}register`, data)
}

const login = (data: any) => {
  return axios.post(`${API_URL}login`, data)
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
