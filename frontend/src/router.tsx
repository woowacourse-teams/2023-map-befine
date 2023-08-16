import { createBrowserRouter } from 'react-router-dom';
import Home from './pages/Home';
import NewPin from './pages/NewPin';
import NewTopic from './pages/NewTopic';
import RootPage from './pages/RootPage';
import SelectedTopic from './pages/SelectedTopic';
import SeeAllTopics from './pages/SeeAllTopics';
import KakaoRedirectPage from './pages/KaKaoRedirectPage';
import ErrorPage from './pages/ErrorPage';
import AskLoginPage from './pages/AskLoginPage';
import { ReactNode } from 'react';
import AuthLayout from './components/Layout/AuthLayout';
import MoaBogi from './pages/MoaBogi';
import Zzlegyeo from './pages/Zzlegyeo';
import NotFound from './components/NotFound';
import My from './pages/My';

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
        path: 'moabogi',
        element: <MoaBogi />,
        withAuth: true,
      },
      {
        path: 'my',
        element: <My />,
        withAuth: true,
      },
      {
        path: 'zzlegyeo',
        element: <Zzlegyeo />,
        withAuth: true,
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
        path: 'topics/see-all',
        element: <SeeAllTopics />,
        withAuth: false,
      },
      {
        path: 'oauth/redirected/kakao',
        element: <KakaoRedirectPage />,
        withAuth: false,
      },
      {
        path: '/askLogin',
        element: <AskLoginPage />,
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
