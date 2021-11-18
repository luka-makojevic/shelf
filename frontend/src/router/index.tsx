import { useRoutes } from 'react-router'
import { Role } from '../enums/roles'
import { Routes } from '../enums/routes'
import { RouteProps } from '../interfaces/route'
import { Dashboard } from '../pages/dashboard/index'
import { Landing } from '../pages/landing/index'
import { Login } from '../pages/login/index'
import { Register } from '../pages/register/index'
import ProtectedRoute from './protectedRoute'
import PublicOnlyRoute from './publicOnlyRoute'

function Router() {
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
  ]

  const routing = useRoutes(routes)

  return <>{routing}</>
}

export default Router
