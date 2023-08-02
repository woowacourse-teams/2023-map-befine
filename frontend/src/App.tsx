import { RouterProvider } from 'react-router-dom';
import router from './router';
import TagContextProvider from './store/TagId';

const App = () => {
  return (
    <TagContextProvider>
      <RouterProvider router={router} />;
    </TagContextProvider>
  );
};

export default App;
