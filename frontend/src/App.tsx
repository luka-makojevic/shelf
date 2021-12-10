import { ThemeProvider } from 'styled-components';
import { theme } from './theme';
import Router from './components/router';

const App = () => (
  <ThemeProvider theme={theme}>
    <Router />
  </ThemeProvider>
);

export default App;
