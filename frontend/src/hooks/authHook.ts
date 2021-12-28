import authServices from '../services/authServices';
import { LocalStorage } from '../services/localStorage';
import { useAppDispatch } from '../store/hooks';
import { removeUser } from '../store/userReducer';
import {
  LoginData,
  LogoutData,
  MicrosoftLoginData,
  MicrosoftRegisterData,
  RegisterData,
} from '../interfaces/dataTypes';

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
        LocalStorage.clear();
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
