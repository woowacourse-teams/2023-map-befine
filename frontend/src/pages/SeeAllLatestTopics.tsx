import { styled } from 'styled-components';
import Space from '../components/common/Space';
import Text from '../components/common/Text';
import { FULLSCREEN } from '../constants';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';
import Box from '../components/common/Box';
import { Suspense, lazy } from 'react';
import TopicCardContainerSkeleton from '../components/Skeletons/TopicListSkeleton';
import useNavigator from '../hooks/useNavigator';
import { setFullScreenResponsive } from '../constants/responsive';

const TopicCardList = lazy(() => import('../components/TopicCardList'));

const SeeAllLatestTopics = () => {
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
        새로울 지도?
      </Text>

      <Space size={5} />

      <Suspense fallback={<TopicCardContainerSkeleton />}>
        <TopicCardList
          url="/topics/newest"
          errorMessage="로그인 후 이용해주세요."
          commentWhenEmpty="최근에 핀이 찍힌 지도가 없습니다."
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

  ${setFullScreenResponsive()}
`;

export default SeeAllLatestTopics;
