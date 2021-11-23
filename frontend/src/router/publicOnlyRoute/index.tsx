import { useContext } from 'react';
import { Navigate } from 'react-router-dom';
import { Routes } from '../../enums/routes';
import { AuthContext } from '../../providers/authProvider';

type PublicOnlyRouteProps = { children: JSX.Element };

const PublicOnlyRoute = ({ children }: PublicOnlyRouteProps) => {
  const { accessToken } = useContext(AuthContext);
  const isLoggedIn = accessToken !== '';

  return isLoggedIn ? <Navigate to={Routes.LOGIN} /> : children;
};

export default PublicOnlyRoute;
