import { lazy, Suspense } from 'react';
import { styled } from 'styled-components';

import Box from '../components/common/Box';
import Space from '../components/common/Space';
import MediaSpace from '../components/common/Space/MediaSpace';
import MediaText from '../components/common/Text/MediaText';
import TopicListSkeleton from '../components/Skeletons/TopicListSkeleton';
import { ARIA_FOCUS, FULLSCREEN } from '../constants';
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
    <Wrapper as="section">
      <Space size={5} />
      <MediaText
        as="h2"
        color="black"
        $fontSize="extraLarge"
        $fontWeight="bold"
        tabIndex={ARIA_FOCUS}
        aria-label="모두일 지도 전체보기 페이지 입니다."
      >
        모두일 지도?
      </MediaText>

      <MediaSpace size={6} />

      <Suspense fallback={<TopicListSkeleton />}>
        <TopicCardList
          url="/topics"
          errorMessage="로그인 후 이용해주세요."
          commentWhenEmpty="지도가 없습니다. 추가하기 버튼을 눌러 지도를 추가해보세요."
          pageCommentWhenEmpty="지도 만들러 가기"
          routePage={goToHome}
        />
      </Suspense>

      <Space size={8} />
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
