import { ReactNode, useEffect } from 'react';
import useNavigator from '../../hooks/useNavigator';

interface RedirectLayoutProps {
  children: ReactNode;
  withAuth: boolean;
  to?: string;
}

const getUserTokenInLocalStorage = () => {
  return localStorage.getItem('userToken');
};

const AuthLayout = ({
  children,
  withAuth,
  to = '/askLogin',
}: RedirectLayoutProps) => {
  const { routePage } = useNavigator();

  useEffect(() => {
    let curAuth: boolean = getUserTokenInLocalStorage() === null ? false : true;

    if (withAuth) {
      if (!curAuth) routePage(to);
    }

    if (!withAuth) {
      if (curAuth) routePage(to);
    }
  });

  return <>{children}</>;
};

export default AuthLayout;
