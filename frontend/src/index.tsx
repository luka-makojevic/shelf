import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter as Router } from 'react-router-dom';
import { MsalProvider } from '@azure/msal-react';
import { PublicClientApplication } from '@azure/msal-browser';
import App from './App';
import { GlobalStyles } from './global-styles';
import { AuthProvider } from './providers/authProvider';
import { msalConfig } from './azure/authConfig';

const msalInstance = new PublicClientApplication(msalConfig);

ReactDOM.render(
  <React.StrictMode>
    <GlobalStyles />
    <Router>
      <MsalProvider instance={msalInstance}>
        <AuthProvider>
          <App />
        </AuthProvider>
      </MsalProvider>
    </Router>
  </React.StrictMode>,
  document.getElementById('root')
);
