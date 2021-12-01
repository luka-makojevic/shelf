export const parameters = {
  actions: { argTypesRegex: '^on[A-Z].*' },
  controls: {
    matchers: {
      color: /(background|color)$/i,
      date: /Date$/,
    },
  },
};
import React from 'react';

import { theme } from '../src/theme';
import { ThemeProvider } from 'styled-components';
import { GlobalStyles } from '../src/global-styles';
import { BrowserRouter } from 'react-router-dom';
export const decorators = [
  (Story) => (
    <ThemeProvider theme={theme}>
      <GlobalStyles />
      <BrowserRouter>
        <Story />
      </BrowserRouter>
    </ThemeProvider>
  ),
];
