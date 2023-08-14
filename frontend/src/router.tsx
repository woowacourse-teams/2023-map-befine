import { createBrowserRouter } from 'react-router-dom';
import Home from './pages/Home';
import NewPin from './pages/NewPin';
import NewTopic from './pages/NewTopic';
import RootPage from './pages/RootPage';
import SelectedTopic from './pages/SelectedTopic';
import SeeAllTopics from './pages/SeeAllTopics';
import NotFound from './components/NotFound';

const routes = [
  {
    path: '/',
    element: <RootPage />,
    errorElement: <NotFound />,
    children: [
      {
        path: '',
        element: <Home />,
      },
      {
        path: 'topics/:topicId',
        element: <SelectedTopic />,
      },
      {
        path: 'new-topic',
        element: <NewTopic />,
      },
      {
        path: 'new-pin',
        element: <NewPin />,
      },
      {
        path: 'topics/see-all',
        element: <SeeAllTopics />,
      },
    ],
  },
];

export default createBrowserRouter(routes);
