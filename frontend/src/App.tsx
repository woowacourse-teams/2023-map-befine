import { RouterProvider } from 'react-router-dom';
import router from './router';
import RouteChangeTracker from './utils/RouteChangeTracker';

const App = () => {
  RouteChangeTracker();

  return <RouterProvider router={router} />;
};

export default App;
