import { useRoutes } from 'react-router-dom';
import { Role } from '../enums/roles';
import { Routes } from '../enums/routes';
import { RouteProps } from '../interfaces/route';
import Dashboard from '../pages/dashboard';
import ForgotPassword from '../pages/forgotPassword';
import Landing from '../pages/landing';
import EmailVerification from '../pages/emailVerification';
import Login from '../pages/login';
import Register from '../pages/register';
import ResetPassword from '../pages/resetPassword';
import TermsAndConditions from '../pages/termsOfService';
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
      path: Routes.VERIFICATION,
      element: (
        <PublicOnlyRoute>
          <EmailVerification />
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
      path: Routes.TERMS_AND_CONDITIONS,
      element: (
        <PublicOnlyRoute>
          <TermsAndConditions />
        </PublicOnlyRoute>
      ),
    },
    {
      path: Routes.FORGOT_PASSWORD,
      element: (
        <PublicOnlyRoute>
          <ForgotPassword />
        </PublicOnlyRoute>
      ),
    },
    {
      path: Routes.RESET_PASSWORD,
      element: (
        <PublicOnlyRoute>
          <ResetPassword />
        </PublicOnlyRoute>
      ),
    },
  ];

  const routing = useRoutes(routes);

  return <>{routing}</>;
};

export default Router;
