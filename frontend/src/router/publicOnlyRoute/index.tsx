import { Navigate } from 'react-router-dom';
import { Routes } from '../../enums/routes';

function PublicOnlyRoute({ children }: { children: JSX.Element }) {
  // from AuthProvider
  const isLoggedIn = false;

  return isLoggedIn ? <Navigate to={Routes.LOGIN} /> : children;
}

export default PublicOnlyRoute;
