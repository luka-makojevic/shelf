import { useRoutes } from 'react-router-dom';
import { Role } from '../../utils/enums/roles';
import { Routes } from '../../utils/enums/routes';
import { RouteProps } from '../../interfaces/route';
import Dashboard from '../../pages/dashboard';
import ForgotPassword from '../../pages/forgotPassword';
import Landing from '../../pages/landing';
import EmailVerification from '../../pages/emailVerification';
import Login from '../../pages/login';
import Register from '../../pages/register';
import ResetPassword from '../../pages/resetPassword';
import TermsAndConditions from '../../pages/termsOfService';
import ProtectedRoute from './protectedRoute';
import PublicOnlyRoute from './publicOnlyRoute';
import SharedShelves from '../../pages/sharedShelves';
import Trash from '../../pages/trash';
import Functions from '../../pages/functions';
import Files from '../../pages/files';
import Profile from '../../pages/profileManagement';
import Users from '../../pages/users';
import Code from '../../pages/code';

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
      path: Routes.PROFILE,
      element: (
        <ProtectedRoute roles={[Role.USER, Role.ADMIN, Role.MASTER_ADMIN]}>
          <Profile />
        </ProtectedRoute>
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
      children: [
        {
          path: Routes.SHELVES,
          element: <Files />,
          children: [
            {
              path: Routes.FOLDERS,
              element: <Files />,
            },
          ],
        },
        {
          path: Routes.SHARED_SHELVES,
          element: <SharedShelves />,
        },
        {
          path: Routes.TRASH,
          element: <Trash />,
          children: [
            {
              path: Routes.FOLDERS,
              element: <Trash />,
            },
          ],
        },
        {
          path: Routes.FUNCTIONS,
          element: <Functions />,
        },
        {
          path: Routes.USERS,
          element: <Users />,
        },
        {
          path: Routes.CODE,
          element: <Code />,
        },
      ],
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
