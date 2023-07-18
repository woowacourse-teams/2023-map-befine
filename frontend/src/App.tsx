import { Route, Routes } from 'react-router-dom';
import Layout from './components/common/Layout';
import Home from './pages/Home';
import PinDetail from './pages/PinDetail';
import Pins from './pages/Pins';
import NewTopic from './pages/NewTopic';
import NewPin from './pages/NewPin';

const App = () => {
  return (
    <>
      <Layout>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="topics/:topicId" element={<Pins />} />
          <Route path="new-topic" element={<NewTopic />} />
          <Route path="new-pin" element={<NewPin topicName="" />} />
        </Routes>
      </Layout>
    </>
  );
};

export default App;
