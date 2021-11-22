import axios from 'axios'
import { RegisterData, LoginData } from '../interfaces/types'

const API_URL = 'http://10.10.0.117:8080/users/'

const register = (data: RegisterData) => axios.post(`${API_URL}register`, data)

const login = (data: LoginData) => axios.post(`${API_URL}login`, data)

const logout = (setUser: (arg: null) => void) => {
  // missing features from backend
  localStorage.removeItem('user')
  setUser(null)
}

export default {
  register,
  login,
  logout,
}
