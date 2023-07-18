import { Route, Routes } from 'react-router-dom';
import Layout from './components/common/Layout';
import Home from './pages/Home';
import SelectedTopic from './pages/SelectedTopic';
import NewTopic from './pages/NewTopic';
import NewPin from './pages/NewPin';

const App = () => {
  return (
    <>
      <Layout>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="topics/:topicId" element={<SelectedTopic />} />
          <Route path="new-topic" element={<NewTopic />} />
          <Route path="new-pin" element={<NewPin topicName="" />} />
        </Routes>
      </Layout>
    </>
  );
};

export default App;
