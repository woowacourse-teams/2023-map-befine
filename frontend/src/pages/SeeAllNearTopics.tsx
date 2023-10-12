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

function SeeAllNearTopics() {
  const { routePage } = useNavigator();
  useSetLayoutWidth(FULLSCREEN);
  useSetNavbarHighlight('home');

  const goToHome = () => {
    routePage('/new-topic');
  };

  return (
    <Wrapper>
      <Space size={5} />
      <MediaText color="black" $fontSize="extraLarge" $fontWeight="bold">
        내 주변일 지도?
      </MediaText>

      <Space size={5} />

      <Suspense fallback={<TopicCardContainerSkeleton />}>
        <TopicCardList
          url="/topics"
          errorMessage="로그인 후 이용해주세요."
          commentWhenEmpty="지도가 없습니다. 추가하기 버튼을 눌러 지도를 추가해보세요."
          pageCommentWhenEmpty="지도 만들러 가기"
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

export default SeeAllNearTopics;
