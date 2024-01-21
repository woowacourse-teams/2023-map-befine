import { Suspense } from 'react';
import { styled } from 'styled-components';

import Box from '../components/common/Box';
import Space from '../components/common/Space';
import MediaSpace from '../components/common/Space/MediaSpace';
import MediaText from '../components/common/Text/MediaText';
import TopicListSkeleton from '../components/Skeletons/TopicListSkeleton';
import TopicCardList from '../components/TopicCardList';
import { ARIA_FOCUS, FULLSCREEN } from '../constants';
import useNavigator from '../hooks/useNavigator';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';

function SeeAllAllTopics() {
  useSetLayoutWidth(FULLSCREEN);
  useSetNavbarHighlight('home');

  const { routingHandlers } = useNavigator();

  return (
    <Wrapper>
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
          commentWhenEmpty="생성된 지도가 없습니다. 지도를 만들어보세요."
          routePageName="지도 만들러 가기"
          routePage={routingHandlers.newTopic}
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

export default SeeAllAllTopics;
