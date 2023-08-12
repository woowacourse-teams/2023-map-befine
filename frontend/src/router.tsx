import { createBrowserRouter } from 'react-router-dom';
import Home from './pages/Home';
import NewPin from './pages/NewPin';
import NewTopic from './pages/NewTopic';
import RootPage from './pages/RootPage';
import SelectedTopic from './pages/SelectedTopic';
import SeeAllPopularTopics from './pages/SeeAllPopularTopics';
import SeeAllNearTopics from './pages/SeeAllNearTopics';
import SeeAllLatestTopics from './pages/SeeAllLatestTopics';
import SeeTogetherTopics from './pages/SeeTogetherTopics';
import Favorite from './pages/Favorite';

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
        path: 'see-all/popularity',
        element: <SeeAllPopularTopics />,
      },
      {
        path: 'see-all/near',
        element: <SeeAllNearTopics />,
      },
      {
        path: 'see-all/latest',
        element: <SeeAllLatestTopics />,
      },
      {
        path: 'see-together',
        element: <SeeTogetherTopics />,
      },
      {
        path: 'favorite',
        element: <Favorite />,
      },
    ],
  },
];

export default createBrowserRouter(routes);
