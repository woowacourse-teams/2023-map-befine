import { ReactNode, useEffect } from 'react';

import useNavigator from '../../hooks/useNavigator';

interface RedirectLayoutProps {
  children: ReactNode;
  withAuth: boolean;
  to?: string;
}

const getUserTokenInLocalStorage = () => localStorage.getItem('userToken');

function AuthLayout({
  children,
  withAuth,
  to = '/askLogin',
}: RedirectLayoutProps) {
  const { routePage } = useNavigator();

  useEffect(() => {
    const curAuth: boolean = getUserTokenInLocalStorage() !== null;

    if (withAuth) {
      if (!curAuth) routePage(to);
    }

    if (!withAuth) {
      if (curAuth) routePage(to);
    }
  });

  return <>{children}</>;
}

export default AuthLayout;
