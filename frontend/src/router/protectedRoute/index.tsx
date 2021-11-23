import { Navigate } from 'react-router-dom';
import { Role } from '../../enums/roles';
import { Routes } from '../../enums/routes';

function ProtectedRoute({
  children,
  roles,
}: {
  children: JSX.Element;
  roles: number[];
}) {
  // from AuthProvider
  const isLoggedIn = false;
  const user = { role: Role.USER };

  const userHasRequiredRole = user && roles.includes(user.role);

  if (!isLoggedIn) {
    return <Navigate to={Routes.LOGIN} />;
  }

  if (isLoggedIn && !userHasRequiredRole) {
    return <Navigate to={Routes.DASHBOARD} />;
  }

  return children;
}

export default ProtectedRoute;
