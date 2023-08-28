import { styled } from 'styled-components';
import { FULLSCREEN } from '../constants';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';
import Box from '../components/common/Box';
import Space from '../components/common/Space';
import Text from '../components/common/Text';
import TopicCardContainerSkeleton from '../components/TopicCardContainer/TopicCardContainerSkeleton';
import { Suspense, lazy } from 'react';
import useNavigator from '../hooks/useNavigator';

const TopicCardList = lazy(() => import('../components/TopicCardList'));

const SeeAllNearTopics = () => {
  const { routePage } = useNavigator();
  useSetLayoutWidth(FULLSCREEN);
  useSetNavbarHighlight('home');

  const goToHome = () => {
    routePage('/new-topic');
  };

  return (
    <Wrapper>
      <Space size={5} />
      <Text color="black" $fontSize="extraLarge" $fontWeight="bold">
        내 주변일 지도?
      </Text>

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
};

const Wrapper = styled(Box)`
  width: 1036px;
  margin: 0 auto;
`;

export default SeeAllNearTopics;
