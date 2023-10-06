import { lazy, Suspense, useContext, useEffect } from 'react';
import { styled } from 'styled-components';

import Space from '../components/common/Space';
import SearchBar from '../components/SearchBar/SearchBar';
import TopicCardContainerSkeleton from '../components/Skeletons/TopicListSkeleton';
import { FULLSCREEN } from '../constants';
import { setFullScreenResponsive } from '../constants/responsive';
import { MarkerContext } from '../context/MarkerContext';
import { SeeTogetherContext } from '../context/SeeTogetherContext';
import useNavigator from '../hooks/useNavigator';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';

const TopicListContainer = lazy(
  () => import('../components/TopicCardContainer'),
);

function Home() {
  const { routingHandlers } = useNavigator();
  const { goToPopularTopics, goToLatestTopics, goToNearByMeTopics } =
    routingHandlers;
  const { seeTogetherTopics, setSeeTogetherTopics } =
    useContext(SeeTogetherContext);
  const { markers, removeMarkers, removeInfowindows } =
    useContext(MarkerContext);
  const accessToken = localStorage.getItem('userToken');

  useSetLayoutWidth(FULLSCREEN);
  useSetNavbarHighlight('home');

  useEffect(() => {
    if (accessToken === null && seeTogetherTopics?.length !== 0) {
      setSeeTogetherTopics([]);
    }
  }, []);

  useEffect(() => {
    if (markers && markers.length > 0) {
      removeMarkers();
      removeInfowindows();
    }
  }, []);

  return (
    <Wrapper>
      <Space size={1} />
      <SearchBar />
      <Space size={1} />
      <Suspense fallback={<TopicCardContainerSkeleton />}>
        <TopicListContainer
          url="/topics/bests"
          containerTitle="인기 급상승할 지도?"
          containerDescription="즐겨찾기가 많이 된 지도를 확인해보세요."
          routeWhenSeeAll={goToPopularTopics}
        />
      </Suspense>

      <Space size={9} />

      <Suspense fallback={<TopicCardContainerSkeleton />}>
        <TopicListContainer
          url="/topics/newest"
          containerTitle="새로울 지도?"
          containerDescription="방금 핀이 추가된 지도를 확인해보세요."
          routeWhenSeeAll={goToLatestTopics}
        />
      </Suspense>

      <Space size={9} />

      <Suspense fallback={<TopicCardContainerSkeleton />}>
        <TopicListContainer
          url="/topics"
          containerTitle="모두일 지도?"
          containerDescription="괜찮을지도의 모든 지도를 확인해보세요."
          routeWhenSeeAll={goToNearByMeTopics}
        />
      </Suspense>

      <Space size={5} />
    </Wrapper>
  );
}

const Wrapper = styled.article`
  width: 1036px;
  margin: 0 auto;
  position: relative;

  ${setFullScreenResponsive()}
`;

export default Home;
