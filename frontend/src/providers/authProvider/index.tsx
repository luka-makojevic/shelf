import React, { useState, createContext, useEffect } from 'react'
import AuthService from '../../services/authServices'

interface ContextProps {
  user: {}
  login: (data: any, onSuccess: any, onError: any) => void
  register: (data: any, onSuccess: any, onError: any) => void
  loading: boolean
}

const defaultValue: ContextProps = {
  user: {},
  login: async () => {},
  register: async () => {},
  loading: false,
}

const AuthContext = createContext(defaultValue)

const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const [user, setUser] = useState<any>()
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    const userLocalStorage = localStorage.getItem('user')
      ? JSON.parse(localStorage.getItem('user') || '')
      : null
    if (userLocalStorage) {
      setUser(userLocalStorage)
    }
  }, [])

  const login = (data: any, onSuccess: any, onError: any) => {
    setLoading(true)
    AuthService.login(data)
      .then((res) => {
        if (res.data.accessToken) {
          localStorage.setItem('user', JSON.stringify(res.data))
          setUser(res.data)
          onSuccess()
        }
      })
      .catch((err) => {
        setUser(null)
        onError(err.message)
      })
      .finally(() => setLoading(false))
  }

  const register = (data: any, onSuccess: any, onError: any) => {
    setLoading(true)
    AuthService.register(data)
      .then((res: any) => {
        localStorage.setItem('user', JSON.stringify(res.data))
        setUser(res.data)
        onSuccess()
      })
      .catch((err) => onError(err.message))
      .finally(() => setLoading(false))
  }
  return (
    <AuthContext.Provider value={{ user, login, register, loading }}>
      {children}
    </AuthContext.Provider>
  )
}

export { AuthProvider, AuthContext }
