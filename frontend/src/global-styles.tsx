import { createGlobalStyle } from 'styled-components';

export const GlobalStyles = createGlobalStyle`
  * {
    box-sizing:border-box;
  }
  html,
  body {
    margin: 0;
    font-family: 'Open Sans', sans-serif;
    -webkit-font-smoothing: antialiased;        
    -moz-osx-font-smoothing: grayscale;
  }
`;
