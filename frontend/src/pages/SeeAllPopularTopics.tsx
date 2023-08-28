import Text from '../components/common/Text';
import Space from '../components/common/Space';
import { styled } from 'styled-components';
import Box from '../components/common/Box';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import { FULLSCREEN } from '../constants';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';
import { Suspense, lazy } from 'react';
import TopicCardContainerSkeleton from '../components/TopicCardContainer/TopicCardContainerSkeleton';
import useNavigator from '../hooks/useNavigator';

const TopicCardList = lazy(() => import('../components/TopicCardList'));

const SeeAllTopics = () => {
  const { routePage } = useNavigator();
  useSetLayoutWidth(FULLSCREEN);
  useSetNavbarHighlight('home');

  const goToHome = () => {
    routePage('/');
  };

  return (
    <Wrapper>
      <Space size={5} />
      <Text color="black" $fontSize="extraLarge" $fontWeight="bold">
        인기 급상승할 지도?
      </Text>

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
};

const Wrapper = styled(Box)`
  width: 1036px;
  margin: 0 auto;
`;

export default SeeAllTopics;
