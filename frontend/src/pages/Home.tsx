import { lazy, Suspense, useContext, useEffect } from 'react';
import { styled } from 'styled-components';

import Banner from '../components/Banner';
import Space from '../components/common/Space';
import MediaSpace from '../components/common/Space/MediaSpace';
import SearchBar from '../components/SearchBar/SearchBar';
import TopicCardContainerSkeleton from '../components/Skeletons/TopicListSkeleton';
import { FULLSCREEN } from '../constants';
import { MarkerContext } from '../context/MarkerContext';
import { SeeTogetherContext } from '../context/SeeTogetherContext';
import useNavigator from '../hooks/useNavigator';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';
import useToast from '../hooks/useToast';

const TopicListContainer = lazy(
  () => import('../components/TopicCardContainer'),
);

function Home() {
  const accessToken = localStorage.getItem('userToken');
  const { routingHandlers } = useNavigator();
  const { goToPopularTopics, goToLatestTopics, goToNearByMeTopics } =
    routingHandlers;
  const { seeTogetherTopics, setSeeTogetherTopics } =
    useContext(SeeTogetherContext);
  const { markers, removeMarkers, removeInfowindows } =
    useContext(MarkerContext);
  const { showToast } = useToast();

  useSetLayoutWidth(FULLSCREEN);
  useSetNavbarHighlight('home');

  useEffect(() => {
    if (accessToken === null && seeTogetherTopics?.length !== 0) {
      setSeeTogetherTopics([]);
      showToast('info', '로그인을 하면 모아보기가 유지돼요.');
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
      <SearchBar />
      <Space size={4} />

      <Banner />
      <Space size={6} />

      <Suspense fallback={<TopicCardContainerSkeleton />}>
        <TopicListContainer
          url="/topics/bests"
          containerTitle="인기 급상승할 지도?"
          containerDescription="즐겨찾기가 많이 된 지도를 확인해보세요."
          routeWhenSeeAll={goToPopularTopics}
        />
      </Suspense>

      <MediaSpace size={9} />

      <Suspense fallback={<TopicCardContainerSkeleton />}>
        <TopicListContainer
          url="/topics/newest"
          containerTitle="새로울 지도?"
          containerDescription="방금 핀이 추가된 지도를 확인해보세요."
          routeWhenSeeAll={goToLatestTopics}
        />
      </Suspense>

      <MediaSpace size={9} />

      <Suspense fallback={<TopicCardContainerSkeleton />}>
        <TopicListContainer
          url="/topics"
          containerTitle="모두일 지도?"
          containerDescription="괜찮을지도의 모든 지도를 확인해보세요."
          routeWhenSeeAll={goToNearByMeTopics}
        />
      </Suspense>

      <Space size={8} />
    </Wrapper>
  );
}

const Wrapper = styled.article`
  width: 1140px;
  margin: 0 auto;
  position: relative;

  @media (max-width: 1180px) {
    width: 100%;
  }
`;

export default Home;
