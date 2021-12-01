import React, { useState, createContext, useEffect } from 'react';
import AuthService from '../../services/authServices';
import {
  RegisterData,
  LoginData,
  ContextTypes,
  UserType,
  MicrosoftRegisterData,
  MicrosoftLoginData,
  LogoutData,
} from '../../interfaces/types';

const defaultValue: ContextTypes = {
  user: null,
  login: async () => {},
  logout: async () => {},
  microsoftLogin: async () => {},
  register: async () => {},
  microsoftRegister: async () => {},
  isLoading: false,
  accessToken: '',
};

const AuthContext = createContext(defaultValue);

const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const [user, setUser] = useState<UserType | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [accessToken, setAccessToken] = useState<string>('');

  useEffect(() => {
    const userLocalStorage = localStorage.getItem('user')
      ? JSON.parse(localStorage.getItem('user') || '')
      : null;
    const tokenLocalStorage = localStorage.getItem('token')
      ? JSON.parse(localStorage.getItem('token') || '')
      : null;
    if (userLocalStorage) {
      setUser(userLocalStorage);
    }
    if (tokenLocalStorage) {
      setAccessToken(tokenLocalStorage);
    }
  }, []);

  const login = (
    data: LoginData,
    onSuccess: () => void,
    onError: (error: string) => void
  ) => {
    setIsLoading(true);
    AuthService.login(data)
      .then((res) => {
        if (res.data.jwtToken) {
          localStorage.setItem('user', JSON.stringify(res.data));
          localStorage.setItem('token', JSON.stringify(res.data.jwtToken));
          setUser(res.data);
          setAccessToken(res.data.jwtToken);
          onSuccess();
        }
      })
      .catch((err) => {
        setUser(null);
        onError(err.response.data.message);
      })
      .finally(() => setIsLoading(false));
  };

  const microsoftLogin = (
    data: MicrosoftLoginData,
    onSuccess: () => void,
    onError: (error: string) => void
  ) => {
    setIsLoading(true);
    AuthService.microsoftLogin(data)
      .then((res) => {
        if (res.data.jwtToken) {
          localStorage.setItem('user', JSON.stringify(res.data));
          localStorage.setItem('token', JSON.stringify(res.data.jwtToken));
          setUser(res.data);
          setAccessToken(res.data.jwtToken);
          onSuccess();
        }
      })
      .catch((err) => {
        setUser(null);
        onError(err.response.data?.message);
      })
      .finally(() => setIsLoading(false));
  };

  const register = (
    data: RegisterData,
    onSuccess: () => void,
    onError: (error: string) => void
  ) => {
    setIsLoading(true);
    AuthService.register(data)
      .then((res) => {
        localStorage.setItem('user', JSON.stringify(res.data));
        setUser(res.data);
        onSuccess();
      })
      .catch((err) => {
        onError(err.response.data?.message);
      })
      .finally(() => setIsLoading(false));
  };

  const logout = (data: LogoutData) => {
    AuthService.logout(data)
      .then(() => {
        localStorage.removeItem('user');
        setUser(null);
      })
      .catch((err) => {
        console.log(err.response?.data.message);
      });
  };

  const microsoftRegister = (
    data: MicrosoftRegisterData,
    onSuccess: () => void,
    onError: (error: string) => void
  ) => {
    setIsLoading(true);
    AuthService.microsoftRegister(data)
      .then(() => {
        onSuccess();
      })
      .catch((err) => {
        onError(err.response.data.message);
      })
      .finally(() => setIsLoading(false));
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        login,
        logout,
        microsoftLogin,
        register,
        microsoftRegister,
        isLoading,
        accessToken,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export { AuthProvider, AuthContext };
