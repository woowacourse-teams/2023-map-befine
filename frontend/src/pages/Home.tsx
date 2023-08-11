import Space from '../components/common/Space';
import Box from '../components/common/Box';
import useNavigator from '../hooks/useNavigator';
import TopicListContainer from '../components/TopicListContainer';
import { useContext, useEffect } from 'react';
import { LayoutWidthContext } from '../context/LayoutWidthContext';
import { styled } from 'styled-components';

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

  useEffect(() => {
    setWidth('100vw');
  }, []);

  return (
    <Wrapper position="relative">
      <TopicListContainer
        containerTitle="인기 급상승한 지도?"
        containerDescription="즐겨찾기가 많이 된 지도를 확인해보세요."
        routeWhenSeeAll={goToPopularTopics}
      />
      <Space size={9} />
      <TopicListContainer
        containerTitle="내 주변인 지도"
        containerDescription="내 주변에 있는 지도를 확인해보세요."
        routeWhenSeeAll={goToNearByMeTopics}
      />
      <Space size={9} />
      <TopicListContainer
        containerTitle="새로운 지도"
        containerDescription="방금 새로운 핀이 추가된 지도를 확인해보세요."
        routeWhenSeeAll={goToLatestTopics}
      />
    </Wrapper>
  );
};

const Wrapper = styled(Box)`
  width: 1036px;
  margin: 0 auto;
`;

export default Home;
