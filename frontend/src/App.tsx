import { ThemeProvider } from 'styled-components';
import { theme } from './theme';
import Router from './components/router';
import { Header } from './components';
import { Table } from './components/table/table';

const App = () => (
  <>
    <Header />
    <Table mulitSelect />
  </>
);

export default App;
