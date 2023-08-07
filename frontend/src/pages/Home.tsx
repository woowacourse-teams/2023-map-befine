import Space from '../components/common/Space';
import Box from '../components/common/Box';
import useNavigator from '../hooks/useNavigator';
import TopicListContainer from '../components/TopicListContainer';

const POPULAR_TOPICS_URL = 'popular';
const NEAR_BY_ME_TOPICS_URL = 'near';
const LATEST_TOPICS_URL = 'latest';

const Home = () => {
  const { routePage } = useNavigator();

  const goToPopularTopics = () => {
    routePage('topics/see-all', POPULAR_TOPICS_URL);
  };

  const goToNearByMeTopics = () => {
    routePage('topics/see-all', NEAR_BY_ME_TOPICS_URL);
  };

  const goToLatestTopics = () => {
    routePage('topics/see-all', LATEST_TOPICS_URL);
  };

  return (
    <Box position="relative">
      <TopicListContainer
        containerTitle="인기 급상승한 지도"
        routeWhenSeeAll={goToPopularTopics}
      />
      <TopicListContainer
        containerTitle="내 주변인 지도"
        routeWhenSeeAll={goToNearByMeTopics}
      />
      <TopicListContainer
        containerTitle="새로운 지도"
        routeWhenSeeAll={goToLatestTopics}
      />
      <Space size={2} />
    </Box>
  );
};

export default Home;
