import { Navigate } from 'react-router-dom';
import { LocalStorage } from '../../../services/localStorage';
import { useAppSelector } from '../../../store/hooks';
import { Routes } from '../../../utils/enums/routes';

type ProtectedRouteProps = {
  children: JSX.Element;
  roles: number[];
};

const ProtectedRoute = ({ children, roles }: ProtectedRouteProps) => {
  const accessToken = LocalStorage.get('token');
  const user = useAppSelector((state) => state.user.user);
  const isLoggedIn = accessToken;
  const userHasRequiredRole = user && roles.includes(user?.role);

  if (!isLoggedIn) {
    return <Navigate to={Routes.HOME} />;
  }
  if (user && isLoggedIn && !userHasRequiredRole) {
    return <Navigate to={Routes.DASHBOARD} />;
  }
  return children;
};

export default ProtectedRoute;
