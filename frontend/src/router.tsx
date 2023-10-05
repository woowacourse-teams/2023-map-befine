import { Suspense, lazy } from 'react';
import { createBrowserRouter } from 'react-router-dom';
import Home from './pages/Home';
import RootPage from './pages/RootPage';
import { ReactNode } from 'react';
import AuthLayout from './components/Layout/AuthLayout';
import NotFound from './pages/NotFound';
import Search from './pages/Search';

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

interface SuspenseCompProps {
  children: ReactNode;
}

const SuspenseComp = ({ children }: SuspenseCompProps) => {
  return <Suspense fallback={null}>{children}</Suspense>;
};

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
        element: (
          <SuspenseComp>
            <SelectedTopic />
          </SuspenseComp>
        ),
        withAuth: false,
      },
      {
        path: 'new-topic',
        element: (
          <SuspenseComp>
            <NewTopic />
          </SuspenseComp>
        ),
        withAuth: true,
      },
      {
        path: 'new-pin',
        element: (
          <SuspenseComp>
            <NewPin />
          </SuspenseComp>
        ),
        withAuth: true,
      },
      {
        path: 'see-all/popularity',
        element: (
          <SuspenseComp>
            <SeeAllPopularTopics />
          </SuspenseComp>
        ),
        withAuth: false,
      },
      {
        path: 'see-all/near',
        element: (
          <SuspenseComp>
            <SeeAllNearTopics />
          </SuspenseComp>
        ),
        withAuth: false,
      },
      {
        path: 'see-all/latest',
        element: (
          <SuspenseComp>
            <SeeAllLatestTopics />
          </SuspenseComp>
        ),
        withAuth: false,
      },
      {
        path: 'see-together',
        element: (
          <SuspenseComp>
            <SeeTogetherTopics />
          </SuspenseComp>
        ),
        withAuth: true,
      },
      {
        path: 'favorite',
        element: (
          <SuspenseComp>
            <Bookmark />
          </SuspenseComp>
        ),
        withAuth: true,
      },
      {
        path: 'my-page',
        element: (
          <SuspenseComp>
            <Profile />
          </SuspenseComp>
        ),
        withAuth: true,
      },
      {
        path: '/askLogin',
        element: (
          <SuspenseComp>
            <AskLogin />
          </SuspenseComp>
        ),
        withAuth: false,
      },
      {
        path: '/oauth/redirected/kakao',
        element: (
          <SuspenseComp>
            <KakaoRedirect />
          </SuspenseComp>
        ),
        withAuth: false,
      },
      {
        path: '/search',
        element: <Search />,
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
