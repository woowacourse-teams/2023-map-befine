import { Outlet } from 'react-router-dom';
import Layout from '../components/common/Layout';

const RootPage = () => {
  return (
    <>
      <Layout>
        <Outlet />
      </Layout>
    </>
  );
};

export default RootPage;
