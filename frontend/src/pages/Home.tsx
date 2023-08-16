import Space from '../components/common/Space';
import Box from '../components/common/Box';
import useNavigator from '../hooks/useNavigator';
import TopicListContainer from '../components/TopicListContainer';
import { styled } from 'styled-components';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import { FULLSCREEN } from '../constants';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';

const Home = () => {
  const { routePage } = useNavigator();
  const { width: _ } = useSetLayoutWidth(FULLSCREEN);
  const { navbarHighlights: __ } = useSetNavbarHighlight('home');

  const goToPopularTopics = () => {
    routePage('see-all/popularity');
  };

  const goToNearByMeTopics = () => {
    routePage('see-all/near');
  };

  const goToLatestTopics = () => {
    routePage('see-all/latest');
  };

  return (
    <>
      <Wrapper position="relative">
        <TopicListContainer
          containerTitle="인기 급상승할 지도?"
          containerDescription="즐겨찾기가 많이 된 지도를 확인해보세요."
          routeWhenSeeAll={goToPopularTopics}
        />
        <Space size={9} />
        <TopicListContainer
          containerTitle="내 주변일 지도?"
          containerDescription="내 주변에 있는 지도를 확인해보세요."
          routeWhenSeeAll={goToNearByMeTopics}
        />
        <Space size={9} />
        <TopicListContainer
          containerTitle="새로울 지도?"
          containerDescription="방금 새로운 핀이 추가된 지도를 확인해보세요."
          routeWhenSeeAll={goToLatestTopics}
        />
      </Wrapper>
    </>
  );
};

const Wrapper = styled(Box)`
  width: 1036px;
  margin: 0 auto;
`;

const ModalWrapper = styled.div`
  width: 300px;
  height: 300px;
  background-color: white;
`;

export default Home;
