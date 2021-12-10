import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter as Router } from 'react-router-dom';
import { MsalProvider } from '@azure/msal-react';
import { PublicClientApplication } from '@azure/msal-browser';
import { Provider } from 'react-redux';
import App from './App';
import { GlobalStyles } from './global-styles';
import { msalConfig } from './azure/authConfig';
import { store } from './store/store';

const msalInstance = new PublicClientApplication(msalConfig);

ReactDOM.render(
  <Provider store={store}>
    <React.StrictMode>
      <GlobalStyles />
      <Router>
        <MsalProvider instance={msalInstance}>
          <App />
        </MsalProvider>
      </Router>
    </React.StrictMode>
  </Provider>,
  document.getElementById('root')
);
