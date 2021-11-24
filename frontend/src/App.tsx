import { ThemeProvider } from 'styled-components';
import { theme } from './theme';
<<<<<<< HEAD
import Landing from './pages/landing';
import Login from './pages/login';
import Register from './pages/register';
import { Routes } from './enums/routes';
import { RouteProps } from './interfaces/route';
import TermsAndConditions from './pages/termsAndConditions';

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
    {
      path: Routes.TERMS,
      element: <TermsAndConditions />,
    },
  ];
  const routing = useRoutes(routes);

  return <ThemeProvider theme={theme}>{routing}</ThemeProvider>;
}
=======
import Router from './router';

const App = () => (
  <ThemeProvider theme={theme}>
    <Router />
  </ThemeProvider>
);
>>>>>>> master

export default App;
