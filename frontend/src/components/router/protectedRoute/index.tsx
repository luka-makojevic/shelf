import { toast } from 'react-toastify';
import { useEffect } from 'react';
import { Navigate } from 'react-router-dom';
import { LocalStorage } from '../../../services/localStorage';
import { useAppDispatch, useAppSelector } from '../../../store/hooks';
import { setUser } from '../../../store/userReducer';
import { Routes } from '../../../utils/enums/routes';
import userServices from '../../../services/userServices';

type ProtectedRouteProps = {
  children: JSX.Element;
  roles: number[];
};

const ProtectedRoute = ({ children, roles }: ProtectedRouteProps) => {
  const dispatch = useAppDispatch();
  const accessToken = LocalStorage.get('token');
  const user = useAppSelector((state) => state.user.user);

  useEffect(() => {
    if (!user)
      userServices
        .getUser()
        .then((res) => {
          dispatch(setUser(res.data));
        })
        .catch((err) => {
          toast.error(err.repsonse?.data?.message);
        });
  }, []);

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
