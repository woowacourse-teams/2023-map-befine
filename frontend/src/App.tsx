import { RouterProvider } from 'react-router-dom';
import router from './router';
import TagContextProvider from './store/TagId';
import TopicsIdContextProvider from './store/TopicsId';

const App = () => {
  return (
    <TagContextProvider>
      <TopicsIdContextProvider>
        <RouterProvider router={router} />;
      </TopicsIdContextProvider>
    </TagContextProvider>
  );
};

export default App;
