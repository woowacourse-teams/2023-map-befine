import { Outlet } from 'react-router-dom';
import Layout from '../components/Layout';
import LayoutWidthProvider from '../context/LayoutWidthContext';

const RootPage = () => {
  return (
    <>
      <LayoutWidthProvider>
        <Layout>
          <Outlet />
        </Layout>
      </LayoutWidthProvider>
    </>
  );
};

export default RootPage;
