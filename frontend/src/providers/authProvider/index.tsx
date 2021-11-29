import React, { useState, createContext, useEffect } from 'react';
import { useNavigate, NavigateFunction } from 'react-router-dom';
import AuthService from '../../services/authServices';
import {
  RegisterData,
  LoginData,
  ContextTypes,
  UserType,
  ResetPasswordData,
} from '../../interfaces/types';

const defaultValue: ContextTypes = {
  user: null,
  login: async () => {},
  register: async () => {},
  resetPassword: async () => {},
  isLoading: false,
  accessToken: '',
};

const AuthContext = createContext(defaultValue);

const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const [user, setUser] = useState<UserType | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [accessToken, setAccesToken] = useState<string>('');
  const [resetToken, setResetToken] = useState<string>('');

  useEffect(() => {
    const userLocalStorage = localStorage.getItem('user')
      ? JSON.parse(localStorage.getItem('user') || '')
      : null;
    if (userLocalStorage) {
      setUser(userLocalStorage);
    }
  }, []);

  const navigation = useNavigate();
  const login = (
    data: LoginData,
    onSuccess: (navigation: NavigateFunction) => void,
    onError: (error: string) => void
  ) => {
    setIsLoading(true);
    AuthService.login(data)
      .then((res) => {
        if (res.data.jwtToken) {
          localStorage.setItem('user', JSON.stringify(res.data));
          setUser(res.data);
          setAccesToken(res.data.jwtToken);
          onSuccess(navigation);
        }
      })
      .catch((err) => {
        setUser(null);
        onError(err.response.data.message);
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
        onError(err.response.data.message);
      })
      .finally(() => setIsLoading(false));
  };

  const resetPassword = (
    data: ResetPasswordData,
    onSuccess: (navigate: NavigateFunction) => void,
    onError: (err: string) => void
  ) => {
    setIsLoading(true);
    AuthService.resetPassword(data)
      .then((res) => {
        if (res.data.resetToken) {
          setResetToken(res.data?.resetToken);
          onSuccess(navigation);
        }
      })
      .catch((err) => {
        onError(err.response?.data?.message);
      })
      .finally(() => setIsLoading(false));
  };

  return (
    <AuthContext.Provider
      value={{ user, login, register, resetPassword, isLoading, accessToken }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export { AuthProvider, AuthContext };
