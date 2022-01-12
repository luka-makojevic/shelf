import { ThemeProvider } from 'styled-components';
import { ToastContainer } from 'react-toastify';
import { theme } from './theme';
import Router from './components/router';
import 'react-toastify/dist/ReactToastify.css';

const App = () => (
  <ThemeProvider theme={theme}>
    <ToastContainer limit={3} />
    <Router />
  </ThemeProvider>
);

export default App;
