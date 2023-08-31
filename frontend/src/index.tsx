import ReactDOM from 'react-dom/client';
import App from './App';
import { ThemeProvider } from 'styled-components';
import theme from './themes';
import GlobalStyle from './GlobalStyle';
import ErrorBoundary from './components/ErrorBoundary';
import NotFound from './components/NotFound';

const rootElement = document.getElementById('root');
if (!rootElement) throw new Error('Failed to find the root element');
const root = ReactDOM.createRoot(rootElement);

if (process.env.NODE_ENV === 'development') {
  const { worker } = require('./mocks/browser');
  worker.start({ quiet: true });
}

root.render(
  <ThemeProvider theme={theme}>
    <GlobalStyle />
    <ErrorBoundary fallback={NotFound}>
      <App />
    </ErrorBoundary>
  </ThemeProvider>,
);
