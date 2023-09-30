import ReactDOM from 'react-dom/client';
import App from './App';
import { ThemeProvider } from 'styled-components';
import theme from './themes';
import GlobalStyle from './GlobalStyle';
import ErrorBoundary from './components/ErrorBoundary';
import NotFound from './pages/NotFound';
import ReactGA from 'react-ga4';
import * as serviceWorkerRegistration from './serviceWorkerRegistration';

const rootElement = document.getElementById('root');
if (!rootElement) throw new Error('Failed to find the root element');
const root = ReactDOM.createRoot(rootElement);

serviceWorkerRegistration.register();
// if (process.env.NODE_ENV === 'development') {
//   const { worker } = require('./mocks/browser');
//   worker.start({ quiet: true });
// }

if (process.env.REACT_APP_GOOGLE_ANALYTICS) {
  ReactGA.initialize(process.env.REACT_APP_GOOGLE_ANALYTICS);
}

root.render(
  <ThemeProvider theme={theme}>
    <GlobalStyle />
    <ErrorBoundary fallback={NotFound}>
      <App />
    </ErrorBoundary>
  </ThemeProvider>,
);
