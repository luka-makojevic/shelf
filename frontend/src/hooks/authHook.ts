import authServices from '../services/authServices';
import { LocalStorage } from '../services/localStorage';
import { useAppDispatch } from '../store/hooks';
import { setLoading } from '../store/loadingReducer';
import { removeUser, setUser } from '../store/userReducer';
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
    onError: (error: string) => void
  ) => {
    dispatch(setLoading(true));

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
        dispatch(removeUser());
        onError(err.response?.data.message);
      })
      .finally(() => {
        dispatch(setLoading(false));
        const user = LocalStorage.get('user')
          ? JSON.parse(LocalStorage.get('user') || '')
          : null;
        dispatch(setUser(user));
      });
  };

  const microsoftLogin = (
    data: MicrosoftLoginData,
    onSuccess: () => void,
    onError: (error: string) => void
  ) => {
    dispatch(setLoading(true));
    authServices
      .microsoftLogin(data)
      .then((res) => {
        if (res.data.jwtToken) {
          localStorage.setItem('user', JSON.stringify(res.data));
          localStorage.setItem('token', JSON.stringify(res.data.jwtToken));
          localStorage.setItem(
            'refreshToken',
            JSON.stringify(res.data.jwtRefreshToken)
          );
          dispatch(setUser(res.data));
          onSuccess();
        }
      })
      .catch((err) => {
        dispatch(removeUser());
        onError(err.response?.data.message);
      })
      .finally(() => dispatch(setLoading(false)));
  };

  const register = (
    data: RegisterData,
    onSuccess: () => void,
    onError: (error: string) => void
  ) => {
    dispatch(setLoading(true));
    authServices
      .register(data)
      .then(() => {
        onSuccess();
      })
      .catch((err) => {
        onError(err.response?.data.message);
      })
      .finally(() => dispatch(setLoading(false)));
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
    dispatch(setLoading(true));
    authServices
      .microsoftRegister(data)
      .then(() => {
        onSuccess();
      })
      .catch((err) => {
        onError(err.response?.data.message);
      })
      .finally(() => dispatch(setLoading(false)));
  };

  return { login, microsoftLogin, register, microsoftRegister, logout };
};
