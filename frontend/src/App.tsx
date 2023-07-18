import { Route, Routes } from 'react-router-dom';
import Layout from './components/common/Layout';
import Home from './pages/Home';
import PinDetail from './pages/PinDetail';
import Pins from './pages/Pins';

const App = () => {
  return (
    <>
      <Layout>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="topics/:topicId" element={<Pins />} />
        </Routes>
      </Layout>
    </>
  );
};

export default App;
