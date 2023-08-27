import Space from '../components/common/Space';
import Box from '../components/common/Box';
import useNavigator from '../hooks/useNavigator';
import { styled } from 'styled-components';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import { FULLSCREEN } from '../constants';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';
import { Suspense, lazy, useContext, useEffect } from 'react';
import { MarkerContext } from '../context/MarkerContext';
import TopicCardListSkeleton from '../components/TopicCardList/TopicCardListSkeleton';

const TopicListContainer = lazy(
  () => import('../components/TopicListContainer'),
);

const Home = () => {
  const { routePage } = useNavigator();
  const { markers, removeMarkers, removeInfowindows } =
    useContext(MarkerContext);
  useSetLayoutWidth(FULLSCREEN);
  useSetNavbarHighlight('home');

  const goToPopularTopics = () => {
    routePage('see-all/popularity');
  };

  const goToNearByMeTopics = () => {
    routePage('see-all/near');
  };

  const goToLatestTopics = () => {
    routePage('see-all/latest');
  };

  useEffect(() => {
    if (markers.length > 0) {
      removeMarkers();
      removeInfowindows();
    }
  }, []);

  return (
    <>
      <Wrapper position="relative">
        <Space size={5} />
        <Suspense fallback={<TopicCardListSkeleton />}>
          <TopicListContainer
            url="/topics/bests"
            containerTitle="인기 급상승할 지도?"
            containerDescription="즐겨찾기가 많이 된 지도를 확인해보세요."
            routeWhenSeeAll={goToPopularTopics}
          />
        </Suspense>

        <Space size={9} />

        <Suspense fallback={<TopicCardListSkeleton />}>
          <TopicListContainer
            url="/topics/newest"
            containerTitle="새로울 지도?"
            containerDescription="방금 새로운 핀이 추가된 지도를 확인해보세요."
            routeWhenSeeAll={goToLatestTopics}
          />
        </Suspense>

        <Space size={9} />

        <Suspense fallback={<TopicCardListSkeleton />}>
          <TopicListContainer
            url="/topics"
            containerTitle="모두일 지도?"
            containerDescription="괜찮을지도의 모든 지도를 확인해보세요."
            routeWhenSeeAll={goToNearByMeTopics}
          />
        </Suspense>

        <Space size={5} />
      </Wrapper>
    </>
  );
};

const Wrapper = styled(Box)`
  width: 1036px;
  margin: 0 auto;
`;

export default Home;
