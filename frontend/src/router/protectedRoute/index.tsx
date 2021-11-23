import { useContext } from 'react';
import { Navigate } from 'react-router-dom';
import { Routes } from '../../enums/routes';
import { AuthContext } from '../../providers/authProvider';

type ProtectedRouteProps = {
  children: JSX.Element;
  roles: number[];
};

const ProtectedRoute = ({ children, roles }: ProtectedRouteProps) => {
  const { accessToken, user } = useContext(AuthContext);
  const isLoggedIn = accessToken !== '';

  const userHasRequiredRole = user && roles.includes(user?.role);

  if (!isLoggedIn) {
    return <Navigate to={Routes.LOGIN} />;
  }

  if (isLoggedIn && !userHasRequiredRole) {
    return <Navigate to={Routes.DASHBOARD} />;
  }

  return children;
};

export default ProtectedRoute;
