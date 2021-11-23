import React from 'react'
import { useRoutes } from 'react-router-dom'
import { ThemeProvider } from 'styled-components'
import { Dashboard } from './pages/dashboard'
import { theme } from './theme'
import  Landing  from './pages/landing'
import  Login  from './pages/login'
import  Register  from './pages/register'
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
