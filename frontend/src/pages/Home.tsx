import Space from '../components/common/Space';
import Box from '../components/common/Box';
import useNavigator from '../hooks/useNavigator';
import TopicListContainer from '../components/TopicListContainer';
import { useContext, useEffect } from 'react';
import { LayoutWidthContext } from '../context/LayoutWidthContext';

const POPULAR_TOPICS_TITLE = '인기 급상승한 지도';
const NEAR_BY_ME_TOPICS_TITLE = '내 주변인 지도';
const LATEST_TOPICS_TITLE = '새로운 지도';

const POPULAR_TOPICS_URL = '/topics';
const NEAR_BY_ME_TOPICS_URL = '/topics';
const LATEST_TOPICS_URL = '/topics';

const Home = () => {
  const { routePage } = useNavigator();
  const { setWidth } = useContext(LayoutWidthContext);

  const goToPopularTopics = () => {
    routePage(
      'topics/see-all',
      `${POPULAR_TOPICS_URL}|${POPULAR_TOPICS_TITLE}`,
    );
  };

  const goToNearByMeTopics = () => {
    routePage(
      'topics/see-all',
      `${NEAR_BY_ME_TOPICS_URL}|${NEAR_BY_ME_TOPICS_TITLE}`,
    );
  };

  const goToLatestTopics = () => {
    routePage('topics/see-all', `${LATEST_TOPICS_URL}|${LATEST_TOPICS_TITLE}`);
  };

  // useEffect(() => {
  //   setWidth('100vw');
  // }, []);

  return (
    <Box position="relative">
      <TopicListContainer
        containerTitle="인기 급상승한 지도"
        routeWhenSeeAll={goToPopularTopics}
      />
      <Space size={4} />
      <TopicListContainer
        containerTitle="내 주변인 지도"
        routeWhenSeeAll={goToNearByMeTopics}
      />
      <Space size={4} />
      <TopicListContainer
        containerTitle="새로운 지도"
        routeWhenSeeAll={goToLatestTopics}
      />
    </Box>
  );
};

export default Home;
