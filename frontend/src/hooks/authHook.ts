import authServices from '../services/authServices';
import { useAppDispatch } from '../store/hooks';
import { removeUser } from '../store/userReducer';
import {
  LoginData,
  LogoutData,
  MicrosoftLoginData,
  MicrosoftRegisterData,
  RegisterData,
} from '../interfaces/dataTypes';
import instance from '../api/axiosInstance';

export const useAuth = () => {
  const dispatch = useAppDispatch();

  const login = (
    data: LoginData,
    onSuccess: () => void,
    onError: (err: string) => void
  ) => {
    authServices
      .login(data)
      .then((res) => {
        if (res.data.jwtToken) {
          localStorage.setItem('token', JSON.stringify(res.data.jwtToken));
          localStorage.setItem(
            'refreshToken',
            JSON.stringify(res.data.jwtRefreshToken)
          );
          instance.defaults.headers.common.Authorization = `Bearer ${res.data.jwtToken}`;
          onSuccess();
        }
      })
      .catch((err) => {
        onError(err.response?.data?.message);
      });
  };

  const microsoftLogin = (
    data: MicrosoftLoginData,
    onSuccess: () => void,
    onError: (error: string) => void
  ) => {
    authServices
      .microsoftLogin(data)
      .then((res) => {
        if (res.data.jwtToken) {
          localStorage.setItem('token', JSON.stringify(res.data.jwtToken));
          localStorage.setItem(
            'refreshToken',
            JSON.stringify(res.data.jwtRefreshToken)
          );
          onSuccess();
        }
      })
      .catch((err) => {
        onError(err.response?.data.message);
      });
  };

  const register = (
    data: RegisterData,
    onSuccess: () => void,
    onError: (error: string) => void
  ) => {
    authServices
      .register(data)
      .then(() => {
        onSuccess();
      })
      .catch((err) => {
        onError(err.response?.data.message);
      });
  };

  const logout = (
    data: LogoutData,
    onSuccess: () => void,
    onError: (error: string) => void
  ) => {
    authServices
      .logout(data)
      .then(() => {
        delete instance.defaults.headers.common.Authorization;
        localStorage.clear();
        dispatch(removeUser());
        onSuccess();
      })
      .catch((err) => {
        onError(err.response?.data.message);
      });
  };

  const microsoftRegister = (
    data: MicrosoftRegisterData,
    onSuccess: () => void,
    onError: (error: string) => void
  ) => {
    authServices
      .microsoftRegister(data)
      .then(() => {
        onSuccess();
      })
      .catch((err) => {
        onError(err.response?.data.message);
      });
  };

  return { login, microsoftLogin, register, microsoftRegister, logout };
};
