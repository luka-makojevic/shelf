import { useRoutes } from 'react-router-dom'
import { ThemeProvider } from 'styled-components'
import Dashboard from './pages/Dashboard'
import { theme } from './theme/index'
import Home from './pages/Home'
import Login from './pages/Login'
import Register from './pages/Register'
import { Routes } from './enums/routes'
import { RouteProps } from './interfaces/route'

function App() {
  const routes: RouteProps[] = [
    {
      path: Routes.HOME,
      element: <Home />,
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
