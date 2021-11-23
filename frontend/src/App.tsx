import { ThemeProvider } from 'styled-components';
import { theme } from './theme';
import Router from './router';

const App = () => (
  <ThemeProvider theme={theme}>
    <Router />
  </ThemeProvider>
);

export default App;
