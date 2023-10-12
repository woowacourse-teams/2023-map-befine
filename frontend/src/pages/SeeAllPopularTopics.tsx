import { lazy, Suspense } from 'react';
import { styled } from 'styled-components';

import Box from '../components/common/Box';
import Space from '../components/common/Space';
import MediaText from '../components/common/Text/mediaText';
import TopicCardContainerSkeleton from '../components/Skeletons/TopicListSkeleton';
import { FULLSCREEN } from '../constants';
import useNavigator from '../hooks/useNavigator';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';

const TopicCardList = lazy(() => import('../components/TopicCardList'));

function SeeAllTopics() {
  const { routePage } = useNavigator();
  useSetLayoutWidth(FULLSCREEN);
  useSetNavbarHighlight('home');

  const goToHome = () => {
    routePage('/');
  };

  return (
    <Wrapper>
      <Space size={5} />
      <MediaText color="black" $fontSize="extraLarge" $fontWeight="bold">
        인기 급상승할 지도?
      </MediaText>

      <Space size={5} />

      <Suspense fallback={<TopicCardContainerSkeleton />}>
        <TopicCardList
          url="/topics/bests"
          errorMessage="로그인 후 이용해주세요."
          commentWhenEmpty="즐겨찾기가 많이 된 지도가 없습니다."
          pageCommentWhenEmpty="메인페이지로 가기"
          routePage={goToHome}
        />
      </Suspense>
    </Wrapper>
  );
}

const Wrapper = styled(Box)`
  width: 1140px;
  margin: 0 auto;
  position: relative;

  @media (max-width: 1180px) {
    width: 100%;
  }
`;

export default SeeAllTopics;
