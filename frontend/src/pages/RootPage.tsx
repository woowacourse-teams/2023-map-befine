import { Outlet } from 'react-router-dom';
import Layout from '../components/Layout';
import LayoutWidthProvider from '../context/LayoutWidthContext';
import NavbarHighlightsProvider from '../context/NavbarHighlightsContext';
import RouteChangeTracker from '../utils/RouteChangeTracker';

const RootPage = () => {
  RouteChangeTracker();
  return (
    <>
      <LayoutWidthProvider>
        <NavbarHighlightsProvider>
          <Layout>
            <Outlet />
          </Layout>
        </NavbarHighlightsProvider>
      </LayoutWidthProvider>
    </>
  );
};

export default RootPage;
