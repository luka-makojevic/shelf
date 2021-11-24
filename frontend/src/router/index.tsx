import { useRoutes } from 'react-router-dom';
import { Role } from '../enums/roles';
import { Routes } from '../enums/routes';
import { RouteProps } from '../interfaces/route';
import Dashboard from '../pages/dashboard';
import ForgotPassword from '../pages/forgotPassword';
import Landing from '../pages/landing';
import Login from '../pages/login';
import Register from '../pages/register';
import ProtectedRoute from './protectedRoute';
import PublicOnlyRoute from './publicOnlyRoute';

const Router = () => {
  const routes: RouteProps[] = [
    {
      path: Routes.HOME,
      element: (
        <PublicOnlyRoute>
          <Landing />
        </PublicOnlyRoute>
      ),
    },
    {
      path: Routes.REGISTER,
      element: (
        <PublicOnlyRoute>
          <Register />
        </PublicOnlyRoute>
      ),
    },
    {
      path: Routes.LOGIN,
      element: (
        <PublicOnlyRoute>
          <Login />
        </PublicOnlyRoute>
      ),
    },
    {
      path: Routes.DASHBOARD,
      element: (
        <ProtectedRoute roles={[Role.USER, Role.ADMIN, Role.MASTER_ADMIN]}>
          <Dashboard />
        </ProtectedRoute>
      ),
    },
    {
      path: Routes.FORGOT_PASSWORD,
      element: <ForgotPassword />,
    },
  ];

  const routing = useRoutes(routes);

  return <>{routing}</>;
};

export default Router;
