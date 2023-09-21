import { createBrowserRouter } from 'react-router-dom';
import Home from './pages/Home';
import RootPage from './pages/RootPage';
import { ReactNode, lazy } from 'react';
import AuthLayout from './components/Layout/AuthLayout';
import NotFound from './pages/NotFound';

const SelectedTopic = lazy(() => import('./pages/SelectedTopic'));
const NewPin = lazy(() => import('./pages/NewPin'));
const NewTopic = lazy(() => import('./pages/NewTopic'));
const SeeAllPopularTopics = lazy(() => import('./pages/SeeAllPopularTopics'));
const SeeAllNearTopics = lazy(() => import('./pages/SeeAllNearTopics'));
const SeeAllLatestTopics = lazy(() => import('./pages/SeeAllLatestTopics'));
const KakaoRedirect = lazy(() => import('./pages/KakaoRedirect'));
const SeeTogetherTopics = lazy(() => import('./pages/SeeTogetherTopics'));
const Profile = lazy(() => import('./pages/Profile'));
const AskLogin = lazy(() => import('./pages/AskLogin'));
const Bookmark = lazy(() => import('./pages/Bookmark'));

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
        element: <Bookmark />,
        withAuth: true,
      },
      {
        path: 'my-page',
        element: <Profile />,
        withAuth: true,
      },
      {
        path: '/askLogin',
        element: <AskLogin />,
        withAuth: false,
      },
      {
        path: '/oauth/redirected/kakao',
        element: <KakaoRedirect />,
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
          errorElement: <NotFound />,
        };
      }

      return {
        path: childRoute.path,
        element: childRoute.element,
        errorElement: <NotFound />,
      };
    });

    return {
      ...route,
      children: childrenRoutes,
    };
  }),
);

export default router;
