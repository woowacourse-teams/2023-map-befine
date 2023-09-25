import { RouterProvider } from 'react-router-dom';
import router from './router';
import { useEffect } from 'react';
import ReactGA from 'react-ga';
import { createBrowserHistory as createHistory } from 'history';

const App = () => {
  const history = createHistory();
  
  useEffect(() => {
    ReactGA.initialize('G-RRSTX6Y61Y', { debug: true });
    history.listen((location: any) => {
      ReactGA.set({ page: location.pathname });
      ReactGA.pageview(location.pathname);
    });
  }, []);
  return <RouterProvider router={router} />;
};

export default App;
