import { useEffect, useState } from 'react';
import ReactGA from 'react-ga4';
import { useLocation } from 'react-router-dom';

const RouteChangeTracker = () => {
  const location = useLocation();
  const [initialized, setInitialized] = useState(false);

  useEffect(() => {
    if (process.env.REACT_APP_GOOGLE_ANALYTICS) {
      ReactGA.initialize(process.env.REACT_APP_GOOGLE_ANALYTICS);
      setInitialized(true);
    }
  }, []);

  // location 변경 감지시 pageview 이벤트 전송
  useEffect(() => {
    if (initialized) {
      ReactGA.set({ page: location.pathname });
      ReactGA.send('pageview');
    }
  }, [initialized, location]);
};

export default RouteChangeTracker;
