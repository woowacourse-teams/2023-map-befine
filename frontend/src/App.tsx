import { Route, Routes } from 'react-router-dom';
import Layout from './components/common/Layout';
import Home from './pages/Home';
import PinDetail from './pages/PinDetail';
import Topics from './pages/Topics';

const App = () => {
  return (
    <>
      <Layout>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="topics/:topicId" element={<Topics />} />
        </Routes>
      </Layout>
    </>
  );
};

export default App;
