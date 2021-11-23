import React, { useState, createContext, useEffect } from 'react';
import { useNavigate, NavigateFunction } from 'react-router-dom';
import AuthService from '../../services/authServices';
import {
  RegisterData,
  LoginData,
  ContextTypes,
  UserType,
} from '../../interfaces/types';

const defaultValue: ContextTypes = {
  user: {} as UserType,
  login: async () => {},
  register: async () => {},
  loading: false,
  accessToken: '',
};

const AuthContext = createContext(defaultValue);

const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const [user, setUser] = useState<UserType | null>();
  const [loading, setLoading] = useState<boolean>(false);
  const [accessToken, setAccesToken] = useState<string>('');

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
    setLoading(true);
    AuthService.login(data)
      .then((res) => {
        if (res.data.jwtToken) {
          localStorage.setItem('user', JSON.stringify(res));
          setUser(res.data);
          setAccesToken(res.data.jwtToken);
          onSuccess(navigation);
        }
      })
      .catch((err) => {
        setUser(null);
        onError(err.response.data.message);
      })
      .finally(() => setLoading(false));
  };

  const register = (
    data: RegisterData,
    onSuccess: () => void,
    onError: (error: string) => void
  ) => {
    setLoading(true);
    AuthService.register(data)
      .then((res) => {
        localStorage.setItem('user', JSON.stringify(res.data));
        setUser(res.data);
        onSuccess();
      })
      .catch((err) => {
        onError(err.response.data.message);
      })
      .finally(() => setLoading(false));
  };
  return (
    <AuthContext.Provider
      value={{ user, login, register, loading, accessToken }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export { AuthProvider, AuthContext };
