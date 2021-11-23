import { ThemeProvider } from 'styled-components';
import { theme } from './theme/index';
import Router from './router';

function App() {
  return (
    <ThemeProvider theme={theme}>
      <Router />
    </ThemeProvider>
  );
}

export default App;
