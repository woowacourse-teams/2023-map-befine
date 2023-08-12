import { createBrowserRouter } from 'react-router-dom';
import Home from './pages/Home';
import NewPin from './pages/NewPin';
import NewTopic from './pages/NewTopic';
import RootPage from './pages/RootPage';
import SelectedTopic from './pages/SelectedTopic';
import SeeAllPopularTopics from './pages/SeeAllPopularTopics';
import SeeAllNearTopics from './pages/SeeAllNearTopics';
import SeeAllLatestTopics from './pages/SeeAllLatestTopics';

const routes = [
  {
    path: '/',
    element: <RootPage />,
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
        path: 'topics/see-all/popularity',
        element: <SeeAllPopularTopics />,
      },
      {
        path: 'topics/see-all/near',
        element: <SeeAllNearTopics />,
      },
      {
        path: 'topics/see-all/latest',
        element: <SeeAllLatestTopics />,
      },
    ],
  },
];

export default createBrowserRouter(routes);
