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
import KakaoRedirectPage from './pages/KaKaoRedirectPage';
import ErrorPage from './pages/ErrorPage';
import AskLoginPage from './pages/AskLoginPage';
import { ReactNode } from 'react';
import AuthLayout from './components/Layout/AuthLayout';
import NotFound from './components/NotFound';
import Profile from './pages/Profile';
import LoginError from './components/NotFound/LoginError';

interface routeElement {
  path: string;
  element: ReactNode;
  withAuth: boolean;
  redirectPath?: string;
  errorElement?: ReactNode;
  children: { path: string; element: ReactNode; withAuth: boolean }[];
}

const routes: routeElement[] = [
  {
    path: '/',
    element: <RootPage />,
    errorElement: <NotFound />,
    withAuth: false,

    children: [
      {
        path: '',
        element: <Home />,
        withAuth: false,
      },
      {
        path: 'topics/:topicId',
        element: <SelectedTopic />,
        withAuth: false,
      },
      {
        path: 'new-topic',
        element: <NewTopic />,
        withAuth: true,
      },
      {
        path: 'new-pin',
        element: <NewPin />,
        withAuth: true,
      },
      {
        path: 'see-all/popularity',
        element: <SeeAllPopularTopics />,
        withAuth: false,
      },
      {
        path: 'see-all/near',
        element: <SeeAllNearTopics />,
        withAuth: false,
      },
      {
        path: 'see-all/latest',
        element: <SeeAllLatestTopics />,
        withAuth: false,
      },
      {
        path: 'see-together',
        element: <SeeTogetherTopics />,
        withAuth: true,
      },
      {
        path: 'favorite',
        element: <Favorite />,
        withAuth: true,
      },
      {
        path: 'my-page',
        element: <Profile />,
        withAuth: true,
      },
      {
        path: 'oauth/redirected/kakao',
        element: <KakaoRedirectPage />,
        withAuth: false,
      },
      {
        path: '/askLogin',
        element: <LoginError />,
        withAuth: false,
      },
    ],
  },
];

const router = createBrowserRouter(
  routes.map((route) => {
    const childrenRoutes = route.children?.map((childRoute) => {
      if (childRoute.withAuth) {
        return {
          path: childRoute.path,
          element: (
            <AuthLayout withAuth={childRoute.withAuth}>
              {childRoute.element}
            </AuthLayout>
          ),
          errorElement: <ErrorPage />,
        };
      }

      return {
        path: childRoute.path,
        element: childRoute.element,
        errorElement: <ErrorPage />,
      };
    });

    return {
      ...route,
      children: childrenRoutes,
    };
  }),
);

export default router;
