import ReactDOM from 'react-dom/client';
import App from './App';
import { ThemeProvider } from 'styled-components';
import theme from './themes';
import GlobalStyle from './GlobalStyle';
import { StrictMode } from 'react';
import AbsoluteModalContextProvider from './context/AbsoluteModalContext';

const rootElement = document.getElementById('root');
if (!rootElement) throw new Error('Failed to find the root element');
const root = ReactDOM.createRoot(rootElement);

// if (process.env.NODE_ENV === 'development') {
//   const { worker } = require('./mocks/browser');
//   worker.start({ quiet: true });
// }

root.render(
  <ThemeProvider theme={theme}>
    <AbsoluteModalContextProvider>
      <GlobalStyle />
      <App />
    </AbsoluteModalContextProvider>
  </ThemeProvider>,
);
