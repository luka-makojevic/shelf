import { Navigate } from 'react-router-dom';
import { Routes } from '../../../utils/enums/routes';
import { LocalStorage } from '../../../services/localStorage';

type PublicOnlyRouteProps = { children: JSX.Element };

const PublicOnlyRoute = ({ children }: PublicOnlyRouteProps) => {
  const accessToken = LocalStorage.get('token');
  const isLoggedIn = accessToken;

  return isLoggedIn ? <Navigate to={Routes.DASHBOARD} /> : children;
};

export default PublicOnlyRoute;
