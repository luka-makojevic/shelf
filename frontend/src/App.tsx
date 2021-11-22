import React from 'react'
import { useRoutes } from 'react-router-dom'
import { ThemeProvider } from 'styled-components'
import { Dashboard } from './pages/dashboard/index'
import { theme } from './theme/index'
import  Landing  from './pages/landing/index'
import  Login  from './pages/login/index'
import  Register  from './pages/register/index'
import { Routes } from './enums/routes'
import { RouteProps } from './interfaces/route'

function App() {
  const routes: RouteProps[] = [
    {
      path: Routes.HOME,
      element: <Landing />,
    },
    {
      path: Routes.REGISTER,
      element: <Register />,
    },
    {
      path: Routes.LOGIN,
      element: <Login />,
    },
    {
      path: Routes.DASHBOARD,
      element: <Dashboard />,
    },
  ]
  const routing = useRoutes(routes)

  return <ThemeProvider theme={theme}>{routing}</ThemeProvider>
}

export default App
