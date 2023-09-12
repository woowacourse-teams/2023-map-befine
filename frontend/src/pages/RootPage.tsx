import { Outlet } from 'react-router-dom';
import Layout from '../components/Layout';
import LayoutWidthProvider from '../context/LayoutWidthContext';
import NavbarHighlightsProvider from '../context/NavbarHighlightsContext';

const RootPage = () => {
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
