import axios from 'axios';
import { useEffect } from 'react';
import { Navigate } from 'react-router-dom';
import { API_URL_ACCOUNT } from '../../../api/api';
import instance from '../../../api/axiosInstance';
import { LocalStorage } from '../../../services/localStorage';
import { useAppDispatch, useAppSelector } from '../../../store/hooks';
import { setUser } from '../../../store/userReducer';
import { Routes } from '../../../utils/enums/routes';
import jwt_decode from 'jwt-decode';

type ProtectedRouteProps = {
  children: JSX.Element;
  roles: number[];
};

const ProtectedRoute = ({ children, roles }: ProtectedRouteProps) => {
  const dispatch = useAppDispatch();
  const accessToken = LocalStorage.get('token');
  useEffect(() => {
    const { jti }: { jti: string } = jwt_decode(accessToken || '');
    instance.get(`${API_URL_ACCOUNT}users/${jti}`).then((res) => {
      dispatch(setUser(res.data));
    });
  }, []);
  const user = useAppSelector((state) => state.user.user);
  const isLoggedIn = accessToken;
  const userHasRequiredRole = user && roles.includes(user?.role.id);

  if (!isLoggedIn) {
    return <Navigate to={Routes.HOME} />;
  }
  if (user && isLoggedIn && !userHasRequiredRole) {
    return <Navigate to={Routes.DASHBOARD} />;
  }
  return children;
};

export default ProtectedRoute;
